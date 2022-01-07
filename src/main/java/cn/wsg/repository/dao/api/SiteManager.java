package cn.wsg.repository.dao.api;

import cn.wsg.commons.internet.com.imdb.ImdbRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Management of all sites.
 *
 * @author Kingen
 */
@Configuration
public class SiteManager {

    @Bean(destroyMethod = "close")
    public ImdbRepositoryImpl imdbRepository() {
        return new ImdbRepositoryImpl();
    }
}
