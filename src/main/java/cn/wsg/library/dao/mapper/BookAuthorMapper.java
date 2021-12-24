package cn.wsg.library.dao.mapper;

import cn.wsg.library.entity.BookAuthorDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for CRUD operations of relations between authors and books.
 *
 * @author Kingen
 */
@Mapper
public interface BookAuthorMapper {

    /**
     * Links an author or a translator to a book.
     *
     * @param record the link to be inserted. The property {@link BookAuthorDO#getId()} will be set
     *               with the generated key.
     * @return 1 if successes, otherwise 0
     */
    int insert(BookAuthorDO record);
}