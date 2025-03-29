package com.es.phoneshop.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private final ProductDao arrayListProductDao;

    private final ReadWriteLock lock;

    private static final class DefaultCartServiceHolder {
        private static final DefaultCartService instance = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.DefaultCartServiceHolder.instance;
    }

    private DefaultCartService() {
        arrayListProductDao = ArrayListProductDao.getInstance();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        lock.readLock().lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);

            if (cart == null) {
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
                    if (cart == null) {
                        request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
                    }
                } finally {
                    lock.writeLock().unlock();
                    lock.readLock().lock();
                }
            }

            return cart;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws TooMuchQuantityException {
        lock.writeLock().lock();

        try {
            CartItem item = getItemById(cart, productId);

            if (item == null) {
                addNewItemToCart(cart, productId, quantity);
                return;
            }

            if (item.getProduct().getStock() < item.getQuantity() + quantity) {
                throw new TooMuchQuantityException(
                        item.getProduct().getCode(),
                        item.getProduct().getStock(),
                        item.getQuantity() + quantity
                );
            }

            item.setQuantity(item.getQuantity() + quantity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws TooMuchQuantityException {
        lock.writeLock().lock();

        try {
            CartItem item = getItemById(cart, productId);

            if (item == null) {
                addNewItemToCart(cart, productId, quantity);
                return;
            }

            if (item.getProduct().getStock() < quantity) {
                throw new TooMuchQuantityException(
                        item.getProduct().getCode(),
                        item.getProduct().getStock(),
                        quantity
                );
            }

            item.setQuantity(quantity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        lock.writeLock().lock();

        try {
            cart.getItems().removeIf(item -> productId.equals(item.getProduct().getId()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    private CartItem getItemById(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(i -> productId.equals(i.getProduct().getId()))
                .findAny()
                .orElse(null);
    }

    private void addNewItemToCart(Cart cart, Long productId, int quantity) throws TooMuchQuantityException {
        Product product = arrayListProductDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new TooMuchQuantityException(
                    product.getCode(),
                    product.getStock(),
                    quantity
            );
        }
        cart.getItems().add(new CartItem(product, quantity));
    }
}
