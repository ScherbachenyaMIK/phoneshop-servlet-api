package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.TooMuchQuantityException;
import com.es.phoneshop.model.history.RecentlyViewedService;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private ProductDao arrayListProductDao;
    @Mock
    private RecentlyViewedService recentlyViewedService;

    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    private final Product product = new Product(
            1L,
            "sgs",
            "Samsung Galaxy S",
            new BigDecimal(100),
            Currency.getInstance("USD"),
            1000,
            "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg",
            List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(95)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(110)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(105))));


    @Before
    public void setup() throws ServletException, NoSuchFieldException, IllegalAccessException {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("/1");
        when(cartService.getCart(request)).thenReturn(cart);
        when(arrayListProductDao.getById(1L)).thenReturn(product);
        when(recentlyViewedService.getRecentlyViewedProducts(request)).thenReturn(any());

        servlet.init(config);

        Field Dao = ProductDetailsPageServlet.class
                .getDeclaredField("arrayListProductDao");
        Dao.setAccessible(true);
        Dao.set(servlet, arrayListProductDao);
        Field service = ProductDetailsPageServlet.class
                .getDeclaredField("cartService");
        service.setAccessible(true);
        service.set(servlet, cartService);
        Field recentlyService = ProductDetailsPageServlet.class
                .getDeclaredField("recentlyViewedService");
        recentlyService.setAccessible(true);
        recentlyService.set(servlet, recentlyViewedService);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getPathInfo();
        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("100");

        servlet.doPost(request, response);

        verify(request).getPathInfo();
        verify(request).getParameter("quantity");
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostUsLocale() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getParameter("quantity")).thenReturn("1,000");

        servlet.doPost(request, response);

        verify(request).getPathInfo();
        verify(request).getParameter("quantity");
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostBadStringQuantity() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("aaa");

        servlet.doPost(request, response);

        verify(request, times(2)).getPathInfo();
        verify(request).getParameter("quantity");
        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostBadNumericQuantity() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("1 000");

        servlet.doPost(request, response);

        verify(request, times(2)).getPathInfo();
        verify(request).getParameter("quantity");
        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostBadNumericQuantity2() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getParameter("quantity")).thenReturn("1.001");

        servlet.doPost(request, response);

        verify(request, times(2)).getPathInfo();
        verify(request).getParameter("quantity");
        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostBadNumericQuantity3() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("-1");

        servlet.doPost(request, response);

        verify(request, times(2)).getPathInfo();
        verify(request).getParameter("quantity");
        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostEmptyQuantity() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("");

        servlet.doPost(request, response);

        verify(request, times(2)).getPathInfo();
        verify(request).getParameter("quantity");
        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostTooMuchQuantity() throws ServletException, IOException, TooMuchQuantityException {
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("1001");
        doThrow(new TooMuchQuantityException("sgs", 1000, 1001))
                .when(cartService).add(cart,1L, 1001);

        servlet.doPost(request, response);

        verify(request, times(2)).getPathInfo();
        verify(request).getParameter("quantity");
        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }
}