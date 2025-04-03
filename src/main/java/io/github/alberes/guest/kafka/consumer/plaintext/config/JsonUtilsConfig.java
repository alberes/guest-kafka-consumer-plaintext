package io.github.alberes.guest.kafka.consumer.plaintext.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.alberes.guest.kafka.consumer.plaintext.utils.JsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonUtilsConfig {

    @Bean
    public JsonUtils jsonUtils(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new JsonUtils(objectMapper);
    }
}
