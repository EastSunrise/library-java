package cn.wsg.repository.service;

import cn.wsg.repository.common.dto.CategoryDTO;
import cn.wsg.repository.dao.mapper.lib.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kingen
 */
@Slf4j
@Service
public class LibraryServiceImpl implements LibraryService {

    private final CategoryMapper categoryMapper;

    @Autowired
    public LibraryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> listCategories(String idx, String superIdx) {
        if (StringUtils.isBlank(idx) && StringUtils.isBlank(superIdx)) {
            return new ArrayList<>();
        }
        return categoryMapper.listBy(idx, superIdx).stream()
            .map(category -> new CategoryDTO(category.getIdx(), category.getTitle(), category.getLeaf()))
            .collect(Collectors.toList());
    }
}
