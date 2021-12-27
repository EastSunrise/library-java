package cn.wsg.repository.dao.mapper.lib;

import cn.wsg.repository.entity.lib.AuthorDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for CRUD operations of authors.
 *
 * @author Kingen
 */
@Mapper
public interface AuthorMapper {

    /**
     * Counts authors of the specified name
     *
     * @param name the specified name to count by
     * @return the number of authors of the specified name
     */
    int countByName(String name);

    /**
     * Inserts a new author.
     *
     * @param author the author to be inserted. The property {@link AuthorDO#getId()} will be set
     *               with the generated key.
     * @return 1 if successes, otherwise 0
     */
    int insert(AuthorDO author);

    /**
     * Queries authors whose names or originalNames are like the keyword.
     *
     * @param keyword the keyword to be compared
     * @return list of matched authors
     */
    List<AuthorDO> listByName(String keyword);
}