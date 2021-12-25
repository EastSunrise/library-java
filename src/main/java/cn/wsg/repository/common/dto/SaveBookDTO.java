package cn.wsg.repository.common.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@ToString
public class SaveBookDTO extends BaseBookDTO {

    private List<Long> authors;

    private List<Long> translators;
}
