package cn.wsg.repository.com.douban;

import cn.wsg.commons.data.common.video.MovieGenre;
import cn.wsg.commons.internet.AbstractLoggableSiteClient;
import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.page.Page;
import cn.wsg.commons.internet.page.PageIndex;
import cn.wsg.commons.internet.support.*;
import cn.wsg.commons.jackson.EnumDeserializers;
import cn.wsg.commons.util.AssertUtils;
import cn.wsg.commons.util.EnumUtilExt;
import cn.wsg.commons.util.RegExUtilsExt;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Kingen
 */
@Slf4j
public class DoubanRepositoryImpl extends AbstractLoggableSiteClient<Long> implements DoubanRepository {

    public DoubanRepositoryImpl() {
        super("豆瓣", HttpHost.create("https://douban.com"), SiteHelper.defaultClient(), SiteHelper.defaultContext());
    }

    @Override
    public void login(String username, String password) throws LoginException {
        this.logout();
        RequestBuilder builder =
            this.create("accounts", BaseSiteClient.METHOD_POST, "/j/mobile/login/basic").addParameter("ck", "")
                .addParameter("remember", String.valueOf(true)).addParameter("name", username)
                .addParameter("password", password);
        LoginResult result;
        try {
            result = this.getObject(builder, Lazy.MAPPER, LoginResult.class);
        } catch (NotFoundException e) {
            throw new UnexpectedException(e);
        }
        // todo handle captcha
        if (!result.isSuccess()) {
            throw new LoginException(result.getMessage());
        }
    }

