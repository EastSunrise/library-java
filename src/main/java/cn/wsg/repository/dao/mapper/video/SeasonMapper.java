package cn.wsg.repository.dao.mapper.video;

import cn.wsg.repository.entity.video.SeasonDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kingen
 */
@Mapper
public interface SeasonMapper {

    /**
     * Queries the season of the specified Douban id.
     *
     * @param doubanId the Douban id
     * @return the season of the specified Douban id.
     */
    SeasonDO selectByDoubanId(long doubanId);

    /**
     * Inserts a season of TV series.
     *
     * @param season the season to be inserted. The property {@link SeasonDO#getId()} will be set with the generated key.
     * @return the number of affected rows
     */
    int insert(SeasonDO season);
}