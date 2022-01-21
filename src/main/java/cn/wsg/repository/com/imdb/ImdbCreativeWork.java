package cn.wsg.repository.com.imdb;

import cn.wsg.commons.Language;
import cn.wsg.commons.Region;
import cn.wsg.commons.internet.common.video.AggregateRating;
import cn.wsg.commons.internet.common.video.MovieGenre;
import cn.wsg.commons.internet.org.schema.item.CreativeWork;
import cn.wsg.commons.jackson.JsonJoinedValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Kingen
 * @see <a href="https://developer.imdb.com/documentation/data-dictionary/titles">Data Dictionary - Title</a>
 */
@Getter
@JsonSubTypes({
    @JsonSubTypes.Type(value = ImdbMovie.class, name = "Movie"),
    @JsonSubTypes.Type(value = ImdbTVSeries.class, name = "TVSeries"),
    @JsonSubTypes.Type(value = ImdbEpisode.class, name = "TVEpisode")
})
@JsonIgnoreProperties("@context")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ImdbCreativeWork extends ImdbThing implements CreativeWork {

    @JsonProperty("aggregateRating")
    private AggregateRating aggregateRating;

    @JsonProperty("contentRating")
    private String contentRating;

    @JsonProperty("genre")
    private List<MovieGenre> genres;

    @JsonProperty("datePublished")
    private LocalDate datePublished;

    @JsonJoinedValue
    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("creator")
    private List<ImdbCreator> creators;

    @JsonProperty("timeRequired")
    private Duration timeRequired;

    @JsonProperty("review")
    private ImdbReview review;

    @JsonProperty("author")
    private ImdbCreator author;

    @JsonProperty("dateCreated")
    private LocalDate dateCreated;

    @Setter(AccessLevel.PACKAGE)
    @JsonProperty("countryOfOrigin")
    private List<Region> countriesOfOrigin;

    @Setter(AccessLevel.PACKAGE)
    @JsonProperty("inLanguage")
    private List<Language> languages;

    @JsonProperty("thumbnailUrl")
    private URL thumbnailUrl;
}
