package com.es.phoneshop.model.order;

public interface OrderDao {
    Order getById(Long id);

    void save(Order order);
}
