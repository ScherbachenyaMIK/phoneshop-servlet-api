package com.es.phoneshop.model.product;

import java.util.List;

public interface ProductDao {
    Product getById(Long id);

    List<Product> findProducts(String query, SortingField field, SortingOrder order);

    void save(Product entity);

    void delete(Long id);
}
