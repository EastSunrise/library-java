package cn.wsg.repository.tw.x80s;

import cn.wsg.commons.data.download.DownloadLink;
import cn.wsg.commons.data.download.InvalidLinkException;
import cn.wsg.commons.data.download.LinkFactory;
import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.repository.ListRepository;
import cn.wsg.commons.internet.repository.support.Repositories;
import cn.wsg.commons.internet.support.CssSelectors;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.internet.util.DocumentUtils;
import cn.wsg.commons.util.NetUtils;
import cn.wsg.commons.util.RegExUtilsExt;
import cn.wsg.repository.resource.VideoResourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.apache.http.HttpHost;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Kingen
 * @see <a href="https://80s.tw/">80s Movies for PC</a>
 * @see <a href="https://m.80s.tw/">80s Movies for Mobile</a>
 */
@Slf4j
public class X80sSite extends BaseSiteClient implements VideoResourceRepository<X80sItem> {

    private static final int MIN_ID = 32;
    private static final Range<Integer> EXCEPTS = Range.between(1501, 3008);
    private static final Set<Integer> ERRORS = Set.of(7610, 12310);

    public X80sSite() {
        super("80s Movies", HttpHost.create("https://80s.tw"));
    }

    /**
     * Returns the repository of all items from 1 to {@link #latest()} <strong>except those in
     * {@link #EXCEPTS}</strong>.
     */
    @Override
    public ListRepository<Integer, X80sItem> getRepository() {
        IntStream stream =
            IntStream.rangeClosed(MIN_ID, latest()).filter(i -> !EXCEPTS.contains(i) && !ERRORS.contains(i));
        return Repositories.list(this, stream.boxed().collect(Collectors.toList()));
    }

    @Override
    public X80sItem findById(Integer id) throws NotFoundException {
        Document document = getDocument(httpGet("/movie/%d", id));
        Map<String, String> metadata = DocumentUtils.getMetadata(document);
        String title = Objects.requireNonNull(metadata.get("og:title"));
        Elements crumbs = document.selectFirst(".s_block1").select(CssSelectors.TAG_A);
        String typeHref = crumbs.get(1).attr(CssSelectors.ATTR_HREF);
        Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.TYPE_HREF_REGEX, typeHref);
        X80sType type = X80sType.valueOf(matcher.group("t").toUpperCase());
        URL url = NetUtils.createURL(httpGet("/%s/%d", type.name().toLowerCase(), id).getUri().toString());

        Elements lis = document.select("#cpage li");
        List<DownloadLink> links = new ArrayList<>();
        for (Element li : lis) {
            if (li.hasClass("dlselected")) {
                links.addAll(getLinks(document.selectFirst("#myform")));
                continue;
            }
            String tid = li.id();
            if (tid.contains("dl")) {
                String format = Lazy.FORMATS.get(tid.substring(4, 5));
                links.addAll(getLinks(getDocument(httpGet("/movie/%d/%s-%d", id, format, 1))));
            }
        }

        X80sItem item = new X80sItem(title, type, url, links);
        matcher = Lazy.YEAR_REGEX.matcher(((TextNode)document.selectFirst("h1.font14w").nextSibling()).text());
        if (matcher.find()) {
            item.setYear(Integer.parseInt(matcher.group()));
        }
        Elements spans = document.select("span.font_888");
        Map<String, Element> info = spans.stream().collect(Collectors.toMap(Element::text, e -> e));
        Element span;
        if ((span = info.get("更新日期：")) != null) {
            item.setUpdate(LocalDate.parse(((TextNode)span.nextSibling()).text().strip()));
        }
        if ((span = info.get("豆瓣评分：")) != null) {
            Element a = span.parent().nextElementSibling().selectFirst(CssSelectors.TAG_A);
            if (a != null) {
                RegExUtilsExt.ifFind(Lazy.DOUBAN_REGEX, a.attr(CssSelectors.ATTR_HREF),
                    m -> item.setDoubanId(Long.parseLong(m.group("id"))));
            }
        }
        return item;
    }

    private int latest() {
        Elements as = findDocument(httpGet("/top/last_update")).select(".tpul1 a");
        return as.stream().map(a -> a.attr(CssSelectors.ATTR_HREF))
            .map(href -> RegExUtilsExt.matchesOrElseThrow(Lazy.ITEM_HREF_REGEX, href)).map(m -> m.group("id"))
            .mapToInt(Integer::parseInt).max().orElseThrow();
    }

    private List<DownloadLink> getLinks(Element form) {
        Elements as = form.select(".dlname.nm").select(CssSelectors.TAG_A);
        List<DownloadLink> links = new ArrayList<>(as.size());
        for (Element a : as) {
            String description = a.text().strip();
            String href = a.attr(CssSelectors.ATTR_HREF);
            if (!"#".equals(href)) {
                try {
                    links.add(LinkFactory.parse(href, StandardCharsets.UTF_8, description));
                } catch (InvalidLinkException e) {
                    log.error("{}", e.getMessage());
                }
            }
        }
        return links;
    }

    private static class Lazy {
        private static final Pattern TYPE_HREF_REGEX;
        private static final Pattern ITEM_HREF_REGEX;
        private static final Pattern YEAR_REGEX = Pattern.compile("\\d{4}");
        private static final Pattern DOUBAN_REGEX = Pattern.compile("movie.douban.com/subject/(?<id>\\d+)");
        private static final Map<String, String> FORMATS =
            Map.of("2", "mp4", "3", "hd", "4", "bd", "5", "bt", "6", "jp");

        static {
            String types = Arrays.stream(X80sType.values()).map(Enum::name).map(String::toLowerCase)
                .collect(Collectors.joining("|"));
            TYPE_HREF_REGEX = Pattern.compile("/(?<t>" + types + ")/list");
            ITEM_HREF_REGEX = Pattern.compile("/(?<t>" + types + ")/(?<id>\\d+)");
        }
    }
}
