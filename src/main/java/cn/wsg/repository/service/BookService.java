package cn.wsg.repository.service;

import cn.wsg.repository.common.dto.BookDTO;
import cn.wsg.repository.common.dto.QueryBookDTO;
import cn.wsg.repository.common.dto.SaveBookDTO;
import cn.wsg.repository.common.enums.ReadStatus;

import java.util.List;

/**
 * Book-related services.
 *
 * @author Kingen
 */
public interface BookService {

    /**
     * Checks whether the book exists based on the unique ISBN number.
     *
     * @param isbn the ISBN number to be checked
     * @return 1 if exists, otherwise 0
     */
    int countByIsbn(long isbn);

    /**
     * Saves a new book.
     *
     * @param saveBookDto the book to be saved
     * @return 1 if successes, otherwise 0
     */
    int saveBook(SaveBookDTO saveBookDto);

    /**
     * Updates the reading status of the book of the specified ISBN number to the specified status.
     *
     * @param isbn   the ISBN number of the book to be changed
     * @param status the target status
     * @return 1 if successes, otherwise 0
     */
    int updateReadStatus(long isbn, ReadStatus status);

    /**
     * Queries books that match the specified condition.
     *
     * @param cond the condition to query by
     * @return list of queried books
     */
    List<BookDTO> listBooks(QueryBookDTO cond);
}
