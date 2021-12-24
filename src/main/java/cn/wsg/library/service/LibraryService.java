package cn.wsg.library.service;

import cn.wsg.library.common.dto.CategoryDTO;

import java.util.List;

/**
 * @author Kingen
 */
public interface LibraryService {

    /**
     * Queries categories of the specified index or/and the specified super index.
     *
     * @param idx      the index of the category
     * @param superIdx the super index
     * @return list of sub categories
     */
    List<CategoryDTO> listCategories(String idx, String superIdx);
}
