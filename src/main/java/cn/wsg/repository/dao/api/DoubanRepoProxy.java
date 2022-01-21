package cn.wsg.repository.dao.api;

import cn.wsg.commons.internet.page.Page;
import cn.wsg.commons.internet.page.PageIndex;
import cn.wsg.commons.internet.support.LoginException;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.repository.com.douban.*;
import cn.wsg.repository.dao.mapper.video.VideoIdRelationMapper;
import cn.wsg.repository.entity.video.VideoIdRelationDO;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.util.List;

/**
 * The proxy of {@link DoubanRepository}.
 *
 * @author Kingen
 */
@Component
public class DoubanRepoProxy implements DoubanRepository, DisposableBean {

    private final DoubanRepository repository;
    private final VideoIdRelationMapper relationMapper;

    public DoubanRepoProxy(VideoIdRelationMapper relationMapper) {
        this.repository = new DoubanRepositoryImpl();
        this.relationMapper = relationMapper;
    }

    @Override
    public DoubanVideo findVideoById(long id) throws NotFoundException {
        DoubanVideo video = repository.findVideoById(id);
        if (video.getImdbId() != null) {
            saveIdRelation(id, video.getImdbId());
        }
        return video;
    }

    @Override
    public long getDbIdByImdbId(String imdbId) throws NotFoundException, LoginException {
        VideoIdRelationDO relationDO = relationMapper.getDoubanIdByImdbId(imdbId);
        if (relationDO != null) {
            return relationDO.getDoubanId();
        }
        return saveIdRelation(repository.getDbIdByImdbId(imdbId), imdbId);
    }

    @Override
    public Long user() {
        return repository.user();
    }

    @Override
    public void login(String username, String password) throws LoginException {
        repository.login(username, password);
    }

    @Override
    public void logout() {
        repository.logout();
    }

    @Override
    public Page<SubjectIndex> searchGlobally(String keyword, PageIndex page, DoubanCatalog catalog)
        throws SearchException {
        return repository.searchGlobally(keyword, page, catalog);
    }

    @Override
    public List<SubjectIndex> search(DoubanCatalog catalog, String keyword) {
        return repository.search(catalog, keyword);
    }

    @Override
    public Page<RankedSubject> top250(PageIndex pageIndex) {
        return repository.top250(pageIndex);
    }

    @Override
    public Page<MarkedSubject> findUserSubjects(DoubanCatalog catalog, long userId, MarkedStatus status, PageIndex page)
        throws NotFoundException {
        return repository.findUserSubjects(catalog, userId, status, page);
    }

    @Override
    public Page<PersonIndex> findUserCreators(DoubanCatalog catalog, long userId, PageIndex page)
        throws NotFoundException {
        return repository.findUserCreators(catalog, userId, page);
    }

    @Override
    public DoubanBook findBookById(long id) throws NotFoundException {
        return repository.findBookById(id);
    }

    @Override
    public void destroy() throws Exception {
        if (repository instanceof Closeable) {
            ((Closeable)repository).close();
        }
    }

    private long saveIdRelation(long doubanId, String imdbId) {
        VideoIdRelationDO relation = new VideoIdRelationDO();
        relation.setDoubanId(doubanId);
        relation.setImdbId(imdbId);
        relationMapper.insertIgnore(relation);
        return doubanId;
    }
}
