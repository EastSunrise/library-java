package cn.wsg.repository.entity.video;

import cn.wsg.repository.common.enums.CelebrityStatus;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kingen
 */
@Getter
@ToString
public class VideoCelebrityRelationDO implements Serializable {

    private static final long serialVersionUID = 759730662561545555L;

    private Long id;

    private Long videoId;

    private Long celebrityId;

    private CelebrityStatus status;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}