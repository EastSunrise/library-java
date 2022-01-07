package cn.wsg.repository.dao.mapper.video;

import cn.wsg.repository.entity.video.SeriesDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kingen
 */
@Mapper
public interface SeriesMapper {

    /**
     * Queries the series of the specified IMDb id.
     *
     * @param imdbId the IMDb id
     * @return the series of the specified IMDb id.
     */
    SeriesDO selectByImdbId(String imdbId);

    /**
     * Inserts a new TV series.
     *
     * @param series the series to be inserted. The property {@link SeriesDO#getId()} will be set with the generated key.
     * @return the number of affected rows
     */
    int insert(SeriesDO series);
}