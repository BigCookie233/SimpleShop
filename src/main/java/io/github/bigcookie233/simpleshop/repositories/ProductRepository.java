package io.github.bigcookie233.simpleshop.repositories;

import io.github.bigcookie233.simpleshop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    // 自定义查询方法：根据商品的 UUID 查找商品
    Product findByUuid(UUID uuid);
}
