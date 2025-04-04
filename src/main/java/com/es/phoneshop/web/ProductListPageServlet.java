package com.es.phoneshop.web;

import com.es.phoneshop.cart.Cart;
import com.es.phoneshop.cart.CartService;
import com.es.phoneshop.cart.DefaultCartService;
import com.es.phoneshop.cart.TooMuchQuantityException;
import com.es.phoneshop.common.Messages;
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
    private static final String PRODUCTS_ATTRIBUTE_NAME = "products";

    private static final String ID_ATTRIBUTE_NAME = "id";

    private static final String ERROR_ATTRIBUTE_NAME = "error";

    private static final String SEARCHING_QUERY_PARAMETER_NAME = "searchingQuery";

    private static final String SORT_PARAMETER_NAME = "sort";

    private static final String ORDER_PARAMETER_NAME = "order";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/productList.jsp";

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
        String query = request.getParameter(SEARCHING_QUERY_PARAMETER_NAME);
        SortingField sortingField = SortingField.valueOf(
                Optional.ofNullable(request.getParameter(SORT_PARAMETER_NAME))
                        .filter(sort -> !sort.isEmpty())
                        .orElse("none")
        );
        SortingOrder sortingOrder = SortingOrder.valueOf(
                Optional.ofNullable(request.getParameter(ORDER_PARAMETER_NAME))
                        .filter(order -> !order.isEmpty())
                        .orElse("none")
        );

        request.setAttribute(PRODUCTS_ATTRIBUTE_NAME, arrayListProductDao.findProducts(
                query,
                sortingField,
                sortingOrder
                )
        );
        request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Locale locale = request.getLocale();
        Cart cart = cartService.getCart(request);
        Long id = Long.valueOf(request.getParameter("productId"));
        String quantity = request.getParameter("quantity").trim();

        try {
            processProductUpdate(
                    id,
                    locale,
                    quantity,
                    cart
                    );
        } catch (TooMuchQuantityException | IllegalArgumentException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, e);
            request.setAttribute(ID_ATTRIBUTE_NAME, id);

            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getRequestURI() + prepareParameters(
                id.toString(),
                quantity,
                request.getParameter(SEARCHING_QUERY_PARAMETER_NAME),
                request.getParameter(ORDER_PARAMETER_NAME),
                request.getParameter(SORT_PARAMETER_NAME)
                )
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

    private String prepareParameters(String id, String count,
                                     String searchingQuery, String order, String sort) {
        return "?message=" + Messages.PRODUCT_ADDED_TO_CART_SUCCESS
                + "&id=" + id
                + "&count=" + count
                + "&searchingQuery=" + searchingQuery
                + "&order=" + order
                + "&sort=" + sort;
    }
}
