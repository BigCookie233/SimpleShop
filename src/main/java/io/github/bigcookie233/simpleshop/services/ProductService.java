package io.github.bigcookie233.simpleshop.services;

import io.github.bigcookie233.simpleshop.entities.Product;
import io.github.bigcookie233.simpleshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 根据 UUID 查找商品
    public Product findProductByUuid(UUID uuid) {
        return productRepository.findByUuid(uuid);
    }

    // 保存商品到数据库
    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}
