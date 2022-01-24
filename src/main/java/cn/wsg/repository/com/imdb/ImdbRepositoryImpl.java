package cn.wsg.repository.com.imdb;

import cn.wsg.commons.data.common.Language;
import cn.wsg.commons.data.common.Region;
import cn.wsg.commons.data.common.video.MovieGenre;
import cn.wsg.commons.data.common.video.ReleaseDate;
import cn.wsg.commons.data.common.video.ReleaseInfo;
import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.support.*;
import cn.wsg.commons.internet.util.URIUtil;
import cn.wsg.commons.jackson.EnumDeserializers;
import cn.wsg.commons.util.RegExUtilsExt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.RequestBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Nonnull;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kingen
 */
public class ImdbRepositoryImpl extends BaseSiteClient implements ImdbRepository {

    public ImdbRepositoryImpl() {
        super("IMDb", HttpHost.create("https://www.imdb.com"), SiteHelper.defaultClient(), SiteHelper.defaultContext());
    }

    @Override
    public List<String> top250() {
        return getRankedList("/chart/top/");
    }

    @Override
    public List<String> top250TV() {
        return getRankedList("/chart/toptv/");
    }

    @Override
    public List<String> mostPopular() {
        return getRankedList("/chart/moviemeter/");
    }

    @Nonnull
    @Override
    public ImdbCreativeWork findTitleById(@Nonnull String titleId) throws NotFoundException {
        ImdbRepository.checkTitleId(titleId);
        return getObject(ImdbCreativeWork.class, "title", titleId, (title, document) -> {
            Element details = document.selectFirst("[data-testid=title-details-section]");
            if (title.getCountriesOfOrigin() == null) {
                title.setCountriesOfOrigin(getDetails(details, "origin", "country_of_origin", this::parseCountry));
            }
            if (title.getLanguages() == null) {
                title.setLanguages(getDetails(details, "languages", "primary_language", this::parseLanguage));
            }

            if (title instanceof ImdbEpisode) {
                String href = document.selectFirst("[data-testid=hero-subnav-bar-all-episodes-button]")
                    .attr(CssSelectors.ATTR_HREF);
                ((ImdbEpisode)title).setSeriesTitleId((RegExUtilsExt.findOrElseThrow(TITLE_ID_REGEX, href)).group());
                String[] parts =
                    document.selectFirst("[data-testid=hero-subnav-bar-season-episode-numbers-section-xs]").text()
                        .split("\\.");
                int season = Integer.parseInt(parts[0].substring(1));
                int episode = Integer.parseInt(parts[1].substring(1));
                ((ImdbEpisode)title).setEpisodeNumber(new EpisodeNumber(season, episode));
            }

            return title;
        });
    }

    @Override
    public ReleaseInfo findReleaseInfo(String titleId) throws NotFoundException {
        Document document = getDocument(httpGet("/title/%s/releaseinfo", ImdbRepository.checkTitleId(titleId)));
        String enTitle = document.selectFirst("[itemprop=name]").selectFirst(CssSelectors.TAG_A).text();
        Elements releases = document.selectFirst("#releases").nextElementSibling().select(CssSelectors.TAG_TR);
        List<ReleaseDate> releaseDates = new ArrayList<>(releases.size());
        for (Element release : releases) {
            Region country =
                parseCountry(URIUtil.getQueryValue(release.child(0).child(0).attr(CssSelectors.ATTR_HREF), "region"));
            String attribute = release.child(2).hasText() ? StringUtils.strip(release.child(2).text(), "()") : null;
            releaseDates.add(ReleaseDate.parse(country, release.child(1).text(), Lazy.DTF_RELEASE, attribute));
        }
        Elements akas = document.selectFirst("#akas").nextElementSibling().select(CssSelectors.TAG_TR);
        List<String> alias = akas.stream().map(ele -> ele.child(1)).map(Element::text).collect(Collectors.toList());
        return new ReleaseInfo(enTitle, releaseDates, alias);
    }

