package cn.wsg.repository.controller;

import cn.wsg.repository.common.dto.BookDTO;
import cn.wsg.repository.common.dto.QueryBookDTO;
import cn.wsg.repository.common.dto.SaveBookDTO;
import cn.wsg.repository.common.enums.ReadStatus;
import cn.wsg.repository.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Book-related apis.
 *
 * @author Kingen
 */
@RestController
@RequestMapping("/api/library/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/check")
    public int checkUniqueBook(long isbn) {
        return bookService.countByIsbn(isbn);
    }

    @PostMapping("/save")
    public int saveBook(@RequestBody SaveBookDTO saveBookDto) {
        return bookService.saveBook(saveBookDto);
    }

    @PostMapping("/read")
    public int updateReadStatus(long isbn, ReadStatus status) {
        return bookService.updateReadStatus(isbn, status);
    }

    @PostMapping("/query")
    public List<BookDTO> findBooksBy(@RequestBody QueryBookDTO cond) {
        return bookService.listBooks(cond);
    }
}
