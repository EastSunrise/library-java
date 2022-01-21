package cn.wsg.repository.config.jackson;

import cn.wsg.commons.DatetimeConsts;
import cn.wsg.commons.Language;
import cn.wsg.commons.Region;
import cn.wsg.commons.internet.common.video.MovieGenre;
import cn.wsg.commons.jackson.EnumDeserializers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Configurations of Jackson.
 *
 * @author Kingen
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(
            new JavaTimeModule().addSerializer(new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
                .addSerializer(new LocalDateTimeSerializer(DatetimeConsts.DTF_YYYY_MM_DD_HH_MM_SS))).registerModule(
            new SimpleModule().addDeserializer(MovieGenre.class,
                EnumDeserializers.match(MovieGenre.class, (s, e) -> Objects.equals(s, e.getZhTitle())))
                .addDeserializer(Region.class, new RegionDeserializer())
                .addDeserializer(Language.class, new LanguageDeserializer()));
    }
}
