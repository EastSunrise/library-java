package cn.wsg.repository.com.bd2020;

import cn.wsg.commons.DatetimeConsts;
import cn.wsg.commons.WebConsts;
import cn.wsg.commons.internet.BaseSite;
import cn.wsg.commons.internet.common.video.resource.VideoResourceRepository;
import cn.wsg.commons.internet.download.DownloadLink;
import cn.wsg.commons.internet.download.InvalidLinkException;
import cn.wsg.commons.internet.download.LinkFactory;
import cn.wsg.commons.internet.repository.ListRepository;
import cn.wsg.commons.internet.repository.support.Repositories;
import cn.wsg.commons.internet.support.CssSelectors;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.internet.util.DocumentUtils;
import cn.wsg.commons.util.AssertUtils;
import cn.wsg.commons.util.NetUtils;
import cn.wsg.commons.util.RegExUtilsExt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpHost;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Kingen
 * @see <a href="https://www.bd2020.com/">BD Movies</a>
 */
@Slf4j
public final class BdMovieSite extends BaseSite implements VideoResourceRepository<BdMovieItem> {

    private static final int MIN_ID = 348;
    private static final int EXCEPT_ONE = 30508;

    public BdMovieSite() {
        super("BD-Movie", HttpHost.create("https://www.bd2020.com"));
    }

    /**
     * Constructs the repository of all items from 1 to {@link #latest()}. <strong>Almost 10% of the
     * items are not found</strong>.
     */
    @Override
    public ListRepository<Integer, BdMovieItem> getRepository() {
        IntStream stream = IntStream.rangeClosed(MIN_ID, this.latest()).filter(id -> id != EXCEPT_ONE);
        return Repositories.list(this, stream.boxed().collect(Collectors.toList()));
    }

    @Override
    public BdMovieItem findById(Integer id) throws NotFoundException {
        Document document = this.getDocument(this.httpGet("/zx/%d.htm", id));
        Map<String, String> metadata = DocumentUtils.getMetadata(document);
        String location = Objects.requireNonNull(metadata.get("og:url"));
        Matcher urlMatcher = Lazy.ITEM_URL_REGEX.matcher(location);
        if (!urlMatcher.matches()) {
            throw new NotFoundException("Not a movie page");
        }
        BdMovieType realType = BdMovieType.valueOf(urlMatcher.group("t").toUpperCase());
        String release = metadata.get("og:video:release_date");
        LocalDateTime updateTime = LocalDateTime.parse(release, DatetimeConsts.DTF_YYYY_MM_DD_HH_MM_SS);
        String title = metadata.get("og:title");

        String varUrls = this.getVarUrls(document);
        Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.VAR_REGEX, varUrls);
        List<DownloadLink> links = new ArrayList<>();
        String urls = this.decode(matcher.group("urls"));
        urls = StringEscapeUtils.unescapeHtml4(urls).replace("<p>", "").replace("</p>", "");
        for (String url : Lazy.URLS_SEPARATOR.split(urls)) {
            if (StringUtils.isNotBlank(url)) {
                try {
                    links.add(LinkFactory.parse(url, StandardCharsets.UTF_8, null));
                } catch (InvalidLinkException e) {
                    log.error("{}", e.getMessage());
                }
            }
        }
        String diskStr = this.decode(matcher.group("disk"));
        if (StringUtils.isNotBlank(diskStr)) {
            this.getDiskLinks(diskStr, links);
        }

        BdMovieItem item = new BdMovieItem(title, realType, NetUtils.createURL(location), links, updateTime);
        item.setNext(this.getNext(document));

        String cover = metadata.get("og:image");
        if (!cover.isEmpty()) {
            if (cover.startsWith(WebConsts.URL_PATH_SEPARATOR)) {
                cover = this.getHost().toURI() + cover;
            }
            item.setImage(NetUtils.createURL(cover));
        }
        Optional.ofNullable(matcher.group("db")).map(Long::parseLong).ifPresent(item::setDbId);
        item.setImdbId(matcher.group("imdb"));

