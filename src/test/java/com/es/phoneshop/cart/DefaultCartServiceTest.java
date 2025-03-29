package com.es.phoneshop.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultCartServiceTest {
    @Mock
    private ArrayListProductDao arrayListProductDao;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    private static final CartService cartService = DefaultCartService.getInstance();
    private static final Cart cart = new Cart();
    private static final Long id = 1L;
    private static final Product expected = new Product(1L,
            "sgs",
            "Samsung Galaxy S",
            new BigDecimal(100),
            Currency.getInstance("USD"),
            100,
            "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg",
            List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(95)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(110)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(105))));


    @Before
    public void setUp() throws Exception {
        Field field = DefaultCartService.class
                .getDeclaredField("arrayListProductDao");
        field.setAccessible(true);
        field.set(cartService, arrayListProductDao);

        when(arrayListProductDao.getProduct(id)).thenReturn(expected);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(cart);
    }

    @Test
    public void testAddNewOnce() throws TooMuchQuantityException {
        cartService.add(cart, id, 2);

        Cart result = cartService.getCart(request);

        assertEquals(1, result.getItems().size());
        assertEquals(expected, result.getItems().get(0).getProduct());
        assertEquals(2, result.getItems().get(0).getQuantity());
    }

    @Test
    public void testAddUpdateQuantity() throws TooMuchQuantityException {
        cartService.add(cart, id, 2);

        Cart result = cartService.getCart(request);

        assertEquals(1, result.getItems().size());
        assertEquals(expected, result.getItems().get(0).getProduct());
        assertEquals(4, result.getItems().get(0).getQuantity());
    }

    @Test(expected = RuntimeException.class)
    public void testAddNewBigQuantity() {
        try {
            cartService.add(cart, id, 1000);
        } catch (TooMuchQuantityException e) {
            assertEquals(
                    "For product sgs the stock is 100 but requested 1000",
                    e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testAddUpdateQuantityBig() {
        try {
            cartService.add(cart, id, 98);
        } catch (TooMuchQuantityException e) {
            assertEquals(
                    "For product sgs the stock is 100 but requested 102",
                    e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddUpdateQuantityDifferentSessions() throws TooMuchQuantityException {
        HttpServletRequest anotherRequest = mock(HttpServletRequest.class);
        HttpSession anotherSession = mock(HttpSession.class);
        when(anotherRequest.getSession()).thenReturn(anotherSession);
        when(anotherSession.getAttribute(anyString())).thenReturn(null);

        cartService.add(cart, id, 2);

        Cart result1 = cartService.getCart(request);
        Cart result2 = cartService.getCart(anotherRequest);

        assertEquals(1, result1.getItems().size());
        assertEquals(expected, result1.getItems().get(0).getProduct());
        assertEquals(6, result1.getItems().get(0).getQuantity());
        assertTrue(result2.getItems().isEmpty());
    }

    @Test
    public void testUpdateOnce() throws TooMuchQuantityException {
        cartService.update(cart, id, 2);

        Cart result = cartService.getCart(request);

        assertEquals(1, result.getItems().size());
        assertEquals(expected, result.getItems().get(0).getProduct());
        assertEquals(2, result.getItems().get(0).getQuantity());
    }

    @Test
    public void testUpdateTwice() throws TooMuchQuantityException {
        cartService.update(cart, id, 2);

        Cart result = cartService.getCart(request);

        assertEquals(1, result.getItems().size());
        assertEquals(expected, result.getItems().get(0).getProduct());
        assertEquals(2, result.getItems().get(0).getQuantity());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateBigQuantity() {
        try {
            cartService.update(cart, id, 1000);
        } catch (TooMuchQuantityException e) {
            assertEquals(
                    "For product sgs the stock is 100 but requested 1000",
                    e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUpdateQuantityDifferentSessions() throws TooMuchQuantityException {
        HttpServletRequest anotherRequest = mock(HttpServletRequest.class);
        HttpSession anotherSession = mock(HttpSession.class);
        when(anotherRequest.getSession()).thenReturn(anotherSession);
        when(anotherSession.getAttribute(anyString())).thenReturn(null);

        cartService.update(cart, id, 4);

        Cart result1 = cartService.getCart(request);
        Cart result2 = cartService.getCart(anotherRequest);

        assertEquals(1, result1.getItems().size());
        assertEquals(expected, result1.getItems().get(0).getProduct());
        assertEquals(4, result1.getItems().get(0).getQuantity());
        assertTrue(result2.getItems().isEmpty());
    }

    @Test
    public void testUpdateTwoThings() throws TooMuchQuantityException {
        Long secondId = 2L;
        Product secondExpected = new Product(2L,
                "sgs",
                "Samsung Galaxy S",
                new BigDecimal(100),
                Currency.getInstance("USD"),
                100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg",
                List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(95)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(110)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(105))));

        when(arrayListProductDao.getProduct(secondId)).thenReturn(secondExpected);

        cartService.update(cart, secondId, 8);

        Cart result = cartService.getCart(request);

        assertEquals(2, result.getItems().size());
        assertEquals(expected, result.getItems().get(0).getProduct());
        assertEquals(secondExpected, result.getItems().get(1).getProduct());
        assertEquals(2, result.getItems().get(0).getQuantity());
        assertEquals(8, result.getItems().get(1).getQuantity());
    }

    @Test
    public void testDeletionExistingProduct() throws TooMuchQuantityException {
        Long someId = 200L;
        Product someProduct = new Product(200L,
                "sgs",
                "Samsung Galaxy S",
                new BigDecimal(100),
                Currency.getInstance("USD"),
                100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg",
                List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(95)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(110)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(105))));

        when(arrayListProductDao.getProduct(someId)).thenReturn(someProduct);

        cartService.add(cart, someId, 8);

        int firstCartSize = cartService.getCart(request).getItems().size();

        cartService.delete(cart, someId);

        Cart result = cartService.getCart(request);

        assertEquals(firstCartSize - 1, result.getItems().size());
        assertFalse(result.getItems().stream().map(CartItem::getProduct).toList().contains(someProduct));
    }

    @Test
    public void testDeletionNotExistingProduct() {
        Long someId = 200L;

        int firstCartSize = cartService.getCart(request).getItems().size();

        cartService.delete(cart, someId);

        Cart result = cartService.getCart(request);

        assertEquals(firstCartSize, result.getItems().size());
    }
}