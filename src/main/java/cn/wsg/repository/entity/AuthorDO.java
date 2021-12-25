package cn.wsg.repository.entity;

import cn.wsg.commons.lang.Region;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@Setter
@ToString
public class AuthorDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String originalName;

    private Region region;

    private String link;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}