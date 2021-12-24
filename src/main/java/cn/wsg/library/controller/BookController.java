package cn.wsg.library.controller;

import cn.wsg.library.common.dto.BookDTO;
import cn.wsg.library.common.dto.QueryBookDTO;
import cn.wsg.library.common.dto.SaveBookDTO;
import cn.wsg.library.common.enums.ReadStatus;
import cn.wsg.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
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
