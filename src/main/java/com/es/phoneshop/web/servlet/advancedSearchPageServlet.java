package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SearchingCriteria;
import com.es.phoneshop.util.PriceParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class advancedSearchPageServlet extends HttpServlet {
    private static final String PRODUCTS_ATTRIBUTE_NAME = "products";

    private static final String DESCRIPTION_PARAMETER_NAME = "description";

    private static final String MIN_PRICE_PARAMETER_NAME = "minPrice";

    private static final String MAX_PRICE_PARAMETER_NAME = "maxPrice";

    private static final String SEARCH_MODE_PARAMETER_NAME = "searchMode";

    private static final String MIN_PRICE_ERROR_ATTRIBUTE_NAME = "minPriceError";

    private static final String MAX_PRICE_ERROR_ATTRIBUTE_NAME = "maxPriceError";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/advancedSearch.jsp";

    private ProductDao arrayListProductDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        arrayListProductDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Locale locale = request.getLocale();
        String description = request.getParameter(DESCRIPTION_PARAMETER_NAME);
        String minPrice = request.getParameter(MIN_PRICE_PARAMETER_NAME);
        String maxPrice = request.getParameter(MAX_PRICE_PARAMETER_NAME);
        String searchMode = request.getParameter(SEARCH_MODE_PARAMETER_NAME);

        List<Product> products = new ArrayList<>();

        if (description != null || minPrice != null || maxPrice != null) {
            Map<String, String> exceptions = new HashMap<>();
            Map<String, BigDecimal> prices = checkPrice(minPrice, maxPrice, locale, exceptions);
            if (!exceptions.isEmpty()) {
                String message = exceptions.get("minPrice");
                if (message != null) {
                    request.setAttribute(MIN_PRICE_ERROR_ATTRIBUTE_NAME, message);
                }
                message = exceptions.get("maxPrice");
                if (message != null) {
                    request.setAttribute(MAX_PRICE_ERROR_ATTRIBUTE_NAME, message);
                }
            } else {
                products = arrayListProductDao.findProductsAdvanced(
                        description,
                        prices.get("minPrice"),
                        prices.get("maxPrice"),
                        SearchingCriteria.valueOf(searchMode
                                .replaceAll(" ", "")
                                .replaceAll("w", "W")
                        )
                );
            }
        }

        request.setAttribute(PRODUCTS_ATTRIBUTE_NAME, products);
        request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
    }

    private Map<String, BigDecimal> checkPrice(String minPrice, String maxPrice,
                                        Locale locale, Map<String, String> exceptions) {
        Map<String, BigDecimal> prices = new HashMap<>();
        try {
            if (minPrice != null && !minPrice.trim().isBlank()) {
                prices.put("minPrice", PriceParser.parsePrice(minPrice.trim(), locale));
            } else {
                prices.put("minPrice", BigDecimal.valueOf(-1));
            }
        } catch (IllegalArgumentException e) {
            exceptions.put("minPrice", e.getMessage());
        }
        try {
            if (maxPrice != null && !maxPrice.trim().isBlank()) {
                prices.put("maxPrice", PriceParser.parsePrice(maxPrice.trim(), locale));
            } else {
                prices.put("maxPrice", BigDecimal.valueOf(-1));
            }
        } catch (IllegalArgumentException e) {
            exceptions.put("maxPrice", e.getMessage());
        }
        return prices;
    }
}
