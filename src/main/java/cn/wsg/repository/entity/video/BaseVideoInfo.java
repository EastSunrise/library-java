package cn.wsg.repository.entity.video;

import cn.wsg.commons.lang.Language;
import cn.wsg.commons.lang.Region;
import cn.wsg.repository.common.enums.Genre;
import java.time.LocalDate;
import lombok.Getter;

/**
 * Base information of videos.
 *
 * @author Kingen
 */
@Getter
public class BaseVideoInfo {

    private String zhTitle;

    private String originalTitle;

    private String enTitle;

    private String[] otherTitles;

    private String cover;

    private Genre[] genres;

    private Region[] regions;

    private Language[] languages;

    private LocalDate releaseDate;

    private int[] runtimes;

    private String description;
}
