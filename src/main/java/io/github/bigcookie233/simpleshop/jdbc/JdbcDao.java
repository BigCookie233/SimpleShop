package io.github.bigcookie233.simpleshop.jdbc;

import io.github.bigcookie233.simpleshop.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.UUID;

public class JdbcDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Product getProduct(String uuid) {
        String sql = "select uuid, name, price from products where uuid=?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, uuid);
        rowSet.next();
        return new Product(UUID.fromString(uuid), rowSet.getString("name"), rowSet.getDouble("price"));
    }
}
