package cn.wsg.repository.com.omdbapi;

import lombok.Getter;

/**
 * The request with optional parameters when accessing to the OMDb API.
 *
 * @author Kingen
 */
@Getter
public class OmdbOptionalReq {

    private final VideoType type;
    private final Integer year;

    public OmdbOptionalReq(VideoType type, Integer year) {
        this.type = type;
        this.year = year;
    }

    public enum VideoType {
        MOVIE,
        SERIES,
        EPISODE
    }
}
