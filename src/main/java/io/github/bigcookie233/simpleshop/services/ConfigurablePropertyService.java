package io.github.bigcookie233.simpleshop.services;

import io.github.bigcookie233.simpleshop.entities.ConfigurableProperty;
import io.github.bigcookie233.simpleshop.repositories.ConfigurablePropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurablePropertyService {
    private final ConfigurablePropertyRepository configurablePropertyRepository;

    @Autowired
    public ConfigurablePropertyService(ConfigurablePropertyRepository configurablePropertyRepository) {
        this.configurablePropertyRepository = configurablePropertyRepository;
    }

    public ConfigurableProperty findPropertyByName(String key) {
        return this.configurablePropertyRepository.findByName(key);
    }

    public void saveProduct(ConfigurableProperty property) {
        this.configurablePropertyRepository.save(property);
    }
}
