package com.es.phoneshop.web;

import com.es.phoneshop.cart.Cart;
import com.es.phoneshop.cart.CartService;
import com.es.phoneshop.cart.TooMuchQuantityException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(cartService).getCart(request);
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("cart"), any());
    }

    @Test
    public void testDoPostSuccess() throws ServletException, IOException, TooMuchQuantityException {
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1", "2", "3"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "2", "3"});
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));

        servlet.doPost(request, response);

        verify(request, times(2)).getParameterValues(any());
        verify(cartService, times(3)).getCart(request);
        verify(cartService, times(3)).update(eq(cart), any(), anyInt());
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostHasErrors() throws ServletException, IOException, TooMuchQuantityException {
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1", "2", "3"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "eee", "3"});
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));

        servlet.doPost(request, response);

        verify(request, times(2)).getParameterValues(any());
        verify(cartService, times(4)).getCart(request);
        verify(cartService, times(2)).update(eq(cart), any(), anyInt());
        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("cart"), any());
        verify(requestDispatcher).forward(request, response);
    }
}