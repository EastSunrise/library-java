package cn.wsg.library.common.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author Kingen
 */
@Getter
@ToString
public class SaveBookDTO extends BaseBookDTO {

    private List<Long> authors;

    private List<Long> translators;
}
