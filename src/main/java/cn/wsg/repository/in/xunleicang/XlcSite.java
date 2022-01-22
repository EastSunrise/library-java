package cn.wsg.repository.in.xunleicang;

import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.common.video.VideoConsts;
import cn.wsg.commons.internet.common.video.resource.ResourceState;
import cn.wsg.commons.internet.common.video.resource.VideoResourceRepository;
import cn.wsg.commons.internet.download.DownloadLink;
import cn.wsg.commons.internet.download.InvalidLinkException;
import cn.wsg.commons.internet.download.LinkFactory;
import cn.wsg.commons.internet.download.Thunder;
import cn.wsg.commons.internet.repository.ListRepository;
import cn.wsg.commons.internet.repository.support.Repositories;
import cn.wsg.commons.internet.support.CssSelectors;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.internet.support.UnexpectedContentException;
import cn.wsg.commons.util.EnumUtilExt;
import cn.wsg.commons.util.MapUtilsExt;
import cn.wsg.commons.util.NetUtils;
import cn.wsg.commons.util.RegExUtilsExt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.RequestBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Kingen
 * @see <a href="https://www.xunleicang.in/">XunLeiCang</a>
 */
@Slf4j
public final class XlcSite extends BaseSiteClient implements VideoResourceRepository<XlcItem> {

    private static final int TITLE_SUFFIX_LENGTH = 14;
    private static final String NO_PIC = "nopic.jpg";
    private static final String NO_PHOTO = "nophoto_xunleicang.in.png";
    private static final String SYSTEM_TIP = "迅雷仓-系统提示";

    public XlcSite() {
        super("XLC", HttpHost.create("https://www.xunleicang.in"));
    }

    /**
     * Returns the repository of all items from 1 to {@link #latest()}. <strong>About 8% of the items are not
     * found.</strong>
     */
    @Override
    public ListRepository<Integer, XlcItem> getRepository() {
        Stream<Integer> stream = IntStream.rangeClosed(1, latest()).boxed();
        return Repositories.list(this, stream.collect(Collectors.toList()));
    }

    @Override
    public XlcItem findById(Integer id) throws NotFoundException {
        RequestBuilder builder = httpGet("/vod-read-id-%d.html", id);
        Document document = getDocument(builder);
        String title = document.title();
        if (SYSTEM_TIP.equals(title)) {
            throw new NotFoundException(document.body().text().strip());
        }

        Element pLeft = document.selectFirst(".pleft");
        Map<String, Node> info = new HashMap<>(8);
        Elements elements = pLeft.selectFirst(".moviecont").select(CssSelectors.TAG_STRONG);
        for (Element strong : elements) {
            MapUtilsExt.putIfAbsentOrElseThrow(info, strong.text(), strong.nextSibling());
        }
        String dateStr = ((TextNode)info.get("更新时间：")).text();
        LocalDate updateDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        Pair<XlcType, ResourceState> pair = getTypeAndState(document);
        XlcType type = pair.getLeft();
        ResourceState state = pair.getRight();
        title = title.substring(0, title.length() - TITLE_SUFFIX_LENGTH);
        if (StringUtils.isBlank(title)) {
            throw new NotFoundException("Unknown title of the video");
        }

        List<DownloadLink> links = new ArrayList<>();
        Elements lis = document.select("ul.down-list").select("li.item");
        for (Element li : lis) {
            Element a = li.selectFirst(CssSelectors.TAG_A);
            String href = a.attr(CssSelectors.ATTR_HREF);
            if (StringUtils.isBlank(href) || Thunder.isEmptyLink(href)) {
                continue;
            }
            String description = a.text().strip();
            try {
                links.add(LinkFactory.parse(href, StandardCharsets.UTF_8, description,
                    () -> LinkFactory.extractPassword(description, href)));
            } catch (InvalidLinkException e) {
                log.error("{}", e.getMessage());
            }
        }

        URL url = NetUtils.createURL(builder.getUri().toString());
        XlcItem item = new XlcItem(title, type, url, links, updateDate, state);
        String cover = document.selectFirst(".pics3").attr(CssSelectors.ATTR_SRC);
        if (!cover.isBlank() && !cover.endsWith(NO_PIC) && !cover.endsWith(NO_PHOTO)) {
            item.setImage(NetUtils.createURL(cover));
        }

        Element header = pLeft.selectFirst(CssSelectors.TAG_H3).select(CssSelectors.TAG_A).last();
        Element font = header.selectFirst(CssSelectors.TAG_FONT);
        if (font != null) {
            int year = Integer.parseInt(StringUtils.strip(font.text(), "()"));
            if (year >= VideoConsts.MOVIE_START_YEAR && year <= Year.now().getValue()) {
                item.setYear(year);
            }
        }

        return item;
    }

    /**
     * @see <a href="https://www.xunleicang.in/ajax-show-id-new.html">Last Update</a>
     */
    private int latest() {
        Document document = findDocument(httpGet("/ajax-show-id-new.html"));
        Elements as = document.selectFirst("ul.f6").select(CssSelectors.TAG_A);
        int max = 1;
        for (Element a : as) {
            String href = a.attr(CssSelectors.ATTR_HREF);
            String id = RegExUtilsExt.findOrElseThrow(Lazy.ITEM_HREF_REGEX, href).group("id");
            max = Math.max(max, Integer.parseInt(id));
        }
        return max;
    }

    private Pair<XlcType, ResourceState> getTypeAndState(Document document) throws NotFoundException {
        Element pLeft = document.selectFirst(".pleft");
        Element header = pLeft.selectFirst(CssSelectors.TAG_H3).select(CssSelectors.TAG_A).last();
        String href = header.previousElementSibling().attr(CssSelectors.ATTR_HREF);
        Matcher matcher = Lazy.TYPE_PATH_REGEX.matcher(href);
        if (!matcher.matches()) {
            throw new NotFoundException("video of unknown type");
        }
        int typeId = Integer.parseInt(matcher.group("id"));
        XlcType type = EnumUtilExt.valueOf(XlcType.class, typeId, (i, e) -> i == e.getId());
        if (type.isMovie()) {
            return Pair.of(type, ResourceState.FINISHED);
        }
        Element cont = pLeft.selectFirst(".moviecont");
        for (Element strong : cont.select(CssSelectors.TAG_STRONG)) {
            if ("状态：".equals(strong.text())) {
                String text = ((TextNode)strong.nextSibling()).text();
                ResourceState state;
                if (text.contains("全")) {
                    state = ResourceState.FINISHED;
                } else if (text.contains("至")) {
                    state = ResourceState.UPDATING;
                } else {
                    state = ResourceState.UNKNOWN;
                }
                return Pair.of(type, state);
            }
        }
        throw new UnexpectedContentException("Can't find state");
    }

    private static class Lazy {

        private static final Pattern ITEM_HREF_REGEX = Pattern.compile("/vod-read-id-(?<id>\\d+)\\.html");
        private static final Pattern TYPE_PATH_REGEX;

        static {
            String types = Arrays.stream(XlcType.values()).map(XlcType::getId).map(String::valueOf)
                .collect(Collectors.joining("|"));
            TYPE_PATH_REGEX = Pattern.compile("/vod-show-id-(?<id>" + types + ")\\.html");
        }
    }
}
