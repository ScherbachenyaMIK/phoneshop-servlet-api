package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getById(Long id);

    List<Product> findProducts(String query, SortingField field, SortingOrder order);

    List<Product> findProductsAdvanced(String query, BigDecimal minPrice, BigDecimal maxPrice, SearchingCriteria criteria);

    void save(Product entity);

    void delete(Long id);
}
