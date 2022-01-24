package cn.wsg.repository.entity.video;

import cn.wsg.commons.data.common.Region;
import cn.wsg.repository.common.enums.Gender;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Kingen
 */
@Getter
@ToString
public class CelebrityDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long doubanId;

    private String imdbId;

    private String zhName;

    private String originalName;

    private String enName;

    private Gender gender;

    private String profile;

    private LocalDate birthday;

    private Region region;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}