package cn.wsg.repository.com.douban;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Kingen
 */
@Getter
@JsonSubTypes({
    @JsonSubTypes.Type(value = DoubanMovie.class, name = "Movie"),
    @JsonSubTypes.Type(value = DoubanTVSeries.class, name = "TVSeries")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public abstract class DoubanVideo extends DoubanCreativeWork {

    @Setter(AccessLevel.PACKAGE)
    private String zhTitle;

    @Setter(AccessLevel.PACKAGE)
    private String originalTitle;

    @Setter(AccessLevel.PACKAGE)
    private String imdbId;

}
