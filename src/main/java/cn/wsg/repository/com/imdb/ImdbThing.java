package cn.wsg.repository.com.imdb;

import cn.wsg.commons.data.schema.item.Thing;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URL;

/**
 * The root type of objects on {@link ImdbRepository}.
 *
 * @author Kingen
 */
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(@JsonSubTypes.Type(value = ImdbCreativeWork.class, name = "CreativeWork"))
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class ImdbThing implements Thing {

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private URI url;

    @JsonProperty("image")
    private URL image;

    @JsonProperty("description")
    private String description;

    @JsonProperty("alternateName")
    private String alternateName;
}
