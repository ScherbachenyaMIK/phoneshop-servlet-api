package com.es.phoneshop.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    @Mock
    private ArrayListProductDao arrayListProductDao;

    private final CartService cartService = DefaultCartService.getInstance();
    private final Long id = 1L;
    private final Product expected = new Product(1L,
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
    }

    @Test
    public void add() {
        int quantity = 2;

        cartService.add(id, 2);

        Cart result = cartService.getCart();

        assertEquals(1, result.getItems().size());
        assertEquals(expected, result.getItems().get(0).getProduct());
        assertEquals(quantity, result.getItems().get(0).getQuantity());
    }
}