package com.es.phoneshop.web.servlet;

import com.es.phoneshop.common.Messages;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.TooMuchQuantityException;
import com.es.phoneshop.util.QuantityParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String CART_ATTRIBUTE_NAME = "cart";

    private static final String ERROR_ATTRIBUTE_NAME = "errors";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/cart.jsp";

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);

        request.setAttribute(CART_ATTRIBUTE_NAME, cart);

        request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        if (productIds != null) {
            Locale locale = request.getLocale();
            Cart cart = cartService.getCart(request);
            Map<Long, String> errors = new HashMap<>();
            for (int i = 0; i < productIds.length; ++i) {
                processProductUpdate(
                        Long.valueOf(productIds[i]),
                        cart,
                        locale,
                        quantities[i].trim(),
                        errors
                );
            }

            if (errors.isEmpty()) {
                response.sendRedirect(request.getRequestURI()
                        + "?message="
                        + Messages.CART_UPDATE_SUCCESS
                );
            } else {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, errors);
                doGet(request, response);
            }
        } else {
            response.sendRedirect(request.getRequestURI());
        }
    }

    private void processProductUpdate(Long productId, Cart cart, Locale locale,
                                      String quantityStr, Map<Long, String> errors) {
        int quantity;
        try {
            quantity = QuantityParser.parseQuantity(
                    quantityStr,
                    locale
            );
            cartService.update(cart, productId, quantity);
        } catch (IllegalArgumentException | TooMuchQuantityException e) {
            errors.put(productId, e.getMessage());
        }
    }
}
