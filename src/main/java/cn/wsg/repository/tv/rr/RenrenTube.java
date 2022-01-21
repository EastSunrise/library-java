package cn.wsg.repository.tv.rr;

import cn.wsg.commons.internet.repository.RepoRetrievable;
import cn.wsg.commons.internet.support.NotFoundException;

import java.util.List;

/**
 * @author Kingen
 * @see <a href="http://m.rr.tv/">Renren Tube</a>
 */
public interface RenrenTube extends RepoRetrievable<Integer, RenrenVideo> {

    /**
     * Retrieves videos by page.
     *
     * @param req the request with optional arguments
     * @return videos in page
     * @throws NotFoundException if the target page is not found
     */
    RenrenPage findAll(RenrenReq req) throws NotFoundException;

    /**
     * Searches items by the specified keyword.
     *
     * @param keyword the keyword to search
     * @param size    the size of returned items. It's required to be positive.
     * @return list of searched items
     */
    List<SearchedItem> search(String keyword, int size);
}
