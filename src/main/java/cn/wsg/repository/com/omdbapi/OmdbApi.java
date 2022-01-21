package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.internet.page.Page;
import cn.wsg.commons.internet.page.PageIndex;
import cn.wsg.commons.internet.support.NotFoundException;

/**
 * The interface provides methods to query IMDb movies from an open api.
 *
 * @author Kingen
 * @see <a href="https://www.omdbapi.com/">OMDb API</a>
 */
public interface OmdbApi {

    /**
     * Returns the key to access to the api.
     *
     * @return the key
     */
    String getApikey();

    /**
     * Retrieves a video of the specified id.
     *
     * @param imdbId      a valid IMDb ID (starting with 'tt')
     * @param optionalReq optional arguments
     * @return the video
     * @throws NotFoundException if the movie is not found
     */
    OmdbVideo findById(String imdbId, OmdbOptionalReq optionalReq) throws NotFoundException;

    /**
     * Searches videos by the specified title.
     *
     * @param title     the title to search by
     * @param pageIndex pagination information
     * @param op        optional arguments
     * @return searched videos
     * @throws NotFoundException        if no movies are found
     * @throws IllegalArgumentException if the title is blank
     */
    Page<OmdbVideo> searchByTitle(String title, PageIndex pageIndex, OmdbOptionalReq op) throws NotFoundException;

    /**
     * Retrieves the specified season.
     *
     * @param seriesId the IMDb ID of the series
     * @param season   the number of the season, starting with 1
     * @return the season
     * @throws NotFoundException        if the season is not found
     * @throws IllegalArgumentException if the number of the season is not positive
     */
    OmdbSeason findSeason(String seriesId, int season) throws NotFoundException;

    /**
     * Retrieves the specified episode.
     *
     * @param seriesId the IMDb ID of the series
     * @param season   the season to which the episode belongs, starting with 1
     * @param episode  the number of the episode, starting with 1
     * @return the episode
     * @throws NotFoundException        if the episode is not found
     * @throws IllegalArgumentException if any number is not positive
     */
    OmdbEpisode findEpisode(String seriesId, int season, int episode) throws NotFoundException;
}
