package com.es.phoneshop.web.servlet;

import com.es.phoneshop.common.Messages;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.util.IdParser;
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
        Long id = IdParser.parseId(request);

        Cart cart = cartService.getCart(request);
        cartService.delete(cart, id);

        response.sendRedirect(request.getContextPath()
                + "/cart?message="
                + String.format(Messages.PRODUCT_REMOVED_FROM_CART_SUCCESS, id)
        );
    }
}
