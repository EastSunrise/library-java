package cn.wsg.repository.com.imdb;

import cn.wsg.commons.internet.org.schema.item.VideoObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Kingen
 */
@Getter
@JsonTypeName("VideoObject")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ImdbVideoObject extends ImdbMediaObject implements VideoObject {

    @JsonProperty("thumbnail")
    private ImdbImageObject thumbnail;
}