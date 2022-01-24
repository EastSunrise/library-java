package cn.wsg.repository.resource;

import cn.wsg.commons.internet.repository.ListRepository;
import cn.wsg.commons.internet.repository.RepoRetrievable;

/**
 * The interface that represents a repository of resources.
 *
 * @author Kingen
 */
public interface VideoResourceRepository<T> extends RepoRetrievable<Integer, T> {

    /**
     * Returns the repository that contains all items of current site.
     *
     * @return the list repository
     */
    ListRepository<Integer, T> getRepository();
}
