package com.es.phoneshop.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

public class DefaultCartService implements CartService {
    private final Cart cart;

    private final ProductDao arrayListProductDao;

    private static final class DefaultCartServiceHolder {
        private static final DefaultCartService instance = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.DefaultCartServiceHolder.instance;
    }

    private DefaultCartService() {
        cart = new Cart();
        arrayListProductDao = ArrayListProductDao.getInstance();
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) {
        Product product = arrayListProductDao.getProduct(productId);

        cart.getItems().add(new CartItem(product, quantity));
    }
}
