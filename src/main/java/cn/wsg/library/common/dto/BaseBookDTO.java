package cn.wsg.library.common.dto;

import cn.wsg.commons.lang.enums.Language;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Base properties of a book.
 *
 * @author Kingen
 */
@Getter
@ToString
public class BaseBookDTO {

    private Long isbn;

    private String title;

    private String originalTitle;

    private String cover;

    private String press;

    private LocalDate publishDate;

    private String category;

    private Language language;

    private String description;

    private String content;

    private String link;
}
