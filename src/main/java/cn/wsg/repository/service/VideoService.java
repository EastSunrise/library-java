package cn.wsg.repository.service;

import cn.wsg.commons.internet.com.douban.DoubanVideo;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.repository.common.dto.QueryVideoDTO;
import cn.wsg.repository.common.error.DataIntegrityException;

/**
 * @author Kingen
 */
public interface VideoService {

    /**
     * Counts eligible videos.
     *
     * @param cond condition to count by
     * @return the number of eligible videos
     */
    int countVideoBy(QueryVideoDTO cond);

    /**
     * Imports a video from Douban.
     *
     * @param doubanId the id of the video to be imported
     * @param video    the video to be imported
     * @return the id of inserted video
     * @throws DataIntegrityException if some required properties are lacking
     */
    long importVideoFromDouban(long doubanId, DoubanVideo video) throws DataIntegrityException;

    /**
     * Imports a video of the specified doubanId.
     *
     * @param doubanId the doubanId of the video to be imported
     * @return the id of inserted video
     * @throws DataIntegrityException if some required properties are lacking
     * @throws NotFoundException      if the video of the given id is not found from Douban
     */
    long importVideoFromDouban(long doubanId) throws DataIntegrityException, NotFoundException;

    /**
     * Imports a video of the specified imdbId.
     *
     * @param imdbId id of IMDb, not blank
     * @return the id of inserted video
     * @throws DataIntegrityException   if some required properties are lacking
     * @throws NotFoundException        if the video of the given id is not found from IMDb
     * @throws IllegalArgumentException if the specified imdbId is blank
     */
    long importVideoFromImdb(String imdbId) throws DataIntegrityException, NotFoundException;
}
