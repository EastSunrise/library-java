package cn.wsg.library.controller;

import cn.wsg.library.common.dto.AuthorDTO;
import cn.wsg.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kingen
 */
@RestController
@RequestMapping("/api/library/author")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/check")
    public int checkUniqueName(String name) {
        return authorService.countByName(name);
    }

    @PostMapping("/save")
    public int saveAuthor(@RequestBody AuthorDTO authorDto) {
        return authorService.saveAuthor(authorDto);
    }

    @GetMapping("/query")
    public List<AuthorDTO> findAuthorsByName(String keyword) {
        return authorService.listAuthorsByName(keyword);
    }
}