        Element content = document.selectFirst("dl.content");
        if (content == null) {
            content = document.selectFirst("div.dfg-layout");
        }
        String text = content.text();
        if (item.getImdbId() == null) {
            Matcher matcher1 = Lazy.IMDB_INFO_REGEX.matcher(text);
            if (matcher1.find()) {
                item.setImdbId(matcher1.group("id"));
            }
        }
        return item;
    }

    /**
     * Returns the identifier of the latest item.
     *
     * @see <a href="https://www.bd2020.com/movies/index.htm">Last Update</a>
     */
    private int latest() {
        Document document = this.findDocument(this.httpGet("/movies/index.htm"));
        Elements lis = document.selectFirst("#content_list").select(".list-item");
        int latest = 1;
        for (Element li : lis) {
            String href = li.selectFirst(CssSelectors.TAG_A).attr(CssSelectors.ATTR_HREF);
            Matcher matcher = Lazy.ITEM_URL_REGEX.matcher(href);
            if (matcher.matches()) {
                latest = Math.max(latest, Integer.parseInt(matcher.group("id")));
            }
        }
        return latest;
    }

    private String getVarUrls(Document document) {
        for (Element script : document.body().select(CssSelectors.TAG_SCRIPT)) {
            String html = script.html().strip();
            if (html.startsWith("var urls")) {
                return html.split(";")[0];
            }
        }
        throw new NoSuchElementException("Can't get var urls");
    }

    private Integer getNext(Document document) {
        Element div = document.selectFirst(".dfg-neighbour");
        if (div == null) {
            return null;
        }
        Elements children = div.children();
        AssertUtils.requireEquals(children.size(), 2);
        Element next = children.get(0).selectFirst(CssSelectors.TAG_A);
        if (next == null) {
            return null;
        }
        String href = next.attr(CssSelectors.ATTR_HREF);
        Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.ITEM_URL_REGEX, href);
        return Integer.parseInt(matcher.group("id"));
    }

    private void getDiskLinks(String diskStr, List<DownloadLink> links) {
        String[] diskUrls = Lazy.DISK_URLS_SEPARATOR.split(diskStr);
        for (String diskUrl : diskUrls) {
            Matcher matcher = Lazy.DISK_RESOURCE_REGEX.matcher(diskUrl.strip());
            if (matcher.matches()) {
                String host = matcher.group("host");
                String url = WebConsts.URL_PREFIX_HTTPS + host + matcher.group("path");
                try {
                    links.add(LinkFactory.parse(url, StandardCharsets.UTF_8, null, () -> matcher.group("pwd")));
                } catch (InvalidLinkException e) {
                    log.error("{}", e.getMessage());
                }
            } else {
                log.error("Not a disk resource: {}", diskUrl);
            }
        }
    }

    private String decode(String urls) {
        urls = new StringBuilder(urls).reverse().toString();
        urls = new String(Base64.getDecoder().decode(urls), StandardCharsets.UTF_8);
        return new String(urls.getBytes(StandardCharsets.UTF_16), StandardCharsets.UTF_16);
    }

    private static class Lazy {

        private static final Pattern ITEM_URL_REGEX;
        private static final Pattern IMDB_INFO_REGEX = Pattern.compile("(title/? ?|((?i)imdb|Db).{0,4})(?<id>tt\\d+)");
        private static final Pattern VAR_REGEX = Pattern.compile(
            "var urls = \"(?<urls>[0-9A-Za-z+/=]*)\", " + "adsUrls = \"[0-9A-Za-z+/=]*\", "
                + "diskUrls = \"(?<disk>[0-9A-Za-z+/=]*)\", " + "scoreData = \"(?<imdb>tt\\d+)? ?###(?<db>\\d+)?\"");
        private static final Pattern DISK_RESOURCE_REGEX = Pattern.compile("(\\+链接: )?(?<pwd>[0-9A-Za-z]{4})?"
            + "(\\|\\|(https?|ttps| https|whttps|\\|https)|\\|?https|\\s+\\|\\|https)"
            + "://(?<host>www\\.yun\\.cn|pan\\.baidu\\.com|pan\\.xunlei\\.com)(?<path>/[\\w-./?=&]+)\\s*");
        private static final Pattern URLS_SEPARATOR = Pattern.compile("#{3,4}\r\n|#{3,4}");
        private static final Pattern DISK_URLS_SEPARATOR = Pattern.compile("###?\r\n|###|\r\n");

        static {
            String types = Arrays.stream(BdMovieType.values()).map(Enum::name).map(String::toLowerCase)
                .collect(Collectors.joining("|"));
            ITEM_URL_REGEX =
                Pattern.compile("https?://www\\.(bd2020|bd-film)\\.com/(?<t>" + types + ")/(?<id>\\d+)\\.htm");
        }
    }
}
