package cn.wsg.repository.dao.api;

import cn.wsg.commons.internet.com.douban.*;
import cn.wsg.commons.internet.page.Page;
import cn.wsg.commons.internet.page.PageIndex;
import cn.wsg.commons.internet.support.LoginException;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.internet.support.OtherResponseException;
import cn.wsg.repository.dao.mapper.video.VideoIdRelationMapper;
import cn.wsg.repository.entity.video.VideoIdRelationDO;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.util.List;

/**
 * Adapter of {@link DoubanRepository}.
 *
 * @author Kingen
 */
@Component
public class DoubanRepoAdapter implements DoubanRepository, DisposableBean {

    private final DoubanRepository repository;
    private final VideoIdRelationMapper relationMapper;

    public DoubanRepoAdapter(VideoIdRelationMapper relationMapper) {
        this.repository = new DoubanRepositoryImpl();
        this.relationMapper = relationMapper;
    }

    @Override
    public DoubanVideo findVideoById(long id) throws NotFoundException, OtherResponseException {
        DoubanVideo video = repository.findVideoById(id);
        if (video.getImdbId() != null) {
            saveIdRelation(id, video.getImdbId());
        }
        return video;
    }

    @Override
    public long getDbIdByImdbId(String imdbId) throws NotFoundException, OtherResponseException, LoginException {
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
    public void login(String username, String password) throws OtherResponseException, LoginException {
        repository.login(username, password);
    }

    @Override
    public void logout() throws OtherResponseException {
        repository.logout();
    }

    @Override
    public Page<SubjectIndex> searchGlobally(String keyword, PageIndex page, DoubanCatalog catalog) throws OtherResponseException {
        return repository.searchGlobally(keyword, page, catalog);
    }

    @Override
    public List<SubjectIndex> search(DoubanCatalog catalog, String keyword) throws OtherResponseException {
        return repository.search(catalog, keyword);
    }

    @Override
    public Page<RankedSubject> top250(PageIndex pageIndex) throws OtherResponseException {
        return repository.top250(pageIndex);
    }

    @Override
    public Page<MarkedSubject> findUserSubjects(DoubanCatalog catalog, long userId, MarkedStatus status, PageIndex page)
        throws NotFoundException, OtherResponseException {
        return repository.findUserSubjects(catalog, userId, status, page);
    }

    @Override
    public Page<PersonIndex> findUserCreators(DoubanCatalog catalog, long userId, PageIndex page) throws NotFoundException, OtherResponseException {
        return repository.findUserCreators(catalog, userId, page);
    }

    @Override
    public DoubanBook findBookById(long id) throws NotFoundException, OtherResponseException {
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
