package cn.wsg.repository.dao.mapper.video;

import cn.wsg.repository.entity.video.VideoIdRelationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kingen
 */
@Mapper
public interface VideoIdRelationMapper {

    /**
     * Inserts a new relation between ids. Ignore if the key exists.
     *
     * @param idRelationDO the relation to be inserted
     * @return the number of affected rows
     */
    int insertIgnore(VideoIdRelationDO idRelationDO);

    /**
     * Queries the relation of the specified IMDb id.
     *
     * @param imdbId the IMDb id to query by
     * @return the corresponding relation
     */
    VideoIdRelationDO getDoubanIdByImdbId(String imdbId);
}