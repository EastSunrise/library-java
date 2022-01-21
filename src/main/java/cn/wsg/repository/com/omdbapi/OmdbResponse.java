package cn.wsg.repository.com.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * The common response of OMDb API.
 *
 * @author Kingen
 */
@Getter
class OmdbResponse {

    @JsonProperty("Response")
    private boolean success;

    @JsonProperty("Error")
    private String error;
}
