package cn.wsg.library.entity;

import cn.wsg.library.common.enums.CollectStatus;
import cn.wsg.library.common.enums.ReadStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Kingen
 */
@Getter
@Setter
@ToString
public class BookDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long isbn;

    private String title;

    private String originalTitle;

    private String cover;

    private String press;

    private LocalDate publishDate;

    private String category;

    private String description;

    private String content;

    private String link;

    private CollectStatus collectStatus;

    private ReadStatus readStatus;

    @ToString.Exclude
    private LocalDateTime gmtModified;
}