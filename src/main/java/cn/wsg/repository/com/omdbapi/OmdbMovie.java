package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.data.schema.item.Movie;
import cn.wsg.commons.jackson.JsonUnknownType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OmdbMovie extends OmdbVideo implements Movie {

    @JsonProperty("Year")
    private Integer year;

    @JsonProperty("DVD")
    @JsonFormat(pattern = "dd MMM yyyy")
    private LocalDate dvd;

    @JsonProperty("BoxOffice")
    private JsonUnknownType boxOffice;

    @JsonProperty("Production")
    private JsonUnknownType production;

    @JsonProperty("Website")
    private JsonUnknownType website;
}
