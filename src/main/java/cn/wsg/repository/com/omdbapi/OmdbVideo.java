package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.data.common.Country;
import cn.wsg.commons.data.common.Language;
import cn.wsg.commons.data.common.video.MovieGenre;
import cn.wsg.commons.data.schema.item.CreativeWork;
import cn.wsg.commons.jackson.JsonJoinedValue;
import cn.wsg.commons.jackson.JsonUnknownType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Kingen
 */
@Getter
@ToString
@JsonSubTypes({
    @JsonSubTypes.Type(value = OmdbMovie.class, name = "movie"),
    @JsonSubTypes.Type(value = OmdbSeries.class, name = "series"),
    @JsonSubTypes.Type(value = OmdbEpisode.class, name = "episode")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "Type")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class OmdbVideo extends OmdbResponse implements CreativeWork {

    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Rated")
    private String rated;

    @JsonProperty("Released")
    @JsonFormat(pattern = "dd MMM yyyy")
    private LocalDate release;

    @JsonProperty("Runtime")
    private String runtime;

    @JsonJoinedValue
    @JsonProperty("Genre")
    private List<MovieGenre> genres;

    @JsonJoinedValue
    @JsonProperty("Director")
    private List<String> directors;

    @JsonJoinedValue
    @JsonProperty("Writer")
    private List<String> writers;

    @JsonJoinedValue
    @JsonProperty("Actors")
    private List<String> actors;

    @JsonProperty("Plot")
    private String plot;

    @JsonJoinedValue
    @JsonProperty("Language")
    private List<Language> languages;

    @JsonProperty("Country")
    private Country country;

    @JsonProperty("Awards")
    private String awards;

    @JsonProperty("Poster")
    private URL image;

    @JsonProperty("Ratings")
    private List<Rating> ratings;

    @JsonProperty("Metascore")
    private JsonUnknownType metaScore;

    @JsonProperty("imdbRating")
    private Double imdbRating;

    @JsonProperty("imdbVotes")
    private Integer imdbVotes;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static class Rating {

        @JsonProperty("Source")
        private RatingSource source;

        @JsonProperty("Value")
        private String value;
    }

    public enum RatingSource {
        /**
         * @see <a href="https://www.imdb.com/">IMDb</a>
         */
        IMDB("Internet Movie Database"),
        /**
         * @see <a href="https://www.rottentomatoes.com/">Rotten Tomatoes</a>
         */
        ROTTEN_TOMATOES("Rotten Tomatoes"),
        /**
         * @see <a href="https://www.metacritic.com/">IMDb</a>
         */
        META_CRITIC("Metacritic");

        private final String name;

        RatingSource(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
