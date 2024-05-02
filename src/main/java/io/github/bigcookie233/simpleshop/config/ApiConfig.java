package io.github.bigcookie233.simpleshop.config;

import io.github.bigcookie233.simpleshop.ApiAdapter;
import io.github.bigcookie233.simpleshop.services.ConfigurablePropertyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Bean
    public ApiAdapter apiAdapter(ConfigurablePropertyService configurablePropertyService) {
        return new ApiAdapter(configurablePropertyService);
    }
}
