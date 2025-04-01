package com.es.phoneshop.web;

import com.es.phoneshop.cart.Cart;
import com.es.phoneshop.cart.CartService;
import com.es.phoneshop.cart.DefaultCartService;
import com.es.phoneshop.cart.TooMuchQuantityException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortingField;
import com.es.phoneshop.model.product.SortingOrder;
import com.es.phoneshop.util.QuantityParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao arrayListProductDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        arrayListProductDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("searchingQuery");
        SortingField sortingField = SortingField.valueOf(
                Optional.ofNullable(request.getParameter("sort"))
                        .filter(sort -> !sort.isEmpty())
                        .orElse("none")
        );
        SortingOrder sortingOrder = SortingOrder.valueOf(
                Optional.ofNullable(request.getParameter("order"))
                        .filter(order -> !order.isEmpty())
                        .orElse("none")
        );

        request.setAttribute("products", arrayListProductDao.findProducts(
                query,
                sortingField,
                sortingOrder
                )
        );
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.valueOf(request.getParameter("productId"));
        int quantity;

        Locale locale = request.getLocale();

        try {
            quantity = QuantityParser.parseQuantity(
                    request.getParameter("quantity").trim(),
                    locale
            );
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e);
            request.setAttribute("id", id);

            doGet(request, response);
            return;
        }

        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, id, quantity);
        } catch (TooMuchQuantityException e) {
            request.setAttribute("error", e);
            request.setAttribute("id", id);

            doGet(request, response);
            return;
        }

        String message = "Product added successfully";

        response.sendRedirect(request.getRequestURI()
                + "?message=" + message
                + "&id=" + id
                + "&count=" + quantity
                + "&searchingQuery=" + request.getParameter("searchingQuery")
                + "&order=" + request.getParameter("order")
                + "&sort=" + request.getParameter("sort")
        );
    }
}
