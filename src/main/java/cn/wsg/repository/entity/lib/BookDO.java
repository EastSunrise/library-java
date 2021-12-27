package cn.wsg.repository.entity.lib;

import cn.wsg.repository.common.enums.CollectStatus;
import cn.wsg.repository.common.enums.ReadStatus;
import java.io.Serializable;
import java.time.LocalDate;
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