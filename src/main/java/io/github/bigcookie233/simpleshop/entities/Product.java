package io.github.bigcookie233.simpleshop.entities;

import java.util.UUID;

public class Product {
    public final UUID uuid;
    public final String name;
    public final double price;

    public Product(UUID uuid, String name, double price) {
        this.uuid = uuid;
        this.name = name;
        this.price = price;
    }
}
