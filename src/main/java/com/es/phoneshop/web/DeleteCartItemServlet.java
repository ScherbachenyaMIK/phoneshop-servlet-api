package com.es.phoneshop.web;

import com.es.phoneshop.cart.Cart;
import com.es.phoneshop.cart.CartService;
import com.es.phoneshop.cart.DefaultCartService;
import com.es.phoneshop.common.Messages;
import com.es.phoneshop.util.ProductIdParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = ProductIdParser.parseProductId(request);

        Cart cart = cartService.getCart(request);
        cartService.delete(cart, id);

        response.sendRedirect(request.getContextPath()
                + "/cart?message="
                + String.format(Messages.PRODUCT_REMOVED_FROM_CART_SUCCESS, id)
        );
    }
}
