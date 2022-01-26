package cn.wsg.repository.entity.video;

import cn.wsg.commons.data.common.Country;
import cn.wsg.commons.data.common.Language;
import cn.wsg.commons.data.common.video.MovieGenre;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Base information of videos.
 *
 * @author Kingen
 */
@Getter
@Setter
class BaseVideoInfo {

    private String zhTitle;

    private String originalTitle;

    private String[] otherTitles;

    private MovieGenre[] genres;

    private Country[] countries;

    private Language[] languages;

    private LocalDate releaseDate;

    private String description;
}
