package cn.wsg.repository.dao.mapper.lib;

import cn.wsg.repository.common.dto.BookDTO;
import cn.wsg.repository.common.dto.QueryBookDTO;
import cn.wsg.repository.common.enums.ReadStatus;
import cn.wsg.repository.entity.lib.BookDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for CRUD operations of books.
 *
 * @author Kingen
 */
@Mapper
public interface BookMapper {

    /**
     * Counts books of the specified id.
     *
     * @param isbn the id of the book
     * @return the number of the book of the id
     */
    int countByPrimaryKey(long isbn);

    /**
     * Inserts a new book.
     *
     * @param book the book to be inserted.
     * @return 1 if successes, otherwise 0
     */
    int insert(BookDO book);

    /**
     * Queries books based on the specified condition. Returned are the books
     * <ul>
     *     <li>whose titles are like {@link QueryBookDTO#getTitle()}</li> and
     *     <li>whose authors are named with {@link QueryBookDTO#getAuthor()} fuzzily</li> and
     *     <li>whose categories start with {@link QueryBookDTO#getCategory()}</li>
     * </ul>
     *
     * @param cond the condition to query by
     * @return list of matched books
     */
    List<BookDTO> listBy(QueryBookDTO cond);

    /**
     * Updates the reading status of the book of the specified ISBN number.
     *
     * @param isbn   the ISBN number of the book to be changed
     * @param status the target status
     * @return 1 if successes, otherwise 0
     */
    int updateReadStatus(long isbn, ReadStatus status);
}