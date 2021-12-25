package cn.wsg.repository.dao.mapper;

import cn.wsg.repository.entity.CategoryDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for CRUD operations of categories.
 *
 * @author Kingen
 */
@Mapper
public interface CategoryMapper {

    /**
     * Queries categories of the specified index or/and the specified super index.
     *
     * @param idx      the index of the category
     * @param superIdx the super index
     * @return list of matched categories
     */
    List<CategoryDO> listBy(String idx, String superIdx);

    /**
     * Counts the number of all indices.
     *
     * @return the number of all indices
     */
    int countAll();

    /**
     * Inserts a new category.
     *
     * @param category the category to be inserted
     * @return 1 if successes, otherwise 0
     */
    int insert(CategoryDO category);
}