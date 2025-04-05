package com.es.phoneshop.web;

import com.es.phoneshop.common.Messages;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private static final String ORDER_ATTRIBUTE_NAME = "order";

    private static final String ERRORS_ATTRIBUTE_NAME = "errors";

    private static final String PAYMENT_METHODS_ATTRIBUTE_NAME = "paymentMethods";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/checkout.jsp";

    private CartService cartService;

    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        request.setAttribute(ORDER_ATTRIBUTE_NAME, order);
        request.setAttribute(PAYMENT_METHODS_ATTRIBUTE_NAME, orderService.getPaymentMethods());

        request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        Map<String, String> errors = new HashMap<>();
        setRequiredParameter(request, "firstName", errors, order::setFirstName);
        setRequiredParameter(request, "lastName", errors, order::setLastName);
        setRequiredParameter(request, "phone", errors, order::setPhone);
        setDeliveryDate(request, errors, order);
        setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress);
        setPaymentMethod(request, errors, order);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cart);
            response.sendRedirect(request.getContextPath()
                    + "/order/overview/"
                    + order.getSecureId()
            );
        } else {
            request.setAttribute(ERRORS_ATTRIBUTE_NAME, errors);
            request.setAttribute(ORDER_ATTRIBUTE_NAME, order);
            request.setAttribute(PAYMENT_METHODS_ATTRIBUTE_NAME, orderService.getPaymentMethods());
            request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
        }
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter,
                                      Map<String, String> errors, Consumer<String> consumer) {
        String value = request.getParameter(parameter);

        if (value == null || value.isEmpty()) {
            errors.put(parameter, Messages.REQUIRED_FIELD_IS_EMPTY);
        } else {
            consumer.accept(value);
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String parameter = "deliveryDate";
        String value = request.getParameter(parameter);

        if (value == null || value.isEmpty()) {
            errors.put(parameter, Messages.REQUIRED_FIELD_IS_EMPTY);
            return;
        }

        Locale locale = request.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDate(FormatStyle.SHORT)
                .withLocale(locale);

        try {
            LocalDate deliveryDate = LocalDate.parse(value, formatter);
            order.setDeliveryDate(deliveryDate);
        } catch (RuntimeException e) {
            errors.put(parameter, "Invalid date format." +
                    "Expected format: " + LocalDate.now().format(formatter));
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String parameter = "paymentMethod";
        String value = request.getParameter(parameter);

        if (value == null || value.isEmpty()) {
            errors.put(parameter, Messages.REQUIRED_FIELD_IS_EMPTY);
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
}
