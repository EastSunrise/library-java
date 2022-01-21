package cn.wsg.repository.com.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OmdbEpisode extends OmdbVideo implements OmdbEpisodeIndex {

    @JsonProperty("seriesID")
    private String seriesId;

    @JsonProperty("Season")
    private Integer season;

    @JsonProperty("Episode")
    private Integer episode;

    @JsonProperty("Year")
    private Integer year;
}
