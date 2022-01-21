package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.internet.org.schema.item.TVSeries;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OmdbSeries extends OmdbVideo implements TVSeries {

    @JsonProperty("totalSeasons")
    private Integer totalSeasons;

    @JsonProperty("Year")
    private String year;
}
