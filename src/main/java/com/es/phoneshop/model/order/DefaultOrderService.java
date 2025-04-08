package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private final OrderDao arrayListOrderDao;

    private static final class DefaultOrderServiceHolder {
        private static final DefaultOrderService instance = new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return DefaultOrderService.DefaultOrderServiceHolder.instance;
    }

    private DefaultOrderService() {
        arrayListOrderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();

        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return (CartItem) item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost(cart.getTotalQuantity()));
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        order.setTotalQuantity(cart.getTotalQuantity());

        return order;
    }

    private BigDecimal calculateDeliveryCost(int totalQuantity) {
        return BigDecimal.valueOf(5L * totalQuantity);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        arrayListOrderDao.save(order);
    }
}