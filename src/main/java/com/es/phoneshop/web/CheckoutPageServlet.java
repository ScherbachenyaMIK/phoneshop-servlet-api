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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.maven.shared.utils.StringUtils;

public class CheckoutPageServlet extends HttpServlet {
    private static final String ORDER_ATTRIBUTE_NAME = "order";

    private static final String ERRORS_ATTRIBUTE_NAME = "errors";

    private static final String PAYMENT_METHODS_ATTRIBUTE_NAME = "paymentMethods";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/checkout.jsp";

    private static final String INVALID_DATE_MESSAGE = "Delivery date must be later than current date";

    private static final String INVALID_PHONE_NUMBER_MESSAGE = "Invalid phone number";

    private static final String PHONE_NUMBER_PATTERN = "\\+\\d{12}";

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
        setPhone(request, errors, order);
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

        if (StringUtils.isBlank(value)) {
            errors.put(parameter, Messages.REQUIRED_FIELD_IS_EMPTY);
        } else {
            consumer.accept(value.trim());
        }
    }

    private void setPhone(HttpServletRequest request, Map<String, String> errors, Order order) {
        String parameter = "phone";
        String value = request.getParameter(parameter);

        if (StringUtils.isBlank(value)) {
            errors.put(parameter, Messages.REQUIRED_FIELD_IS_EMPTY);
            return;
        }

        value = value.trim();

        if (value.matches(PHONE_NUMBER_PATTERN)) {
            order.setPhone(value);
        } else {
            errors.put(parameter, INVALID_PHONE_NUMBER_MESSAGE);
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String parameter = "deliveryDate";
        String value = request.getParameter(parameter);

        if (StringUtils.isBlank(value)) {
            errors.put(parameter, Messages.REQUIRED_FIELD_IS_EMPTY);
            return;
        }

        LocalDate deliveryDate = LocalDate.parse(value.trim());

        if (LocalDate.now().isBefore(deliveryDate)) {
            order.setDeliveryDate(deliveryDate);
        } else {
            errors.put(parameter, INVALID_DATE_MESSAGE);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String parameter = "paymentMethod";
        String value = request.getParameter(parameter);

        if (StringUtils.isBlank(value)) {
            errors.put(parameter, Messages.REQUIRED_FIELD_IS_EMPTY);
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value.trim()));
        }
    }
}
