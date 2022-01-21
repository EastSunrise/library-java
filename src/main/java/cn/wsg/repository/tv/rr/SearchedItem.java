package cn.wsg.repository.tv.rr;

import cn.wsg.commons.Region;
import cn.wsg.commons.internet.common.video.MovieGenre;
import cn.wsg.commons.internet.org.schema.item.CreativeWork;
import cn.wsg.commons.jackson.JsonJoinedValue;
import cn.wsg.commons.jackson.JsonUnknownType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

/**
 * A series on the site.
 *
 * @author Kingen
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SearchedItem implements CreativeWork {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("cover")
    private URL image;

    @JsonProperty("view_count")
    private Integer viewCount;

    @JsonProperty("brief")
    private String description;

    @JsonProperty("area")
    private Region region;

    @JsonProperty("year")
    private Integer year;

    @JsonJoinedValue
    @JsonProperty("cat")
    private List<MovieGenre> genres;

    @JsonProperty("score")
    private Double score;

    @JsonProperty("sort")
    private JsonUnknownType sort;

    @JsonProperty("upInfo")
    private JsonUnknownType upInfo;

    @JsonProperty("status")
    private JsonUnknownType status;

    @JsonProperty("classify")
    private String classify;

    @JsonProperty("director")
    @JsonJoinedValue(separator = "/")
    private List<String> director;

    @JsonProperty("actor")
    @JsonJoinedValue(separator = "/")
    private List<String> actors;

    @JsonProperty("search_after")
    private String searchAfter;

    @JsonProperty("highlights")
    private Highlight highlights;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static class Highlight {
        @JsonProperty("title")
        private String title;
    }
}
