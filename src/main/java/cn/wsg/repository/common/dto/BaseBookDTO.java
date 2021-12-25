package cn.wsg.repository.common.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;

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

    private String description;

    private String content;

    private String link;
}
