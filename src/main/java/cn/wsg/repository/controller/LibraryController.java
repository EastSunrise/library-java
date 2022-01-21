package cn.wsg.repository.controller;

import cn.wsg.repository.common.dto.CategoryDTO;
import cn.wsg.repository.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Library-related apis.
 *
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
