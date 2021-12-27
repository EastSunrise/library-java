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
public class MovieDO extends BaseVideoInfo implements Serializable {

    private static final long serialVersionUID = -45094030292234000L;

    private Long id;

    private Long doubanId;

    private String imdbId;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}