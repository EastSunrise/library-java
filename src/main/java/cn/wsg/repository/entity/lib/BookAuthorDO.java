package cn.wsg.repository.entity.lib;

import cn.wsg.repository.common.enums.WorkType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor
public class BookAuthorDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long bookIsbn;

    private Long authorId;

    private WorkType workType;

    public BookAuthorDO(Long bookIsbn, Long authorId, WorkType workType) {
        this.bookIsbn = bookIsbn;
        this.authorId = authorId;
        this.workType = workType;
    }
}