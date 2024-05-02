package io.github.bigcookie233.simpleshop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ConfigurableProperty {
    @Id
    public String value;
    private String name;

    public ConfigurableProperty() {
    }

    public ConfigurableProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
