package cn.wsg.repository.entity.video;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kingen
 */
@Getter
@Setter
@ToString
public class SeasonDO extends BaseVideoInfo implements Serializable {

    private static final long serialVersionUID = 698368440022584697L;

    private Long id;

    private Long seriesId;

    private Integer currentSeason;

    private Integer episodesCount;

    private Long doubanId;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}