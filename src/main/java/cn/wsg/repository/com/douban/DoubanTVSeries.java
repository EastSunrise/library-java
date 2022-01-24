package cn.wsg.repository.com.douban;

import cn.wsg.commons.data.schema.item.TVSeries;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

/**
 * @author Kingen
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DoubanTVSeries extends DoubanVideo implements TVSeries {

    @JsonProperty("director")
    private List<DoubanPerson> directors;

    @JsonProperty("actor")
    private List<DoubanPerson> actors;

    @Setter(AccessLevel.PACKAGE)
    @JsonProperty("numberOfEpisodes")
    private Integer numberOfEpisodes;

    // extended properties

    @JsonProperty("duration")
    private Duration duration;
}
