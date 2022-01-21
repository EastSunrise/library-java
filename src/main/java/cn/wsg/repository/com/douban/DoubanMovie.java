package cn.wsg.repository.com.douban;

import cn.wsg.commons.internet.org.schema.item.Movie;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;

/**
 * @author Kingen
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DoubanMovie extends DoubanVideo implements Movie {

    @JsonProperty("director")
    private List<DoubanPerson> directors;

    @JsonProperty("actor")
    private List<DoubanPerson> actors;

    @JsonProperty("duration")
    private Duration duration;
}
