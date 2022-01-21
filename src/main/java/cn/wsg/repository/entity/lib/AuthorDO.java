package cn.wsg.repository.entity.lib;

import cn.wsg.commons.Region;
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