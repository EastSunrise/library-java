package cn.wsg.repository.dao.mapper.video;

import cn.wsg.repository.common.dto.QueryVideoDTO;
import cn.wsg.repository.entity.video.SeriesDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kingen
 */
@Mapper
public interface SeriesMapper {

    /**
     * Inserts a new TV series.
     *
     * @param series the series to be inserted. The property {@link SeriesDO#getId()} will be set with the generated
     *               key.
     * @return the number of affected rows
     */
    int insert(SeriesDO series);

    /**
     * Counts eligible series.
     *
     * @param cond condition to count by
     * @return the number of eligible series
     */
    int countBy(QueryVideoDTO cond);

    /**
     * Queries the series of the specified IMDb id.
     *
     * @param imdbId the IMDb id
     * @return the series of the specified IMDb id.
     */
    SeriesDO selectByImdbId(String imdbId);
}