package cn.wsg.repository.com.douban;

import cn.wsg.commons.internet.Loggable;
import cn.wsg.commons.internet.page.Page;
import cn.wsg.commons.internet.page.PageIndex;
import cn.wsg.commons.internet.support.LoginException;
import cn.wsg.commons.internet.support.NotFoundException;

import java.util.List;

/**
 * The interface provides methods to query subjects on Douban.
 * <p>
 * Notes that some x-rated subjects may be restricted to access without logging in.
 *
 * @author Kingen
 * @see <a href="https://www.douban.com/">Douban</a>
 */
public interface DoubanRepository extends Loggable<Long> {

    /**
     * Logs in the site with the specified username and password.
     *
     * @param username the username of the account
     * @param password the password of the account
     * @throws LoginException if the specified user and password is invalid or a CAPTCHA is
     *                        required.
     */
    void login(String username, String password) throws LoginException;

    /**
     * Logs out if logged-in.
     */
    void logout();

    /**
     * Searches subjects related to the specified keyword globally.
     * <p>
     * This method is limited to 10 times once.
     *
     * @param keyword keyword to search
     * @param page    pagination information
     * @param catalog the catalog to filter subjects, may null
     * @return indices of searched subjects in page
     * @throws IllegalArgumentException if the keyword is blank
     * @throws SearchException          if a problem occurs
     */
    Page<SubjectIndex> searchGlobally(String keyword, PageIndex page, DoubanCatalog catalog) throws SearchException;

    /**
     * Searches subjects related to the specified keyword under the specified topic.
     *
     * @param catalog the catalog to which the subjects belong
     * @param keyword keyword to search
     * @return indices of searched subjects
     * @throws IllegalArgumentException if the keyword is blank
     */
    List<SubjectIndex> search(DoubanCatalog catalog, String keyword);

    /**
     * Retrieves top250 movies on the site.
     *
     * @param pageIndex pagination information
     * @return ranked subject indices in page
     */
    Page<RankedSubject> top250(PageIndex pageIndex);

    /**
     * Retrieves marked subjects of the specified user.
     *
     * @param catalog catalog of the subjects to be queried
     * @param userId  the id of the user to be queried
     * @param status  marked status
     * @param page    pagination information
     * @return marked subjects in page
     * @throws NotFoundException if the user is not found
     */
    Page<MarkedSubject> findUserSubjects(DoubanCatalog catalog, long userId, MarkedStatus status, PageIndex page)
        throws NotFoundException;

    /**
     * Retrieves collected creators of the specified user.
     *
     * @param userId  the id of the user to be queried
     * @param catalog catalog of the creators to be queried
     * @param page    pagination information
     * @return indices of collected creators in page
     * @throws NotFoundException if the user is not found
     */
    Page<PersonIndex> findUserCreators(DoubanCatalog catalog, long userId, PageIndex page) throws NotFoundException;

    /**
     * Retrieves the video of the specified id.
     *
     * @param id id of the video to be queried
     * @return the movie or TV series
     * @throws NotFoundException if the video is not found
     */
    DoubanVideo findVideoById(long id) throws NotFoundException;

    /**
     * Retrieves the book of the specified id.
     *
     * @param id id of the book to be queried
     * @return the book
     * @throws NotFoundException if the book is not found
     */
    DoubanBook findBookById(long id) throws NotFoundException;

    /**
     * Gets the corresponding subject-id of the specified IMDb-id.
     *
     * @param imdbId the id of the subject on IMDb
     * @return the corresponding id of the subject
     * @throws NotFoundException if the specified IMDb-id is not found
     * @throws LoginException    if not logged-in
     */
    long getDbIdByImdbId(String imdbId) throws NotFoundException, LoginException;
}
