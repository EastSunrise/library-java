package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.data.schema.item.TVSeason;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OmdbSeason extends OmdbResponse implements TVSeason {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Season")
    private Integer season;

    @JsonProperty("totalSeasons")
    private Integer totalSeasons;

    @JsonProperty("Episodes")
    @JsonDeserialize(contentAs = OmdbEpisodeIndexImpl.class)
    private List<OmdbEpisodeIndex> episodes;
}
