package cn.wsg.repository.com.imdb;

import cn.wsg.commons.data.schema.item.TVSeries;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Kingen
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ImdbTVSeries extends ImdbCreativeWork implements TVSeries {

    @JsonProperty("actor")
    private List<ImdbPerson> actors;

    @JsonProperty("trailer")
    private ImdbVideoObject trailer;
}
