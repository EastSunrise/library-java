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
public class SeriesDO implements Serializable {

    private static final long serialVersionUID = 190012839465234142L;

    private Long id;

    private String imdbId;

    private String zhTitle;

    private String originalTitle;

    private String enTitle;

    private String[] otherTitles;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}