package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.TooMuchQuantityException;
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
public class ProductListPageServletTest {
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

    private final ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup() throws Exception {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        servlet.init(config);

        Field service = ProductListPageServlet.class
                .getDeclaredField("cartService");
        service.setAccessible(true);
        service.set(servlet, cartService);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request, times(3)).getParameter(anyString());
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), any());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("2");
        when(cartService.getCart(request)).thenReturn(cart);

        servlet.doPost(request, response);

        verify(request).getParameter("productId");
        verify(request).getParameter("quantity");
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostBadQuantity() throws ServletException, IOException {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("eee");

        servlet.doPost(request, response);

        verify(request).getParameter("productId");
        verify(request).getParameter("quantity");
        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("id"), any());
        verify(request).setAttribute(eq("products"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostBigQuantity() throws ServletException, IOException, TooMuchQuantityException {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));
        when(request.getParameter("quantity")).thenReturn("1001");
        when(cartService.getCart(request)).thenReturn(cart);
        doThrow(new TooMuchQuantityException("sgs", 1000, 1001))
                .when(cartService).add(cart,1L, 1001);

        servlet.doPost(request, response);

        verify(request).getParameter("productId");
        verify(request).getParameter("quantity");
        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("id"), any());
        verify(request).setAttribute(eq("products"), any());
        verify(requestDispatcher).forward(request, response);
    }
}