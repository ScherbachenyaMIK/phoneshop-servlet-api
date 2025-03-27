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
public class CartPageServletTest {
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

    private final CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setUp() throws Exception {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(cartService.getCart(request)).thenReturn(cart);

        servlet.init(config);

        Field service = CartPageServlet.class
                .getDeclaredField("cartService");
        service.setAccessible(true);
        service.set(servlet, cartService);
    }

    @Test
    public void doGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(cartService).getCart(request);
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("cart"), any());
    }
}