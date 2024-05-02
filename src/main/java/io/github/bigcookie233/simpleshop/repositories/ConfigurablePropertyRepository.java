package io.github.bigcookie233.simpleshop.repositories;

import io.github.bigcookie233.simpleshop.entities.ConfigurableProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurablePropertyRepository extends JpaRepository<ConfigurableProperty, String> {
    ConfigurableProperty findByName(String key);
}
