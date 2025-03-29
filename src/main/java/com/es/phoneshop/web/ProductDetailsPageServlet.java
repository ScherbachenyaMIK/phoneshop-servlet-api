package com.es.phoneshop.web;

import com.es.phoneshop.cart.Cart;
import com.es.phoneshop.cart.CartService;
import com.es.phoneshop.cart.DefaultCartService;
import com.es.phoneshop.cart.TooMuchQuantityException;
import com.es.phoneshop.history.DefaultRecentlyViewedService;
import com.es.phoneshop.history.RecentlyViewedService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.util.ProductIdParser;
import com.es.phoneshop.util.QuantityParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao arrayListProductDao;

    private CartService cartService;

    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        arrayListProductDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = ProductIdParser.parseProductId(request);
        Product product = arrayListProductDao.getProduct(id);

        request.setAttribute("product", product);

        recentlyViewedService.addToRecentlyViewed(
                recentlyViewedService.getRecentlyViewedProducts(request),
                product
        );

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = ProductIdParser.parseProductId(request);
        int quantity;

        Locale locale = request.getLocale();

        try {
            quantity = QuantityParser.parseQuantity(
                    request.getParameter("quantity").trim(),
                    locale
            );
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e);

            doGet(request, response);
            return;
        }

        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, id, quantity);
        } catch (TooMuchQuantityException e) {
            request.setAttribute("error", e);

            doGet(request, response);
            return;
        }

        String message = "Product added successfully";

        response.sendRedirect(request.getRequestURI() + "?message=" + message);
    }
}