    @Override
    public String[] findEpisodesOfSeries(String seriesTitleId, int season) throws NotFoundException {
        RequestBuilder builder = httpGet("/title/%s/episodes", ImdbRepository.checkTitleId(seriesTitleId));
        Document document = getDocument(builder.addParameter("season", String.valueOf(season)));
        String title = document.title();
        if (title.endsWith(Lazy.EPISODES_PAGE_TITLE_SUFFIX)) {
            throw new UnexpectedContentException(title);
        }
        Matcher matcher = RegExUtilsExt.matchesOrElseThrow(Lazy.EPISODES_BY_SEASON_TITLE_REGEX, title);
        if (Integer.parseInt(matcher.group("s")) != season) {
            throw new NotFoundException("The TV series has no season " + season);
        }
        Element element = document.selectFirst("meta[itemprop=numberofEpisodes]");
        int episodesCount = Integer.parseInt(element.attr(CssSelectors.ATTR_CONTENT));
        Elements divs = document.select("div[itemprop=episodes]");
        Map<Integer, String> index2Id = new HashMap<>(episodesCount);
        for (int i = divs.size() - 1; i >= 0; i--) {
            Element div = divs.get(i);
            String href =
                div.selectFirst(CssSelectors.TAG_STRONG).selectFirst(CssSelectors.TAG_A).attr(CssSelectors.ATTR_HREF)
                    .split("\\?")[0];
            String id = RegExUtilsExt.matchesOrElseThrow(Lazy.TITLE_HREF_REGEX, href).group("id");
            String number = div.selectFirst("meta[itemprop=episodeNumber]").attr(CssSelectors.ATTR_CONTENT);
            int episode = Integer.parseInt(number);
            if (null != index2Id.put(episode, id)) {
                throw new UnexpectedContentException(
                    String.format("Conflict episodes of season %d of series %s", season, seriesTitleId));
            }
        }
        if (index2Id.isEmpty()) {
            return new String[0];
        }
        String[] episodes = new String[Collections.max(index2Id.keySet()) + 1];
        index2Id.forEach((key, value) -> episodes[key] = value);
        return episodes;
    }

    @Override
    public ImdbPerson findPersonById(String nameId) throws NotFoundException {
        return getObject(ImdbPerson.class, "name", ImdbRepository.checkNameId(nameId), (person, doc) -> person);
    }

    private List<String> getRankedList(String path) {
        Document document = findDocument(httpGet(path));
        Elements as = document.select("td.titleColumn").select(CssSelectors.TAG_A);
        List<String> ids = new ArrayList<>(as.size());
        for (Element a : as) {
            Matcher matcher = RegExUtilsExt.lookingAtOrElseThrow(Lazy.TITLE_HREF_REGEX, a.attr(CssSelectors.ATTR_HREF));
            ids.add(matcher.group("id"));
        }
        return ids;
    }

    private Region parseCountry(String value) {
        value = value.toUpperCase();
        try {
            return Region.valueOf(value);
        } catch (IllegalArgumentException e) {
            return RegionMapping.valueOf(value).get();
        }
    }

    private Language parseLanguage(String value) {
        if (value.length() == 2) {
            value = value.toLowerCase();
            for (Language language : Language.values()) {
                if (value.equals(language.getPart1())) {
                    return language;
                }
            }
        } else if (value.length() == 3) {
            value = value.toUpperCase();
            if (Language.isLocalUsed(value)) {
                return LanguageMapping.valueOf(value).get();
            }
            try {
                return Language.valueOf(value);
            } catch (IllegalArgumentException e) {
                return LanguageMapping.valueOf(value).get();
            }
        }
        throw new IllegalArgumentException("No language constant: " + value);
    }

    private <E extends Enum<E>> List<E> getDetails(Element section, String testId, String query,
        Function<String, E> parse) {
        Element div = section.selectFirst("[data-testid=title-details-" + testId + "]");
        if (div == null) {
            return null;
        }
        Stream<String> stream =
            div.child(1).select(CssSelectors.TAG_A).stream().map(a -> a.attr(CssSelectors.ATTR_HREF));
        return stream.map(href -> URIUtil.getQueryValue(href, query)).map(parse).collect(Collectors.toList());
    }

    private <T> T getObject(Class<T> clazz, String type, String id, BiFunction<T, Document, T> decorator)
        throws NotFoundException {
        Document document = getDocument(httpGet("/%s/%s/", type, id));
        try {
            String html = document.selectFirst("script[type=application/ld+json]").html();
            return decorator.apply(Lazy.MAPPER.readValue(html, clazz), document);
        } catch (JsonProcessingException e) {
            throw new UnexpectedException(e);
        }
    }

    private static final class Lazy {

        private static final ObjectMapper MAPPER =
            new ObjectMapper().setLocale(Locale.ENGLISH).enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .registerModule(new SimpleModule().addDeserializer(MovieGenre.class,
                    EnumDeserializers.match(MovieGenre.class, (s, e) -> Objects.equals(s, e.getEnTitle()))))
                .registerModule(new SimpleModule().addDeserializer(Language.class,
                    EnumDeserializers.match(Language.class, (s, e) -> ArrayUtils.contains(e.getEnNames(), s))))
                .registerModule(new JavaTimeModule());

        private static final DateTimeFormatter DTF_RELEASE =
            DateTimeFormatter.ofPattern("[[d ]MMMM ]yyyy").withLocale(Locale.ENGLISH);

        private static final String TEXT_REGEX_STR = "[ \"!#%&'()*+,-./0-9:>?A-Za-z·\u0080-ÿ]+";
        private static final String EPISODES_PAGE_TITLE_SUFFIX = "- Episodes - IMDb";

        private static final Pattern TITLE_HREF_REGEX = Pattern.compile("/title/(?<id>tt\\d{7,8})/");
        private static final Pattern EPISODES_BY_SEASON_TITLE_REGEX =
            Pattern.compile("(?<t>" + TEXT_REGEX_STR + ") - Season (?<s>\\d{1,2}) - IMDb");
    }
}
