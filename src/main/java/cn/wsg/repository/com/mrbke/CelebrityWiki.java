package cn.wsg.repository.com.mrbke;

import cn.wsg.commons.internet.page.AmountCountablePage;
import cn.wsg.commons.internet.page.PageIndex;
import cn.wsg.commons.internet.repository.ListRepository;
import cn.wsg.commons.internet.repository.RepoRetrievable;
import cn.wsg.commons.internet.support.NotFoundException;

import java.util.List;

/**
 * @author Kingen
 * @see <a href="https://www.mrbke.com/">Wiki of Celebrities</a>
 */
public interface CelebrityWiki extends RepoRetrievable<CelebrityIndex, Celebrity> {

    /**
     * Returns the repository of all celebrities on the site.
     *
     * @return the repository of all celebrities
     */
    ListRepository<CelebrityIndex, Celebrity> getRepository();

    /**
     * Retrieves all indices of celebrities on the site.
     *
     * @return list of all indices of celebrities
     */
    List<CelebrityIndex> findAllCelebrityIndices();

    /**
     * Retrieves celebrities of the specified type by pagination.
     *
     * @param pageIndex pagination information
     * @param type      the type of celebrities to search. All types of celebrities will be returned if null.
     * @return page of indices of celebrities
     * @throws NotFoundException if the target is not found
     */
    AmountCountablePage<CelebrityIndex> findByPage(PageIndex pageIndex, CelebrityType type) throws NotFoundException;

    /**
     * Retrieves albums of the specified type by pagination.
     *
     * @param pageIndex pagination information
     * @param type      the type of albums to search. All types of albums will be returned if null.
     * @return page of indices of albums
     * @throws NotFoundException if the target is not found
     */
    AmountCountablePage<AlbumIndex> findAlbumByPage(PageIndex pageIndex, AlbumType type) throws NotFoundException;

    /**
     * Retrieves the celebrity of the specified index.
     *
     * @param index must not be {@literal null}
     * @return the celebrity
     * @throws NotFoundException if the celebrity of the specified index is not found
     */
    @Override
    Celebrity findById(CelebrityIndex index) throws NotFoundException;

    /**
     * Retrieves an adult work of the specified serial number.
     *
     * @param serialNum the serial number of the target work
     * @return the target adult work
     * @throws NotFoundException if the work of the specified serial number is not found
     * @see Celebrity#getWorks()
     */
    AdultWork findAdultWork(String serialNum) throws NotFoundException;

    /**
     * Retrieves albums of the celebrity.
     *
     * @param index must not be {@literal null}
     * @return list of albums of the celebrity (empty if no albums), may be duplicate.
     * @throws NotFoundException if the target celebrity is not found
     */
    List<AlbumIndex> findAlbumsByCelebrity(CelebrityIndex index) throws NotFoundException;

    /**
     * Retrieves an album of the specified index.
     *
     * @param index must not be {@literal null}
     * @return the album
     * @throws NotFoundException if the album of the specified index is not found
     */
    Album findAlbum(AlbumIndex index) throws NotFoundException;
}
