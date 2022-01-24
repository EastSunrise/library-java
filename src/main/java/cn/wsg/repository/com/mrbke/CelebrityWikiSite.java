package cn.wsg.repository.com.mrbke;

import cn.wsg.commons.DatetimeConsts;
import cn.wsg.commons.data.MetadataParser;
import cn.wsg.commons.data.common.BloodType;
import cn.wsg.commons.data.common.Constellation;
import cn.wsg.commons.data.common.Gender;
import cn.wsg.commons.data.common.Zodiac;
import cn.wsg.commons.data.intangible.Length;
import cn.wsg.commons.data.intangible.LengthUnit;
import cn.wsg.commons.data.intangible.Mass;
import cn.wsg.commons.data.intangible.MassUnit;
import cn.wsg.commons.function.TriFunction;
import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.page.AmountCountablePage;
import cn.wsg.commons.internet.page.Page;
import cn.wsg.commons.internet.page.PageIndex;
import cn.wsg.commons.internet.repository.ListRepository;
import cn.wsg.commons.internet.repository.support.Repositories;
import cn.wsg.commons.internet.support.CssSelectors;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.util.EnumUtilExt;
import cn.wsg.commons.util.MapUtilsExt;
import cn.wsg.commons.util.NetUtils;
import cn.wsg.commons.util.RegExUtilsExt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.RequestBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kingen
 */
@Slf4j
public final class CelebrityWikiSite extends BaseSiteClient implements CelebrityWiki {

    private static final String HOME_PAGE = "http://www.mrbke.com";
    private static final String NO_PERSON_IMG = "noperson.jpg";
    private static final String SEPARATOR_CHARS = "、";
    private static final Set<Integer> EXCEPTS = Set.of(821, 1539, 1568, 1757, 4017);

    public CelebrityWikiSite() {
        super("Wiki of Celebrities", HttpHost.create("https://www.mrbke.com"));
    }

    @Override
    public ListRepository<CelebrityIndex, Celebrity> getRepository() {
        List<CelebrityIndex> indices = findAllCelebrityIndices().stream().filter(idx -> !EXCEPTS.contains(idx.getId()))
            .sorted(Comparator.comparingInt(CelebrityIndex::getId)).collect(Collectors.toList());
        return Repositories.list(this, indices);
    }

    @Override
    public List<CelebrityIndex> findAllCelebrityIndices() {
        Document document = this.findDocument(this.httpGet("/comp/sitemap/"));
        Elements as = document.select(".ulist").last().select(CssSelectors.TAG_A);
        return as.stream().map(this::getCelebrityIndex).collect(Collectors.toList());
    }

    @Override
    public AmountCountablePage<CelebrityIndex> findByPage(PageIndex pageIndex, CelebrityType type)
        throws NotFoundException {
        int current = pageIndex.getCurrent();
        String path = Optional.ofNullable(type).map(Enum::name).map(String::toLowerCase).orElse("mingren");
        String page = current == 0 ? "" : ("_" + (current + 1));
        Document document = getDocument(httpGet("/%s/index%s.html", path, page));

        Element box = document.selectFirst(".personbox");
        Elements a = box.select("ul a");
        List<CelebrityIndex> indices = a.stream().map(this::getCelebrityIndex).collect(Collectors.toList());
        return Page.amountCountable(indices, pageIndex, 100, getTotalOr(box, indices.size()));
    }

    @Override
    public AmountCountablePage<AlbumIndex> findAlbumByPage(PageIndex pageIndex, AlbumType type)
        throws NotFoundException {
        int current = pageIndex.getCurrent();
        String path = Optional.ofNullable(type).map(Enum::name).map(String::toLowerCase).map(s -> '/' + s).orElse("");
        String page = current == 0 ? "" : ("_" + (current + 1));
        Document document = getDocument(httpGet("/tuku%s/index%s.html", path, page));

        Element box = document.selectFirst(".gropbox");
        List<AlbumIndex> indices = box.select("ul li").stream().map(this::getAlbumIndex).collect(Collectors.toList());
        return Page.amountCountable(indices, pageIndex, 30, getTotalOr(box, indices.size()));
    }

