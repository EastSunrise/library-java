package cn.wsg.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Kingen
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Adds cors mappings to allow requests from the react app.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // tofix update to the real origins
        registry.addMapping("/api/library/**").allowedOrigins("http://localhost:3000").allowCredentials(true);
        registry.addMapping("/api/video/**").allowedOrigins("https://movie.douban.com").allowCredentials(true);
    }
}
