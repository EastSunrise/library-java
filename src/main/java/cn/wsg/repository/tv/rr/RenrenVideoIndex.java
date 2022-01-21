package cn.wsg.repository.tv.rr;

import cn.wsg.commons.Region;
import cn.wsg.commons.internet.common.video.MovieGenre;
import cn.wsg.commons.internet.org.schema.item.CreativeWork;
import cn.wsg.commons.jackson.JsonJoinedValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

/**
 * @author Kingen
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class RenrenVideoIndex implements CreativeWork {

    @JsonProperty("dramaId")
    private Integer id;

    @JsonProperty("dramaType")
    private VideoType type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("originalName")
    private String originalName;

    @JsonProperty("subtitle")
    private String subtitle;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("seasonNo")
    private Integer seasonNo;

    @JsonProperty("coverUrl")
    private URL image;

    @JsonProperty("plotType")
    @JsonJoinedValue
    private List<MovieGenre> genre;

    @JsonProperty("area")
    private Region area;

    @JsonProperty("score")
    private Double score;
}
