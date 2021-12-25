package cn.wsg.repository.common.dto;

import cn.wsg.repository.common.enums.ReadStatus;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * Extra properties of a book.
 *
 * @author Kingen
 */
@Getter
@ToString
public class BookDTO extends BaseBookDTO {

    private List<AuthorDTO> authors;

    private List<AuthorDTO> translators;

    private ReadStatus readStatus;
}
