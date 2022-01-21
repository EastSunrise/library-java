package cn.wsg.repository.com.imdb;

import cn.wsg.commons.internet.common.video.ReleaseInfo;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.util.RegExUtilsExt;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Kingen
 * @see <a href="https://www.imdb.com/">IMDb</a>
 * @see <a href="https://developer.imdb.com/documentation">Documentation & Data Dictionary</a>
 */
public interface ImdbRepository {

    Pattern TITLE_ID_REGEX = Pattern.compile("tt\\d{7,8}");

    Pattern NAME_ID_REGEX = Pattern.compile("nm\\d{7}");

    /**
     * Checks whether the specified value is a valid IMDb ID of a creative work.
     *
     * @param titleId the ID to be checked
     * @return the ID if valid
     * @throws IllegalArgumentException if the value is invalid
     */
    static String checkTitleId(String titleId) {
        RegExUtilsExt.matchesOrElseThrow(TITLE_ID_REGEX, titleId);
        return titleId;
    }

    /**
     * Checks whether the specified value is a valid IMDb ID of a person.
     *
     * @param nameId the ID to be checked
     * @return the ID if valid
     * @throws IllegalArgumentException if the value is invalid
     */
    static String checkNameId(String nameId) {
        RegExUtilsExt.matchesOrElseThrow(NAME_ID_REGEX, nameId);
        return nameId;
    }

    /**
     * Retrieves the top 250 movies.
     *
     * @return the ids of the top 250 movies in rank
     */
    List<String> top250();

    /**
     * Retrieves the top 250 TV shows.
     *
     * @return the ids of the top 250 TV shows in rank
     */
    List<String> top250TV();

    /**
     * Retrieves the most popular movies.
     *
     * @return the ids of the most popular movies in rank
     */
    List<String> mostPopular();

    /**
     * Retrieve a title by the given identifier.
     *
     * @param titleId identifier, starting with 'tt'
     * @return the title
     * @throws NullPointerException     if the specified identifier is null
     * @throws IllegalArgumentException if the specified identifier is not valid
     * @throws NotFoundException        if the item is not found
     */
    ImdbCreativeWork findTitleById(String titleId) throws NotFoundException;

    /**
     * Retrieves the release information the target video.
     *
     * @param titleId identifier, starting with 'tt'
     * @return the release information
     * @throws NullPointerException     if the specified identifier is null
     * @throws IllegalArgumentException if the specified identifier is not valid
     * @throws NotFoundException        if the item is not found
     */
    ReleaseInfo findReleaseInfo(String titleId) throws NotFoundException;

    /**
     * Retrieves all ids of episodes of the specified TV season of the TV series.
     *
     * @param seriesTitleId the id of the TV series
     * @param season        the target season
     * @return all ids of episodes of the season, or an empty array if no episodes are found. The index of episode i is
     * [i] and any episode could be null if
     * not found.
     * @throws NotFoundException if the series or the season is not found
     */
    String[] findEpisodesOfSeries(String seriesTitleId, int season) throws NotFoundException;

    /**
     * Retrieve a person by the given identifier.
     *
     * @param nameId identifier, starting with 'nm'
     * @return the person with detailed info
     * @throws NullPointerException if the specified identifier is null
     * @throws NotFoundException    if the person is not found
     */
    ImdbPerson findPersonById(String nameId) throws NotFoundException;
}
