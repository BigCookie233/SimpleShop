package io.github.bigcookie233.simpleshop.config;

import io.github.bigcookie233.simpleshop.ApiAdapter;
import io.github.bigcookie233.simpleshop.ApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApiAdapter apiAdapter(RestTemplate restTemplate, ApiProperties apiProperties) {
        return new ApiAdapter(restTemplate, apiProperties);
    }
}
