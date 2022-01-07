package cn.wsg.repository.service;

import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.internet.support.OtherResponseException;
import cn.wsg.repository.common.error.DataIntegrityException;

/**
 * @author Kingen
 */
public interface VideoService {

    /**
     * Imports a video of the specified imdbId.
     *
     * @param imdbId id of IMDb, not blank
     * @return the id of inserted video
     * @throws OtherResponseException   if an unexpected error occurs
     * @throws DataIntegrityException   if some required properties are lacking
     * @throws NotFoundException        if the video of the given id is not found from IMDb
     * @throws IllegalArgumentException if the specified imdbId is blank
     */
    long importVideoFromImdb(String imdbId) throws OtherResponseException, DataIntegrityException, NotFoundException;

    /**
     * Imports a video of the specified doubanId.
     *
     * @param doubanId the doubanId of the video to be imported
     * @return the id of inserted video
     * @throws OtherResponseException if an unexpected error occurs
     * @throws DataIntegrityException if some required properties are lacking
     * @throws NotFoundException      if the video of the given id is not found from Douban
     */
    long importVideoFromDouban(long doubanId) throws DataIntegrityException, OtherResponseException, NotFoundException;
}
