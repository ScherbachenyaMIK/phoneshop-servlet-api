package com.es.phoneshop.web;

import com.es.phoneshop.cart.Cart;
import com.es.phoneshop.cart.CartService;
import com.es.phoneshop.cart.DefaultCartService;
import com.es.phoneshop.cart.TooMuchQuantityException;
import com.es.phoneshop.util.QuantityParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);

        request.setAttribute("cart", cart);

        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; ++i) {
            Long productId = Long.valueOf(productIds[i]);

            int quantity;
            Cart cart = cartService.getCart(request);
            try {
                quantity = QuantityParser.parseQuantity(
                        quantities[i].trim(),
                        request.getLocale()
                );
                cartService.update(cart, productId, quantity);
            } catch (IllegalArgumentException | TooMuchQuantityException e) {
                errors.put(productId, e.getMessage());
            }
        }

        if (errors.isEmpty()) {
            String message = "Cart updated successfully";
            response.sendRedirect(request.getRequestURI() + "?message=" + message);
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
