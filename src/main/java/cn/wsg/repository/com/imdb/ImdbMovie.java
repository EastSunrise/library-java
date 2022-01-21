package cn.wsg.repository.com.imdb;

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
public class ImdbMovie extends ImdbCreativeWork implements Movie {

    @JsonProperty("trailer")
    private ImdbVideoObject trailer;

    @JsonProperty("actor")
    private List<ImdbPerson> actors;

    @JsonProperty("director")
    private List<ImdbPerson> directors;

    @JsonProperty("duration")
    private Duration duration;
}
