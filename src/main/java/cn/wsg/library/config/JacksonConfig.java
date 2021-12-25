package cn.wsg.library.config;

import cn.wsg.commons.lang.DatetimeConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
        return new ObjectMapper()
            .registerModule(new JavaTimeModule()
                .addSerializer(new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
                .addSerializer(new LocalDateTimeSerializer(DatetimeConsts.DTF_YYYY_MM_DD_HH_MM_SS))
            );
    }
}
