package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultRecentlyViewedServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    private static final Product product1 = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), Currency.getInstance("USD"), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(95)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(110)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(105))));
    private static final Product product2 = new Product(2L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), Currency.getInstance("USD"), 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(195)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(210)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(205))));
    private static final Product product3 = new Product(3L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), Currency.getInstance("USD"), 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(295)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(310)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(305))));
    private static final Product product4 = new Product(4L, "iphone", "Apple iPhone", new BigDecimal(200), Currency.getInstance("USD"), 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(295)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(250)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(225))));
    private static List<Product> recentlyViewedProducts = new LinkedList<>();

    private static final RecentlyViewedService recentlyViewedService =
            DefaultRecentlyViewedService.getInstance();

    @Before
    public void setUp() {
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetRecentlyViewedProducts1() {
        when(session.getAttribute("recentlyViewed")).thenReturn(null);

        List<Product> recentlyViewedProducts =
                recentlyViewedService.getRecentlyViewedProducts(request);

        assertTrue(recentlyViewedProducts.isEmpty());
    }

    @Test
    public void testGetRecentlyViewedProducts2() {
        when(session.getAttribute("recentlyViewed")).thenReturn(List.of(product1, product2));

        List<Product> recentlyViewedProducts =
                recentlyViewedService.getRecentlyViewedProducts(request);

        assertFalse(recentlyViewedProducts.isEmpty());
    }

    @Test
    public void testAddRecentlyViewedProducts1() {
        when(session.getAttribute("recentlyViewed")).thenReturn(recentlyViewedProducts);

        recentlyViewedService.addToRecentlyViewed(recentlyViewedProducts, product1);
        recentlyViewedProducts =
                recentlyViewedService.getRecentlyViewedProducts(request);

        assertEquals(1, recentlyViewedProducts.size());
        assertEquals(product1, recentlyViewedProducts.get(0));
    }

    @Test
    public void testAddRecentlyViewedProducts2() {
        when(session.getAttribute("recentlyViewed")).thenReturn(recentlyViewedProducts);

        recentlyViewedService.addToRecentlyViewed(recentlyViewedProducts, product2);
        recentlyViewedProducts =
                recentlyViewedService.getRecentlyViewedProducts(request);

        assertEquals(2, recentlyViewedProducts.size());
        assertEquals(product2, recentlyViewedProducts.get(0));
        assertEquals(product1, recentlyViewedProducts.get(1));
    }

    @Test
    public void testAddRecentlyViewedProducts3() {
        when(session.getAttribute("recentlyViewed")).thenReturn(recentlyViewedProducts);

        recentlyViewedService.addToRecentlyViewed(recentlyViewedProducts, product3);
        recentlyViewedProducts =
                recentlyViewedService.getRecentlyViewedProducts(request);

        assertEquals(3, recentlyViewedProducts.size());
        assertEquals(product3, recentlyViewedProducts.get(0));
        assertEquals(product2, recentlyViewedProducts.get(1));
        assertEquals(product1, recentlyViewedProducts.get(2));
    }

    @Test
    public void testAddRecentlyViewedProducts4() {
        when(session.getAttribute("recentlyViewed")).thenReturn(recentlyViewedProducts);

        recentlyViewedService.addToRecentlyViewed(recentlyViewedProducts, product4);
        recentlyViewedProducts =
                recentlyViewedService.getRecentlyViewedProducts(request);

        assertEquals(3, recentlyViewedProducts.size());
        assertEquals(product4, recentlyViewedProducts.get(0));
        assertEquals(product3, recentlyViewedProducts.get(1));
        assertEquals(product2, recentlyViewedProducts.get(2));
    }

    @Test
    public void testAddRecentlyViewedProducts5() {
        when(session.getAttribute("recentlyViewed")).thenReturn(recentlyViewedProducts);

        recentlyViewedService.addToRecentlyViewed(recentlyViewedProducts, product2);
        recentlyViewedProducts =
                recentlyViewedService.getRecentlyViewedProducts(request);

        assertEquals(3, recentlyViewedProducts.size());
        assertEquals(product2, recentlyViewedProducts.get(0));
        assertEquals(product4, recentlyViewedProducts.get(1));
        assertEquals(product3, recentlyViewedProducts.get(2));
    }
}