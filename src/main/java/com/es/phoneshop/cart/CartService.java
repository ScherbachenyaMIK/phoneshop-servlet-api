package com.es.phoneshop.cart;

public interface CartService {
    Cart getCart();

    void add(Long productId, int quantity) throws TooMuchQuantityException;
}
