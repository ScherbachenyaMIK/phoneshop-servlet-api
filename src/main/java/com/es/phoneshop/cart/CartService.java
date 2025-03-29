package com.es.phoneshop.cart;

import jakarta.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    void add(Cart cart, Long productId, int quantity) throws TooMuchQuantityException;

    void update(Cart cart, Long productId, int quantity) throws TooMuchQuantityException;
}
