package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortingField;
import com.es.phoneshop.model.product.SortingOrder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class advancedSearchPageServlet extends HttpServlet {
    private static final String PRODUCTS_ATTRIBUTE_NAME = "products";

    private static final String DESCRIPTION_PARAMETER_NAME = "description";

    private static final String MIN_PRICE_PARAMETER_NAME = "minPrice";

    private static final String MAX_PRICE_PARAMETER_NAME = "maxPrice";

    private static final String SEARCH_MODE_PARAMETER_NAME = "searchMode";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/advancedSearch.jsp";

    private ProductDao arrayListProductDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        arrayListProductDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter(DESCRIPTION_PARAMETER_NAME);
        String minPrice = request.getParameter(MIN_PRICE_PARAMETER_NAME);
        String maxPrice = request.getParameter(MAX_PRICE_PARAMETER_NAME);
        String searchMode = request.getParameter(SEARCH_MODE_PARAMETER_NAME);

        List<Product> products = new ArrayList<>();

        if (description == null && minPrice == null && maxPrice == null) {
            request.setAttribute(PRODUCTS_ATTRIBUTE_NAME, products);
        } else {
            products = arrayListProductDao.findProducts(
                    "",
                    SortingField.none,
                    SortingOrder.none
            );
            request.setAttribute(PRODUCTS_ATTRIBUTE_NAME, products);
        }

        request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
    }
}
