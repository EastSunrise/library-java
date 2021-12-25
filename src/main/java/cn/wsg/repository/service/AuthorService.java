package cn.wsg.repository.service;

import cn.wsg.repository.common.dto.AuthorDTO;
import java.util.List;

/**
 * Author-related services.
 *
 * @author Kingen
 */
public interface AuthorService {

    /**
     * Stats authors of the specified name.
     *
     * @param name the name to stat by
     * @return count of matched authors
     */
    int countByName(String name);

    /**
     * Saves a new author.
     *
     * @param authorDto the author to be saved
     * @return 1 if successes, otherwise 0
     */
    int saveAuthor(AuthorDTO authorDto);

    /**
     * Queries authors whose names are like the specified key
     *
     * @param keyword the keyword to be compared
     * @return list of matched authors
     */
    List<AuthorDTO> listAuthorsByName(String keyword);
}