    @Override
    public Celebrity findById(CelebrityIndex index) throws NotFoundException {
        String type = index.getType().name().toLowerCase();
        Document doc = getDocument(httpGet("/%s/m%d/info.html", type, index.getId()));
        CelebrityInfo info = getBasicInfo(doc);
        Celebrity celebrity = Objects.requireNonNull(initCelebrity(doc, (i, t, n) -> new Celebrity(i, t, n, info)));

        Element ext = doc.selectFirst(".dataext");
        if (ext != null) {
            Elements elements = ext.select("h2.itit");
            Map<String, Element> extInfo = new HashMap<>(4);
            for (Element ele : elements) {
                if (ele.hasText()) {
                    String key = ele.hasAttr("id") ? ele.id() : ele.text();
                    MapUtilsExt.putIfAbsentOrElseThrow(extInfo, key, ele.nextElementSibling());
                }
            }
            celebrity.setDescriptions(getExtStringList(extInfo, "人物介绍", "人物点评"));
            celebrity.setExperiences(getExtStringList(extInfo, "演艺阅历"));
            celebrity.setGroupLives(getExtStringList(extInfo, "团体生活"));
            celebrity.setAwards(getExtStringList(extInfo, "获奖记载"));
            Element prompt = extInfo.remove("fanghao");
            if (prompt != null) {
                celebrity.setWorks(getWorks(prompt.nextElementSibling()));
            }
            extInfo.remove("hotfanghao");
            if (!extInfo.isEmpty()) {
                log.error("Unhandled extra information: {}", extInfo.keySet());
            }
        }
        return celebrity;
    }

