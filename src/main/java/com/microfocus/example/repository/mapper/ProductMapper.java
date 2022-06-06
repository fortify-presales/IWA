package com.microfocus.example.repository.mapper;

import com.microfocus.example.entity.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductMapper implements RowMapper<Product> {
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(UUID.fromString(rs.getString("id")));
        product.setCode(rs.getString("code"));
        product.setName(rs.getString("name"));
        product.setRating(rs.getInt("rating"));
        product.setSummary(rs.getString(("summary")));
        product.setDescription(rs.getString("description"));
        product.setImage(rs.getString("image"));
        product.setPrice(rs.getFloat("price"));
        product.setOnSale(rs.getBoolean("on_sale"));
        product.setSalePrice(rs.getFloat("sale_price"));
        product.setInStock(rs.getBoolean("in_stock"));
        product.setTimeToStock(rs.getInt("time_to_stock"));
        product.setAvailable(rs.getBoolean("available"));
        return product;
    }
}
