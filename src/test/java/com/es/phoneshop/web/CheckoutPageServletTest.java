package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
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
    private OrderService orderService;
    @Mock
    private Cart cart;

    private final Order order = new Order();
    private final CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setUp() throws Exception {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(cartService.getCart(request)).thenReturn(cart);
        when(orderService.getOrder(cart)).thenReturn(order);

        servlet.init(config);

        Field cartServiceField = CheckoutPageServlet.class
                .getDeclaredField("cartService");
        cartServiceField.setAccessible(true);
        cartServiceField.set(servlet, cartService);
        Field orderServiceField = CheckoutPageServlet.class
                .getDeclaredField("orderService");
        orderServiceField.setAccessible(true);
        orderServiceField.set(servlet, orderService);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostSuccess() throws ServletException, IOException {
        when(request.getParameter(anyString())).thenReturn(
                "fname",
                "sname",
                "phone",
                "08.12.2025",
                "address",
                "cash"
        );
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));

        servlet.doPost(request, response);

        verify(orderService).placeOrder(order);
        verify(cartService).clear(cart);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostSuccessOtherLocale() throws ServletException, IOException {
        when(request.getParameter(anyString())).thenReturn(
                "fname",
                "sname",
                "phone",
                "08/12/2025",
                "address",
                "cash"
        );
        when(request.getLocale()).thenReturn(Locale.UK);

        servlet.doPost(request, response);

        verify(orderService).placeOrder(order);
        verify(cartService).clear(cart);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostSomeFieldsNotSet() throws ServletException, IOException {
        when(request.getParameter(anyString())).thenReturn(
                "",
                null,
                "phone",
                "",
                "address",
                null
        );

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostIncorrectDate() throws ServletException, IOException {
        when(request.getParameter(anyString())).thenReturn(
                "fname",
                "sname",
                "phone",
                "08/12/2025",
                "address",
                "cash"
        );
        when(request.getLocale()).thenReturn(new Locale("ru", "RU"));

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }
}