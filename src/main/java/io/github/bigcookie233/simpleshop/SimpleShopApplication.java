package io.github.bigcookie233.simpleshop;

import io.github.bigcookie233.simpleshop.config.ApiConfig;
import io.github.bigcookie233.simpleshop.config.JdbcConfig;
import io.github.bigcookie233.simpleshop.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Class[]{
				SimpleShopApplication.class,
				SecurityConfig.class,
				JdbcConfig.class,
				ApiConfig.class,
				ApiProperties.class
		}, args);
	}

}