    @Override
    public AdultWork findAdultWork(String id) throws NotFoundException {
        Document document = getDocument(httpGet("/fanhao/%s.html", id));
        Element div = document.selectFirst(".fanhao");
        if (div.childNodeSize() == 1) {
            throw new NotFoundException(div.text());
        }
        SimpleCelebrity celebrity = initCelebrity(document, SimpleCelebrity::new);

        Elements ems = div.select(CssSelectors.TAG_EM);
        MetadataParser parser = MetadataParser.create(ems.size());
        for (Element em : ems) {
            String key = em.text().replace(" ", "");
            String value = ((TextNode)em.nextSibling()).text().substring(1);
            parser.put(key, value);
        }
        String serialNum = parser.getString("番号");
        String title = parser.getString("名称");
        URL image = NetUtils.createURL(div.selectFirst(CssSelectors.TAG_IMG).attr(CssSelectors.ATTR_SRC));
        List<String> actresses = parser.getSeparatedStrings(SEPARATOR_CHARS, "演员");
        AdultWork work = new AdultWork(celebrity, serialNum, title, image, actresses);
        work.setMosaic(parser.getValue(s -> {
            if ("有码".equals(s)) {
                return true;
            }
            if ("无码".equals(s)) {
                return false;
            }
            throw new IllegalArgumentException("Unknown mosaic: " + s);
        }, "是否有码"));
        work.setDuration(parser.getValue(s -> {
            Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.DURATION_REGEX, s);
            return Duration.ofMinutes(Integer.parseInt(matcher.group("d")));
        }, "播放时间"));
        work.setDatePublished(parser.getValue(LocalDate::parse, "发行时间"));
        work.setDirector(parser.getString("导演"));
        work.setProducer(parser.getString("制作商"));
        work.setPublisher(parser.getString("发行商"));
        work.setSeries(parser.getString("系列"));
        work.setTags(parser.getSeparatedStrings("类别"));
        parser.check("整理");
        return work;
    }

    @Override
    public List<AlbumIndex> findAlbumsByCelebrity(CelebrityIndex index) throws NotFoundException {
        String type = index.getType().name().toLowerCase();
        Document document = getDocument(httpGet("/%s/m%s/pic.html", type, index.getId()));
        return document.select("#xiezhen li").stream().map(this::getAlbumIndex).collect(Collectors.toList());
    }

    /**
     * Retrieves an album of the given identifier.
     */
    @Override
    public Album findAlbum(AlbumIndex index) throws NotFoundException {
        String type = index.getType().name().toLowerCase();
        Document document = this.getDocument(this.httpGet("/tuku/%s/%d.html", type, index.getId()));
        Element show = document.selectFirst("div.picshow");
        String title = show.selectFirst(CssSelectors.TAG_H1).text();
        String timeStr = ((TextNode)show.selectFirst(".info").childNode(0)).text();
        LocalDateTime updateTime = LocalDateTime.parse(timeStr, DatetimeConsts.DTF_YYYY_MM_DD_HH_MM_SS);
        List<URL> images = show.select("#pictureurls img").stream().map(img -> img.attr(CssSelectors.ATTR_REL))
            .map(NetUtils::createURL).collect(Collectors.toList());
        Album album = new Album(index.getId(), index.getType(), title, images, updateTime);

        Element text = show.selectFirst(".text");
        List<CelebrityIndex> related = new ArrayList<>();
        for (Element a : text.select(">a")) {
            String href = a.attr(CssSelectors.ATTR_HREF);
            if (href.isEmpty() || HOME_PAGE.equals(href)) {
                continue;
            }
            related.add(this.getCelebrityIndex(a));
        }
        if (!related.isEmpty()) {
            album.setRelatedCelebrities(related);
        }
        Elements tags = text.select(".ptags a");
        if (!tags.isEmpty()) {
            album.setKeywords(new HashSet<>(tags.eachText()));
        }
        Element a = show.selectFirst(".next a");
        if (!"最后一页".equals(a.attr(CssSelectors.ATTR_TITLE))) {
            Matcher matcher = RegExUtilsExt.findOrElseThrow(Lazy.ALBUM_URL_REGEX, a.attr(CssSelectors.ATTR_HREF));
            int nextId = Integer.parseInt(matcher.group("id"));
            AlbumType nextType = AlbumType.valueOf(matcher.group("t").toUpperCase());
            String nextTitle = a.attr(CssSelectors.ATTR_TITLE);
            album.setNextId(new AlbumIndex(nextId, nextType, nextTitle));
        }
        return album;
    }

    @Override
    public Document getDocument(RequestBuilder builder) throws NotFoundException {
        Document document = super.getDocument(builder);
        if ("提示信息".equals(document.title())) {
            throw new NotFoundException(document.selectFirst("div.content").text());
        }
        return document;
    }

    private int getTotalOr(Element box, int defaultValue) {
        Element a1 = box.selectFirst(".pagePg .a1");
        if (a1 == null) {
            return defaultValue;
        }
        String text = a1.text();
        return Integer.parseInt(text.substring(0, text.length() - 1));
    }

    /**
     * Initializes basic properties of a celebrity.
     */
    private <T extends SimpleCelebrity> T initCelebrity(Document document,
        TriFunction<Integer, CelebrityType, String, T> constructor) {
        Element current = document.selectFirst(".current");
        if (current == null) {
            return null;
        }
        CelebrityIndex index = getCelebrityIndex(current);
        String name = document.selectFirst(".mx_name").text();
        T t = constructor.apply(index.getId(), index.getType(), name);

        Element block = document.selectFirst(".cm_block01");
        String image = block.selectFirst(CssSelectors.TAG_IMG).attr(CssSelectors.ATTR_SRC);
        if (!image.endsWith(NO_PERSON_IMG)) {
            t.setImage(NetUtils.createURL(image));
        }
        Elements tags = block.selectFirst(".txt03").select(CssSelectors.TAG_A);
        if (!tags.isEmpty()) {
            t.setKeywords(new HashSet<>(tags.eachText()));
        }
        return t;
    }

    private CelebrityIndex getCelebrityIndex(Element a) {
        Matcher matcher = RegExUtilsExt.findOrElseThrow(Lazy.CELEBRITY_URL_REGEX, a.attr(CssSelectors.ATTR_HREF));
        CelebrityType type = CelebrityType.valueOf(matcher.group("t").toUpperCase());
        int id = Integer.parseInt(matcher.group("id"));
        return new CelebrityIndex(id, type, a.text());
    }

    private CelebrityInfo getBasicInfo(Document document) {
        Elements ems = document.select(".datacon em");
        MetadataParser parser = MetadataParser.create(ems.size());
        for (Element em : ems) {
            String value = ((TextNode)em.nextSibling()).text().substring(1).strip();
            if (StringUtils.isBlank(value) || "暂无".equals(value)) {
                continue;
            }
            String key = em.text().replace(" ", "");
            parser.put(key, value);
        }
        CelebrityInfo info = new CelebrityInfo();
        info.setGender(parser.getEnum(Gender.class, (s, e) -> Objects.equals(s, e.getDisplayName()), "性别"));
        info.setFullName(parser.getString("姓名"));
        info.setZhNames(parser.getSeparatedStrings(SEPARATOR_CHARS, "中文名"));
        info.setJaNames(parser.getSeparatedStrings(SEPARATOR_CHARS, "日文名"));
        info.setEnNames(parser.getSeparatedStrings(SEPARATOR_CHARS, "英文名"));
        Set<String> aka = new HashSet<>();
        Optional.ofNullable(parser.getSeparatedStrings(SEPARATOR_CHARS, "别名")).ifPresent(aka::addAll);
        Optional.ofNullable(parser.getSeparatedStrings(SEPARATOR_CHARS, "外文名")).ifPresent(aka::addAll);
        Optional.ofNullable(parser.getSeparatedStrings(SEPARATOR_CHARS, "艺名")).ifPresent(aka::addAll);
        Stream<String> stream = aka.stream().distinct().filter(s -> !Objects.equals(s, info.getFullName()));
        if (info.getZhNames() != null) {
            stream = stream.filter(s -> !info.getZhNames().contains(s));
        }
        if (info.getEnNames() != null) {
            stream = stream.filter(s -> !info.getEnNames().contains(s));
        }
        if (info.getJaNames() != null) {
            stream = stream.filter(s -> !info.getJaNames().contains(s));
        }
        info.setAka(stream.collect(Collectors.toList()));

        info.setZodiac(parser.getEnum(Zodiac.class, (s, e) -> Objects.equals(s, e.getZhName()), "生肖"));
        info.setConstellation(parser.getValue(key -> Objects.requireNonNullElseGet(Lazy.CONSTELLATION_ALIAS.get(key),
            () -> EnumUtilExt.valueOf(Constellation.class, key,
                (s, e) -> Objects.equals(s, e.getZhName()) || Objects.equals(s, e.getJaName()) || (e.getAlias() != null
                    && ArrayUtils.contains(e.getAlias(), s)))), "星座"));
        info.setInterests(parser.getSeparatedStrings(SEPARATOR_CHARS, "兴趣", "爱好", "兴趣爱好"));
        info.setHeight(parser.getValue(s -> {
            Matcher matcher = RegExUtilsExt.findOrElseThrow(Lazy.HEIGHT_REGEX, s);
            return new Length(Double.parseDouble(matcher.group("h")), LengthUnit.CENTIMETER);
        }, "身高"));
        info.setWeight(parser.getValue(s -> {
            Matcher matcher = RegExUtilsExt.findOrElseThrow(Lazy.WEIGHT_REGEX, s);
            return new Mass(Double.parseDouble(matcher.group("w")), MassUnit.KILOGRAM);
        }, "体重"));
        info.setFigure(parser.getString("三围"));
        info.setCup(parser.getString("罩杯"));
        info.setBloodType(parser.getValue(s -> {
            if ("0型".equals(s)) {
                return BloodType.O;
            }
            Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.BLOOD_TYPE_REGEX, s);
            return Enum.valueOf(BloodType.class, matcher.group("t").toUpperCase());
        }, "血型"));
        info.setBirthday(parser.getString("出生日期", "生日", "出生时间"));
        info.setOccupations(parser.getSeparatedStrings(SEPARATOR_CHARS, "职业"));
        info.setCountry(parser.getString("国籍", "地区"));
        info.setLanguage(parser.getString("语言"));
        info.setAgency(parser.getString("经纪公司"));
        info.setStartDate(parser.getString("出道时间"));
        info.setRetireDate(parser.getString("隐退时间"));
        info.setFirm(parser.getString("事务所"));
        info.setSchool(parser.getString("毕业院校"));
        info.setBirthplace(parser.getString("出生地"));
        info.setEthnicity(parser.getString("民族"));
        parser.check();
        return info;
    }

    private List<String> getExtStringList(Map<String, Element> extInfo, String... keys) {
        for (String key : keys) {
            Element nextEle = extInfo.remove(key);
            if (nextEle != null) {
                Elements ps = nextEle.select(CssSelectors.TAG_P);
                return ps.isEmpty() ? List.of(nextEle.text()) : ps.eachText();
            }
        }
        return null;
    }

    private Set<String> getWorks(Element icon) {
        Set<String> works = new HashSet<>();
        Element tbody = icon.selectFirst("tbody");
        if (tbody != null) {
            for (Element tr : tbody.select(CssSelectors.TAG_TR)) {
                String serialNum = tr.selectFirst("td").text().strip();
                if (serialNum.isBlank()) {
                    continue;
                }
                works.add(serialNum.toUpperCase());
            }
        }
        Elements labels = icon.select("label.born");
        if (!labels.isEmpty()) {
            labels.eachText().stream().map(String::toUpperCase).forEach(works::add);
        }
        return works;
    }

    private AlbumIndex getAlbumIndex(Element ele) {
        String href = ele.selectFirst(CssSelectors.TAG_A).attr(CssSelectors.ATTR_HREF);
        Matcher matcher = RegExUtilsExt.findOrElseThrow(Lazy.ALBUM_URL_REGEX, href);
        int id = Integer.parseInt(matcher.group("id"));
        AlbumType albumType = AlbumType.valueOf(matcher.group("t").toUpperCase());
        String title = ele.selectFirst(CssSelectors.TAG_IMG).attr(CssSelectors.ATTR_ALT);
        return new AlbumIndex(id, albumType, title);
    }

    private static class Lazy {

        private static final Pattern CELEBRITY_URL_REGEX;
        private static final Pattern ALBUM_URL_REGEX;
        private static final Pattern BLOOD_TYPE_REGEX;
        private static final Pattern DURATION_REGEX = Pattern.compile("(?<d>\\d+)\\s*(min|分|分钟)");
        private static final Pattern HEIGHT_REGEX = Pattern.compile("(?<h>[12]\\d{2}(\\.\\d)?)\\s*(cm|CM|厘米|公分|㎝)?");
        private static final Pattern WEIGHT_REGEX =
            Pattern.compile("(?<w>([1-9]|1[0-3])\\d(\\.\\d)?)\\s*(kg|公斤)", Pattern.CASE_INSENSITIVE);
        private static final Map<String, Constellation> CONSTELLATION_ALIAS =
            Map.of("魔蝎座", Constellation.CAPRICORNUS, "天蠍座", Constellation.SCORPIO, "處女座", Constellation.VIRGO);

        static {
            String types = Arrays.stream(CelebrityType.values()).map(Enum::name).map(String::toLowerCase)
                .collect(Collectors.joining("|"));
            CELEBRITY_URL_REGEX = Pattern.compile("/(?<t>" + types + ")/m(?<id>\\d+)/((info|pic|news|comm)\\.html)?");
            String albumTypes = Arrays.stream(AlbumType.values()).map(Enum::name).map(String::toLowerCase)
                .collect(Collectors.joining("|"));
            ALBUM_URL_REGEX = Pattern.compile("/tuku/(?<t>" + albumTypes + ")/(?<id>\\d+)(_\\d+)?\\.html");
            String bloodTypes = Arrays.stream(BloodType.values()).map(Enum::name).collect(Collectors.joining("|"));
            BLOOD_TYPE_REGEX = Pattern.compile("(?<t>" + bloodTypes + ")\\s*型?", Pattern.CASE_INSENSITIVE);
        }
    }
}
