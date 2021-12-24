package cn.wsg.library.controller;

import cn.wsg.library.common.dto.CategoryDTO;
import cn.wsg.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kingen
 */
@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/category/query")
    public List<CategoryDTO> findCategoriesBy(String idx, String superIdx) {
        return libraryService.listCategories(idx, superIdx);
    }
}
