package cn.wsg.library.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Conditions to query books.
 *
 * @author Kingen
 */
@Getter
@Setter
@ToString
public class QueryBookDTO {

    private String title;

    private String category;

    private String author;
}
