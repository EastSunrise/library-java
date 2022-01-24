package cn.wsg.repository.com.douban;

import cn.wsg.commons.data.schema.item.Thing;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * The root type of objects on {@link DoubanRepository}.
 *
 * @author Kingen
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class DoubanThing implements Thing {

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private URI url;

    @JsonProperty("image")
    private URL image;

    @JsonProperty("description")
    private String description;

    @JsonProperty("sameAs")
    private URL sameAs;

    @JsonProperty("alternateName")
    @Setter(AccessLevel.PACKAGE)
    private List<String> alternateNames;
}
