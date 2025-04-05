package com.es.phoneshop.web;

import com.es.phoneshop.common.Messages;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.TooMuchQuantityException;
import com.es.phoneshop.model.history.DefaultRecentlyViewedService;
import com.es.phoneshop.model.history.RecentlyViewedService;
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
    private static final String PRODUCT_ATTRIBUTE_NAME = "product";

    private static final String QUANTITY_ATTRIBUTE_NAME = "quantity";

    private static final String ERROR_ATTRIBUTE_NAME = "error";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/product.jsp";

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
        Product product = arrayListProductDao.getById(id);

        request.setAttribute(PRODUCT_ATTRIBUTE_NAME, product);

        recentlyViewedService.addToRecentlyViewed(
                recentlyViewedService.getRecentlyViewedProducts(request),
                product
        );

        request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);

        try {
            processProductUpdate(
                    ProductIdParser.parseProductId(request),
                    request.getLocale(),
                    request.getParameter(QUANTITY_ATTRIBUTE_NAME).trim(),
                    cart
            );
        } catch (IllegalArgumentException | TooMuchQuantityException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, e);

            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getRequestURI()
                + "?message="
                + Messages.PRODUCT_ADDED_TO_CART_SUCCESS
        );
    }

    private void processProductUpdate(Long productId, Locale locale, String quantityStr, Cart cart)
            throws IllegalArgumentException, TooMuchQuantityException {
        int quantity = QuantityParser.parseQuantity(
                    quantityStr,
                    locale
            );

        cartService.add(cart, productId, quantity);
    }
}
