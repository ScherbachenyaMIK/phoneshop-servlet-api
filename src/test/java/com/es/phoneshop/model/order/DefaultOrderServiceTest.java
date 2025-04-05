package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    @Mock
    private ArrayListOrderDao arrayListOrderDao;

    private Cart cart;

    private static final OrderService orderService = DefaultOrderService.getInstance();

    @Before
    public void setUp() throws Exception {
        Field field = DefaultOrderService.class
                .getDeclaredField("arrayListOrderDao");
        field.setAccessible(true);
        field.set(orderService, arrayListOrderDao);

        cart = createCart();
    }

    @Test
    public void testGetOrder() {
        Order order = orderService.getOrder(cart);

        assertEquals(cart.getTotalCost(), order.getSubtotal());
        assertEquals(cart.getTotalQuantity(), order.getTotalQuantity());
        assertEquals(BigDecimal.valueOf(cart.getTotalQuantity() * 5L), order.getDeliveryCost());
        assertEquals(
                cart.getTotalCost().add(BigDecimal.valueOf(cart.getTotalQuantity() * 5L)),
                order.getTotalCost());
    }

    @Test
    public void testPlaceOrder() {
        Order order = orderService.getOrder(cart);

        orderService.placeOrder(order);

        verify(arrayListOrderDao).save(order);
    }

    public static Cart createCart() {
        Cart cart = new Cart();

        Product product1 = new Product();
        product1.setId(1001L);
        product1.setCode("IP15P");
        product1.setDescription("iPhone 15 Pro");
        product1.setPrice(new BigDecimal("999.99"));
        product1.setCurrency(Currency.getInstance("USD"));
        product1.setStock(10);
        product1.setImageUrl("iphone_15_pro_image_url");

        Product product2 = new Product();
        product2.setId(1002L);
        product2.setCode("SGS24");
        product2.setDescription("Samsung Galaxy S24");
        product2.setPrice(new BigDecimal("899.99"));
        product2.setCurrency(Currency.getInstance("USD"));
        product2.setStock(5);
        product2.setImageUrl("samsung_galaxy_s24_image_url");

        CartItem item1 = new CartItem(product1, 2);
        CartItem item2 = new CartItem(product2, 1);

        cart.setItems(List.of(item1, item2));

        cart.setTotalQuantity(item1.getQuantity() + item2.getQuantity());
        cart.setTotalCost(product1.getPrice().multiply(BigDecimal.valueOf(item1.getQuantity()))
                .add(product2.getPrice().multiply(BigDecimal.valueOf(item2.getQuantity()))));

        return cart;
    }
}