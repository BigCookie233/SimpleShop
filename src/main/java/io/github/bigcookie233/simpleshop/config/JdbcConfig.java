package io.github.bigcookie233.simpleshop.config;

import io.github.bigcookie233.simpleshop.jdbc.JdbcDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcConfig {
    @Bean
    public JdbcDao jdbcDao(JdbcTemplate jdbcTemplate) {
        return new JdbcDao(jdbcTemplate);
    }
}
