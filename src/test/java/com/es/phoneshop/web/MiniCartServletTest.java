package com.es.phoneshop.web;

import com.es.phoneshop.cart.Cart;
import com.es.phoneshop.cart.CartService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {
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

    private final MiniCartServlet servlet = new MiniCartServlet();

    @Before
    public void setUp() throws Exception {
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        servlet.init(config);

        Field service = MiniCartServlet.class
                .getDeclaredField("cartService");
        service.setAccessible(true);
        service.set(servlet, cartService);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(cartService).getCart(request);
        verify(request).setAttribute(eq("cart"), any());
        verify(requestDispatcher).include(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(cartService).getCart(request);
        verify(request).setAttribute(eq("cart"), any());
        verify(requestDispatcher).include(request, response);
    }
}