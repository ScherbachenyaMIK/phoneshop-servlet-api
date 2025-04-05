package com.es.phoneshop.model.order;

public interface OrderDao {
    Order getById(Long id);

    Order getBySecureId(String id);

    void save(Order order);
}
