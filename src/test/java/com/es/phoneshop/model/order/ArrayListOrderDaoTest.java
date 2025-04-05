package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayListOrderDaoTest {
    private OrderDao orderDao;

    private Order order1;

    private Order order2;

    @Before
    public void setUp() {
        orderDao = ArrayListOrderDao.getInstance();
        order1 = createFirstOrder();
        order2 = createSecondOrder();
    }

    @Test
    public void testGetOrderOnce() {
        Long id = 1L;

        orderDao.save(order1);

        Order result = orderDao.getById(id);

        assertEquals(id, result.getId());
        assertEquals(order1.getFirstName(), result.getFirstName());
        assertEquals(order1.getLastName(), result.getLastName());
        assertEquals(order1.getPhone(), result.getPhone());
    }

    @Test
    public void testGetOrderTwice() {
        Long id1 = 1L;
        Long id2 = 2L;

        orderDao.save(order2);

        Order result1 = orderDao.getById(id1);
        Order result2 = orderDao.getById(id2);

        assertEquals(id1, result1.getId());
        assertEquals(order1.getSubtotal(), result1.getSubtotal());
        assertEquals(order1.getDeliveryCost(), result1.getDeliveryCost());
        assertEquals(order1.getTotalCost(), result1.getTotalCost());
        assertEquals(id2, result2.getId());
        assertEquals(order2.getFirstName(), result2.getFirstName());
        assertEquals(order2.getLastName(), result2.getLastName());
        assertEquals(order2.getPhone(), result2.getPhone());
        assertEquals(order2.getSubtotal(), result2.getSubtotal());
        assertEquals(order2.getDeliveryCost(), result2.getDeliveryCost());
        assertEquals(order2.getTotalCost(), result2.getTotalCost());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOrderTwiceNotFound() {
        Long id = 3L;

        orderDao.getById(id);
    }

    public static Order createFirstOrder() {
        Order order = new Order();
        order.setFirstName("John");
        order.setLastName("Doe");
        order.setPhone("+1234567890");
        order.setDeliveryAddress("123 Main Street, New York, NY");
        order.setDeliveryDate(LocalDate.of(2025, 4, 15));
        order.setPaymentMethod(PaymentMethod.credit_card);

        Product product1 = new Product();
        product1.setId(1001L);
        product1.setDescription("iPhone 15 Pro");
        product1.setPrice(new BigDecimal("999.99"));
        product1.setStock(10);

        CartItem item1 = new CartItem(product1, 2);
        order.setItems(List.of(item1));
        order.setTotalQuantity(2);
        order.setTotalCost(product1.getPrice().multiply(BigDecimal.valueOf(2)));

        order.setSubtotal(order.getTotalCost());
        order.setDeliveryCost(new BigDecimal("20.00"));

        return order;
    }

    public static Order createSecondOrder() {
        Order order = new Order();
        order.setFirstName("Jane");
        order.setLastName("Smith");
        order.setPhone("+9876543210");
        order.setDeliveryAddress("456 Elm Street, Los Angeles, CA");
        order.setDeliveryDate(LocalDate.of(2025, 5, 10));
        order.setPaymentMethod(PaymentMethod.cash);

        Product product2 = new Product();
        product2.setId(2002L);
        product2.setDescription("Samsung Galaxy S24");
        product2.setPrice(new BigDecimal("899.99"));
        product2.setStock(5);

        CartItem item2 = new CartItem(product2, 1);
        order.setItems(List.of(item2));
        order.setTotalQuantity(1);
        order.setTotalCost(product2.getPrice());

        order.setSubtotal(order.getTotalCost());
        order.setDeliveryCost(new BigDecimal("15.00"));

        return order;
    }
}