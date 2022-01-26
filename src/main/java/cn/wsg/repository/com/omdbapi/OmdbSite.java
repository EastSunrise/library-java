package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.data.common.Country;
import cn.wsg.commons.data.common.Language;
import cn.wsg.commons.data.common.video.MovieGenre;
import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.page.Page;
import cn.wsg.commons.internet.page.PageIndex;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.internet.support.SiteHelper;
import cn.wsg.commons.internet.support.UnexpectedException;
import cn.wsg.commons.jackson.EnumDeserializers;
import cn.wsg.commons.util.AssertUtils;
import cn.wsg.commons.util.EnumUtilExt;
import cn.wsg.repository.com.imdb.ImdbRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Kingen
 */
public final class OmdbSite extends BaseSiteClient implements OmdbApi {

    private static final String NOT_FOUND_MSG = "not found!";

    private final String apikey;

    public OmdbSite(String apikey) {
        super("The Open Movie Database", HttpHost.create("https://www.omdbapi.com"));
        this.apikey = AssertUtils.requireNotBlank(apikey);
    }

    @Override
    public String getApikey() {
        return apikey;
    }

    @Override
    public OmdbVideo findById(String imdbId, OmdbOptionalReq optionalReq) throws NotFoundException {
        RequestBuilder builder = httpGet("/").addParameter("i", ImdbRepository.checkTitleId(imdbId));
        addOptionalReq(builder, optionalReq);
        builder.addParameter("plot", "full").addParameter("r", "json");
        return getObject(builder, OmdbVideo.class);
    }

    @Override
    public Page<OmdbVideo> searchByTitle(String title, PageIndex pageIndex, OmdbOptionalReq optionalReq)
        throws NotFoundException {
        AssertUtils.requireNotBlank(title);
        RequestBuilder builder = httpGet("/").addParameter("s", title);
        addOptionalReq(builder, optionalReq);
        builder.addParameter("r", "json");
        builder.addParameter("page", String.valueOf(pageIndex.getCurrent() + 1));
        OmdbSearchResult result = getObject(builder, OmdbSearchResult.class);
        return Page.amountCountable(result.items, pageIndex, 10, result.total);
    }

    @Override
    public OmdbSeason findSeason(String seriesId, int season) throws NotFoundException {
        AssertUtils.requireRange(season, 1, null);
        RequestBuilder builder = httpGet("/").addParameter("i", ImdbRepository.checkTitleId(seriesId));
        return getObject(builder.addParameter("season", String.valueOf(season)), OmdbSeason.class);
    }

    @Override
    public OmdbEpisode findEpisode(String seriesId, int season, int episode) throws NotFoundException {
        AssertUtils.requireRange(season, 1, null);
        AssertUtils.requireRange(episode, 1, null);
        RequestBuilder builder = httpGet("/").addParameter("i", ImdbRepository.checkTitleId(seriesId));
        builder.addParameter("season", String.valueOf(season)).addParameter("episode", String.valueOf(episode));
        return getObject(builder, OmdbEpisode.class);
    }

    @Override
    public String getContent(RequestBuilder builder) throws IOException {
        return super.getContent(builder).replace("\"N/A\"", "null");
    }

    private <T extends OmdbResponse> T getObject(RequestBuilder builder, Class<T> clazz) throws NotFoundException {
        builder.addParameter("apikey", apikey);
        String content;
        try {
            content = getContent(builder);
        } catch (IOException e) {
            throw SiteHelper.handleException(e);
        }
        T t;
        try {
            t = Lazy.MAPPER.readValue(content, clazz);
        } catch (InvalidTypeIdException e) {
            OmdbResponse response;
            try {
                response = Lazy.MAPPER.readValue(content, OmdbResponse.class);
            } catch (JsonProcessingException ex) {
                throw SiteHelper.handleException(e);
            }
            AssertUtils.requireEquals(response.isSuccess(), false);
            String error = response.getError();
            if (error.endsWith(NOT_FOUND_MSG)) {
                throw new NotFoundException(error);
            }
            throw new UnexpectedException(new HttpResponseException(HttpStatus.SC_INTERNAL_SERVER_ERROR, error));
        } catch (JsonProcessingException e) {
            throw SiteHelper.handleException(e);
        }
        if (!t.isSuccess()) {
            String error = t.getError();
            if (error.endsWith(NOT_FOUND_MSG)) {
                throw new NotFoundException(error);
            }
            throw new UnexpectedException(new HttpResponseException(HttpStatus.SC_INTERNAL_SERVER_ERROR, error));
        }
        return t;
    }

    private void addOptionalReq(RequestBuilder builder, OmdbOptionalReq optionalReq) {
        if (optionalReq != null) {
            if (optionalReq.getType() != null) {
                builder.addParameter("type", optionalReq.getType().name().toLowerCase());
            }
            if (optionalReq.getYear() != null) {
                builder.addParameter("y", String.valueOf(optionalReq.getYear()));
            }
        }
    }

    private static class Lazy {

        private static final ObjectMapper MAPPER =
            new ObjectMapper().registerModule(new JavaTimeModule()).setLocale(Locale.ENGLISH).registerModule(
                new SimpleModule().addDeserializer(MovieGenre.class,
                    EnumDeserializers.match(MovieGenre.class, (s, e) -> Objects.equals(s, e.getEnTitle())))
                    .addDeserializer(Language.class,
                        EnumDeserializers.match(Language.class, (s, e) -> ArrayUtils.contains(e.getEnNames(), s)))
                    .addDeserializer(Country.class, new FromStringDeserializer<>(Country.class) {
                        @Override
                        protected Country _deserialize(String value, DeserializationContext ctxt) {
                            try {
                                return EnumUtilExt.valueOf(CountryMapping.class, value, (s, e) -> e.match(s)).getEnum();
                            } catch (Exception ex) {
                                return EnumUtilExt
                                    .valueOf(Country.class, value, (s, e) -> Objects.equals(s, e.getEnShortName()));
                            }
                        }
                    }).addDeserializer(OmdbVideo.RatingSource.class,
                    EnumDeserializers.match(OmdbVideo.RatingSource.class, (s, e) -> Objects.equals(s, e.getName()))))
                .addHandler(new OmdbDeserializationProblemHandler());
    }

    @Getter
    private static class OmdbSearchResult extends OmdbResponse {

        @JsonProperty("Search")
        private List<OmdbVideo> items;
        @JsonProperty("totalResults")
        private Integer total;
    }
}
