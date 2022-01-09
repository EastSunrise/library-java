package cn.wsg.repository.dao.mapper.video;

import cn.wsg.repository.common.dto.QueryVideoDTO;
import cn.wsg.repository.entity.video.MovieDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kingen
 */
@Mapper
public interface MovieMapper {

    /**
     * Inserts a new movie.
     *
     * @param movieDO the movie to be inserted. The property {@link MovieDO#getId()} will be set
     *                with the generated key.
     * @return 1 if successes, 0 otherwise
     */
    int insert(MovieDO movieDO);

    /**
     * Counts eligible movies.
     *
     * @param cond condition to count by
     * @return the number of eligible movies
     */
    int countBy(QueryVideoDTO cond);

    /**
     * Queries the movie of the specified IMDb id.
     *
     * @param imdbId the IMDb id
     * @return the movie of the specified IMDb id, or null if not found
     */
    MovieDO selectByImdbId(String imdbId);

    /**
     * Queries the movie of the specified Douban id.
     *
     * @param doubanId the Douban id
     * @return the number of movies of the specified Douban id.
     */
    MovieDO selectByDoubanId(long doubanId);
}