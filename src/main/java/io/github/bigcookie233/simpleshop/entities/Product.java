package io.github.bigcookie233.simpleshop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String name;
    private double price;
    private String actionPayload;
    private String mcsmRemoteUuid;
    private String mcsmUuid;

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getActionPayload() {
        return actionPayload;
    }

    public String getMcsmRemoteUuid() {
        return mcsmRemoteUuid;
    }

    public String getMcsmUuid() {
        return mcsmUuid;
    }
}
