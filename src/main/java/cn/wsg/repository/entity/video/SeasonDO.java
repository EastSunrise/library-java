package cn.wsg.repository.entity.video;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@ToString
public class SeasonDO extends BaseVideoInfo implements Serializable {

    private static final long serialVersionUID = 698368440022584697L;

    private Long id;

    private Long doubanId;

    private String imdbId;

    private Integer currentSeason;

    private Integer episodesCount;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}