    @Override
    public Long user() {
        Cookie cookie = this.getCookie("dbcl2");
        if (cookie == null) {
            return null;
        }
        Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.COOKIE_DBCL2_REGEX, cookie.getValue());
        return Long.parseLong(matcher.group("id"));
    }

    @Override
    public void logout() {
        if (this.user() == null) {
            return;
        }
        this.findDocument(this.httpGet(""));
        RequestBuilder builder = this.httpGet("/accounts/logout").addParameter("source", "main")
            .addParameter("ck", Objects.requireNonNull(this.getCookie("ck")).getValue());
        this.findDocument(builder);
    }

    @Override
    public Page<SubjectIndex> searchGlobally(String keyword, PageIndex page, DoubanCatalog catalog)
        throws SearchException {
        AssertUtils.requireNotBlank(keyword);
        RequestBuilder builder = httpGet("/j/search").addParameter("q", keyword)
            .addParameter("start", String.valueOf(page.getCurrent() * 20));
        if (catalog != null) {
            builder.addParameter("cat", String.valueOf(catalog.getIntCode()));
        }
        SearchResult result;
        try {
            result = this.getObject(builder, Lazy.MAPPER, SearchResult.class);
        } catch (NotFoundException e) {
            throw new UnexpectedException(e);
        }
        if (result.getMsg() != null) {
            throw new SearchException(result.getMsg());
        }
        List<SubjectIndex> indices = new ArrayList<>(result.getItems().size());
        for (String item : result.getItems()) {
            Element nbg = Jsoup.parse(item).body().selectFirst(".nbg");
            String href = nbg.attr(CssSelectors.ATTR_HREF);
            Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.SUBJECT_LINK_REGEX, href);
            String url = URLDecoder.decode(matcher.group("u"), StandardCharsets.UTF_8);
            Matcher urlMatcher = Lazy.SUBJECT_URL_REGEX.matcher(url);
            if (!urlMatcher.matches()) {
                log.info("Not a subject: {}", url);
                continue;
            }
            long id = Long.parseLong(urlMatcher.group("id"));
            int code = Integer.parseInt(matcher.group("c"));
            DoubanCatalog cat = EnumUtilExt.valueOfIntCode(DoubanCatalog.class, code);
            indices.add(new SubjectIndex(id, cat, nbg.attr(CssSelectors.ATTR_TITLE)));
        }
        return Page.amountCountable(indices, page, 20, result.getTotal());
    }

    @Override
    public List<SubjectIndex> search(DoubanCatalog catalog, String keyword) {
        AssertUtils.requireNotBlank(keyword);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Page<RankedSubject> top250(PageIndex pageIndex) {
        Document document = findDocument(
            request(DoubanCatalog.MOVIE, "/top250").addParameter("start", String.valueOf(pageIndex.getCurrent() * 25)));
        Elements items = document.selectFirst(".article").select(".item");
        List<RankedSubject> subjects = new ArrayList<>(items.size());
        for (Element item : items) {
            Element pic = item.selectFirst(".pic");
            int rank = Integer.parseInt(pic.child(0).text());
            String id =
                RegExUtilsExt.matchesOrElseThrow(Lazy.SUBJECT_URL_REGEX, pic.child(1).attr(CssSelectors.ATTR_HREF))
                    .group("id");
            String name = pic.selectFirst(CssSelectors.TAG_IMG).attr(CssSelectors.ATTR_ALT);
            subjects.add(new RankedSubject(Long.parseLong(id), DoubanCatalog.MOVIE, name, rank));
        }
        return Page.amountCountable(subjects, pageIndex, 25, 250);
    }

    @Override
    public Page<MarkedSubject> findUserSubjects(DoubanCatalog catalog, long userId, MarkedStatus status, PageIndex page)
        throws NotFoundException {
        RequestBuilder builder = request(catalog, "/people/%d/%s", userId, status.name().toLowerCase());
        builder.addParameter("start", String.valueOf(page.getCurrent() * 30));
        builder.addParameter("sort", "time");
        builder.addParameter("rating", "all");
        builder.addParameter("filter", "all");
        builder.addParameter("mode", "list");

        Document document = this.getDocument(builder);
        Elements lis = document.selectFirst(".list-view").select(".item");
        List<MarkedSubject> subjects = new ArrayList<>(lis.size());
        for (Element li : lis) {
            long id = Long.parseLong(li.id().substring(4));
            String title = li.selectFirst(CssSelectors.TAG_A).text().split("/")[0].strip();
            LocalDate markedDate = LocalDate.parse(li.selectFirst(".date").text());
            subjects.add(new MarkedSubject(id, catalog, title, markedDate));
        }
        String numStr = document.selectFirst(".subject-num").text();
        int total = Integer.parseInt(numStr.split("/")[1].strip());
        return Page.amountCountable(subjects, page, 30, total);
    }

    @Override
    public Page<PersonIndex> findUserCreators(DoubanCatalog catalog, long userId, PageIndex page)
        throws NotFoundException {
        RequestBuilder builder = request(catalog, "/people/%d/%s", userId, catalog.getPersonPlurality());
        builder.addParameter("start", String.valueOf(page.getCurrent() * 15));
        Document document = this.getDocument(builder);
        Elements items = document.select(".item");
        List<PersonIndex> indices = new ArrayList<>(items.size());
        for (Element item : items) {
            Element a = item.selectFirst(CssSelectors.TAG_A);
            String href = a.attr(CssSelectors.ATTR_HREF);
            Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.CREATOR_URL_REGEX, href);
            long id = Long.parseLong(matcher.group("id"));
            indices.add(new PersonIndex(id, catalog, a.attr(CssSelectors.ATTR_TITLE)));
        }
        String title = document.title();
        Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.CREATORS_PAGE_TITLE_REGEX, title);
        long total = Long.parseLong(matcher.group("t"));
        return Page.amountCountable(indices, page, 15, total);
    }

    @Override
    public DoubanVideo findVideoById(long id) throws NotFoundException {
        return this.getSubject(DoubanVideo.class, DoubanCatalog.MOVIE, id, (video, document) -> {
            String title = document.title();
            String zhTitle = title.substring(0, title.length() - 5);
            video.setZhTitle(zhTitle);

            String name = video.getName().replace("  ", " ");
            AssertUtils.require(name, s -> s.startsWith(zhTitle), "Name and zhTitle are not matched.");
            if (name.length() > zhTitle.length()) {
                video.setOriginalTitle(name.substring(zhTitle.length()).strip());
            } else {
                video.setOriginalTitle(zhTitle);
            }

            Map<String, Element> metadata = this.getMetadata(document);
            Element roo = metadata.get("制片国家/地区:");
            if (video.getCountriesOfOrigin() == null && null != roo) {
                String[] values = StringUtils.split(((TextNode)roo.nextSibling()).text(), "/");
                video.setCountriesOfOrigin(
                    Arrays.stream(values).map(s -> CountryMapping.of(s.strip())).collect(Collectors.toList()));
            }
            Element lang = metadata.get("语言:");
            if (video.getLanguages() == null && null != lang) {
                String[] values = StringUtils.split(((TextNode)lang.nextSibling()).text(), "/");
                video.setLanguages(
                    Arrays.stream(values).map(s -> LanguageMapping.of(s.strip())).collect(Collectors.toList()));
            }
            Element imdb = metadata.get("IMDb:");
            if (null != imdb) {
                video.setImdbId(((TextNode)imdb.nextSibling()).text().strip());
            }
            Element aka = metadata.get("又名:");
            if (video.getAlternateNames() == null && null != aka) {
                String[] values = StringUtils.split(((TextNode)aka.nextSibling()).text(), "/");
                video.setAlternateNames(Arrays.stream(values).map(String::strip).collect(Collectors.toList()));
            }

            if (video instanceof DoubanTVSeries) {
                DoubanTVSeries series = (DoubanTVSeries)video;
                Element episodes = metadata.get("集数:");
                if (null != episodes) {
                    try {
                        series.setNumberOfEpisodes(Integer.parseInt(((TextNode)episodes.nextSibling()).text().strip()));
                    } catch (NumberFormatException e) {
                        log.warn("Can't get count of episodes: {}", e.getMessage());
                    }
                }
            }
            return video;
        });
    }

    @Override
    public DoubanBook findBookById(long id) throws NotFoundException {
        return this.getSubject(DoubanBook.class, DoubanCatalog.BOOK, id, (book, document) -> book);
    }

    @Override
    public long getDbIdByImdbId(String imdbId) throws NotFoundException, LoginException {
        if (this.user() == null) {
            throw new LoginException("Please log in first.");
        }
        AssertUtils.requireNotBlank(imdbId);
        RequestBuilder builder = this.create("movie", BaseSiteClient.METHOD_POST, "/new_subject")
            .addParameter("ck", Objects.requireNonNull(this.getCookie("ck")).getValue()).addParameter("type", "0")
            .addParameter("p_title", imdbId).addParameter("p_uid", imdbId)
            .addParameter("cat", String.valueOf(DoubanCatalog.MOVIE.getIntCode()))
            .addParameter("subject_submit", "下一步");
        Document document = this.findDocument(builder);
        Element fieldset = document.selectFirst("div#content").selectFirst(CssSelectors.TAG_FIELDSET);
        Element input = fieldset.selectFirst("input#p_uid");
        if (input == null) {
            throw new NotFoundException("");
        }
        Element span = input.nextElementSibling();
        Element ref = span.nextElementSibling();
        if (ref == null) {
            throw new NotFoundException(span.text());
        }
        String href = ref.attr(CssSelectors.ATTR_HREF);
        Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.SUBJECT_URL_REGEX, href);
        return Long.parseLong(matcher.group("id"));
    }

    private Map<String, Element> getMetadata(Document document) {
        Elements pls = document.selectFirst("#info").select(".pl");
        return pls.stream().collect(Collectors.toMap(Element::text, Function.identity()));
    }

    private <T> T getSubject(Class<T> clazz, DoubanCatalog catalog, long id, BiFunction<T, Document, T> decorator)
        throws NotFoundException {
        RequestBuilder builder = request(catalog, "/subject/%d/", id);
        Document document = this.getDocument(builder);
        String html = document.selectFirst("script[type=application/ld+json]").html();
        html = StringUtils.replaceChars(html, "\n\t", "");
        try {
            return decorator.apply(Lazy.MAPPER.readValue(html, clazz), document);
        } catch (JsonProcessingException e) {
            throw new UnexpectedException(e);
        }
    }

    private RequestBuilder request(DoubanCatalog catalog, String path, Object... args) {
        if (catalog == null) {
            return httpGet(path, args);
        }
        return this.create(catalog.name().toLowerCase(), BaseSiteClient.METHOD_GET, path, args);
    }

    private static final class Lazy {

        private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new SimpleModule()
            .addDeserializer(MovieGenre.class,
                EnumDeserializers.match(MovieGenre.class, (s, e) -> Objects.equals(s, e.getZhTitle()))))
            .registerModule(new JavaTimeModule());

        private static final Pattern COOKIE_DBCL2_REGEX = Pattern.compile("\"(?<id>\\d+):[0-9A-Za-z+/]+\"");

        private static final Pattern SUBJECT_LINK_REGEX = Pattern.compile(
            "https://www\\.douban\\.com/link2/\\?url=(?<u>[\\w%.-]+)"
                + "&query=(?<q>[\\w%-]+)&cat_id=(?<c>\\d+)&type=search&pos=(?<p>\\d+)");
        private static final Pattern CREATORS_PAGE_TITLE_REGEX = Pattern.compile("[^()\\s]+\\((?<t>\\d+)\\)");

        private static final Pattern SUBJECT_URL_REGEX;
        private static final Pattern CREATOR_URL_REGEX;

        static {
            String catalogs = Arrays.stream(DoubanCatalog.values()).map(DoubanCatalog::name).map(String::toLowerCase)
                .collect(Collectors.joining("|"));
            SUBJECT_URL_REGEX = Pattern.compile("https://(" + catalogs + ")\\.douban\\.com/subject/(?<id>\\d+)/");
            String creators =
                Arrays.stream(DoubanCatalog.values()).map(DoubanCatalog::getPerson).collect(Collectors.joining("|"));
            CREATOR_URL_REGEX =
                Pattern.compile("https://(" + catalogs + ")\\.douban\\.com/(" + creators + ")/(?<id>\\d+)/");
        }
    }

    @Getter
    private static class LoginResult {

        @JsonProperty("status")
        private String status;
        @JsonProperty("message")
        private String message;
        @JsonProperty("description")
        private String description;
        @JsonProperty("payload")
        private Payload payload;

        public boolean isSuccess() {
            return "success".equals(status);
        }

        @Getter
        private static class Payload {

            @JsonProperty("account_info")
            private String account;

            /**
             * When graph validate code is required.
             */
            @JsonProperty("tc_app_id")
            private Long tcAppId;
            @JsonProperty("captcha_signature_sample")
            private String captchaSignatureSample;
            @JsonProperty("touch_cap_url")
            private String touchCapUrl;
            @JsonProperty("captcha_id")
            private String captchaId;
            @JsonProperty("captcha_image_url")
            private String captchaImageUrl;
        }
    }

    @Getter
    private static class SearchResult {

        @JsonProperty("items")
        private List<String> items;

        @JsonProperty("total")
        private int total;

        @JsonProperty("limit")
        private int limit;

        @JsonProperty("more")
        private boolean more;

        @JsonProperty("msg")
        private String msg;
    }
}
