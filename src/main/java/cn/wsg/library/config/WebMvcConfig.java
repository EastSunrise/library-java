package cn.wsg.library.config;

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
        registry.addMapping("/api/library/**").allowedOrigins("http://localhost:3000").maxAge(3600).allowCredentials(true);
    }
}
