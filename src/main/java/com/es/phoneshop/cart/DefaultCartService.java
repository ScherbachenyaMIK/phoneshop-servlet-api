package com.es.phoneshop.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import java.util.stream.IntStream;

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
    public void add(Long productId, int quantity) throws TooMuchQuantityException {
        int index = IntStream.range(0, cart.getItems().size())
                .filter(i -> productId.equals(
                        cart.getItems().get(i).getProduct().getId()
                ))
                .findAny()
                .orElse(-1);

        if (index == -1) {
            Product product = arrayListProductDao.getProduct(productId);
            if (product.getStock() < quantity) {
                throw new TooMuchQuantityException(product.getCode(), product.getStock(), quantity);
            }
            cart.getItems().add(new CartItem(product, quantity));
            return;
        }

        CartItem item = cart.getItems().get(index);

        if (item.getProduct().getStock() < item.getQuantity() + quantity) {
            throw new TooMuchQuantityException(
                    item.getProduct().getCode(),
                    item.getProduct().getStock(),
                    item.getQuantity() + quantity
            );
        }

        item.setQuantity(item.getQuantity() + quantity);
    }
}
