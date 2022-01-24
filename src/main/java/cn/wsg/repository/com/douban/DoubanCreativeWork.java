package cn.wsg.repository.com.douban;

import cn.wsg.commons.data.common.Language;
import cn.wsg.commons.data.common.Region;
import cn.wsg.commons.data.common.video.AggregateRating;
import cn.wsg.commons.data.common.video.MovieGenre;
import cn.wsg.commons.data.schema.item.CreativeWork;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Kingen
 */
@Getter
@JsonIgnoreProperties("@context")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class DoubanCreativeWork extends DoubanThing implements CreativeWork {

    @JsonProperty("author")
    private List<DoubanPerson> authors;

    @JsonProperty("workExample")
    private List<DoubanCreativeWork> workExample;

    @JsonProperty("datePublished")
    private LocalDate datePublished;

    @JsonProperty("genre")
    private List<MovieGenre> genres;

    @JsonProperty("aggregateRating")
    private AggregateRating aggregateRating;

    @Setter(AccessLevel.PACKAGE)
    @JsonProperty("countryOfOrigin")
    private List<Region> countriesOfOrigin;

    @Setter(AccessLevel.PACKAGE)
    @JsonProperty("inLanguage")
    private List<Language> languages;
}
