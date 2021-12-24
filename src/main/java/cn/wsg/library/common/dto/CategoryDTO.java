package cn.wsg.library.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@AllArgsConstructor
@ToString
public class CategoryDTO {

    private final String idx;

    private final String title;

    private final Boolean leaf;
}
