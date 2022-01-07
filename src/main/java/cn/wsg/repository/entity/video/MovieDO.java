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
public class MovieDO extends BaseVideoInfo implements Serializable {

    private static final long serialVersionUID = -45094030292234000L;

    private Long id;

    private Long doubanId;

    private String imdbId;

    private String enTitle;

    private int[] runtimes;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}