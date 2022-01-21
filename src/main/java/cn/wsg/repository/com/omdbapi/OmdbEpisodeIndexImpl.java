package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.DatetimeConsts;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author Kingen
 */
@Getter
@ToString
class OmdbEpisodeIndexImpl implements OmdbEpisodeIndex {

    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Episode")
    private Integer episode;

    @JsonProperty("Released")
    @JsonFormat(pattern = DatetimeConsts.PAT_ISO_LOCAL_DATE)
    private LocalDate release;

    @JsonProperty("imdbRating")
    private Double imdbRating;
}
