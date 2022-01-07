package cn.wsg.repository.entity.video;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Kingen
 */
@Getter
@Setter
@ToString
public class VideoIdRelationDO implements Serializable {
    private static final long serialVersionUID = 264708406338303161L;

    private Long doubanId;

    private String imdbId;
}