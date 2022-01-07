package cn.wsg.repository.service;

import cn.wsg.repository.common.dto.BookDTO;
import cn.wsg.repository.common.dto.QueryBookDTO;
import cn.wsg.repository.common.dto.SaveBookDTO;
import cn.wsg.repository.common.enums.CollectStatus;
import cn.wsg.repository.common.enums.ReadStatus;
import cn.wsg.repository.common.enums.WorkType;
import cn.wsg.repository.dao.mapper.lib.BookAuthorMapper;
import cn.wsg.repository.dao.mapper.lib.BookMapper;
import cn.wsg.repository.entity.lib.BookAuthorDO;
import cn.wsg.repository.entity.lib.BookDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kingen
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    private final BookAuthorMapper bookAuthorMapper;

    @Autowired
    public BookServiceImpl(BookMapper bookMapper, BookAuthorMapper bookAuthorMapper) {
        this.bookMapper = bookMapper;
        this.bookAuthorMapper = bookAuthorMapper;
    }

    @Override
    public int countByIsbn(long isbn) {
        return bookMapper.countByPrimaryKey(isbn);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int saveBook(SaveBookDTO bookDto) {
        BookDO book = new BookDO();
        BeanUtils.copyProperties(bookDto, book);
        book.setCollectStatus(CollectStatus.WI);
        book.setReadStatus(ReadStatus.WI);
        int result = bookMapper.insert(book);
        if (result == 0) {
            return 0;
        }
        long isbn = book.getIsbn();
        for (long authorId : bookDto.getAuthors()) {
            BookAuthorDO bookAuthor = new BookAuthorDO(isbn, authorId, WorkType.AU);
            if (bookAuthorMapper.insert(bookAuthor) != 1) {
                log.error("Failed to link author:{} to book:{}", authorId, isbn);
                throw new RuntimeException("Failed to link an author to the book");
            }
        }
        if (CollectionUtils.isNotEmpty(bookDto.getTranslators())) {
            for (long translatorId : bookDto.getTranslators()) {
                BookAuthorDO bookAuthor = new BookAuthorDO(isbn, translatorId, WorkType.TR);
                if (bookAuthorMapper.insert(bookAuthor) != 1) {
                    log.error("Failed to link translator:{} to book:{}", translatorId, isbn);
                    throw new RuntimeException("Failed to link a translator to the book");
                }
            }
        }
        return 1;
    }

    @Override
    public int updateReadStatus(long isbn, ReadStatus status) {
        return bookMapper.updateReadStatus(isbn, status);
    }

    @Override
    public List<BookDTO> listBooks(QueryBookDTO cond) {
        return bookMapper.listBy(cond);
    }
}
