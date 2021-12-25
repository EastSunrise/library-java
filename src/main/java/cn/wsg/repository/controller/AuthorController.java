package cn.wsg.repository.controller;

import cn.wsg.repository.common.dto.AuthorDTO;
import cn.wsg.repository.service.AuthorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author-related apis.
 *
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
