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
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
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
        Long id = parseProductId(request);
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
        Long id = parseProductId(request);
        int quantity;

        Locale locale = request.getLocale();

        try {
            quantity = parseQuantity(request.getParameter("quantity").trim(), locale);
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

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }

    private int parseQuantity(String quantity, Locale locale) throws IllegalArgumentException {
        if (quantity.isEmpty()) {
            throw new IllegalArgumentException("Quantity must not be empty");
        }

        ParsePosition pos = new ParsePosition(0);
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        try {
            Number number = numberFormat.parse(quantity, pos);
            if (pos.getIndex() < quantity.length()) {
                throw new ParseException(quantity, pos.getIndex());
            }

            if (number.intValue() < 1) {
                throw new IllegalArgumentException("Quantity must be a positive number");
            }

            if (Math.ceil(number.doubleValue()) != number.intValue()) {
                throw new IllegalArgumentException("Quantity must be an integer");
            }

            return number.intValue();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Quantity must be an integer", e);
        }
    }
}
