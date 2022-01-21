package cn.wsg.repository.com.imdb;

import cn.wsg.commons.internet.org.schema.item.MediaObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URL;

/**
 * @author Kingen
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class ImdbMediaObject extends ImdbCreativeWork implements MediaObject {

    @JsonProperty("contentUrl")
    private URL contentUrl;

    @JsonProperty("embedUrl")
    private URI embedUrl;

}
