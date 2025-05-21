package com.es.phoneshop.web.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class advancedSearchPageServlet extends HttpServlet {
    private static final String PRODUCTS_ATTRIBUTE_NAME = "products";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/advancedSearch.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(PRODUCTS_ATTRIBUTE_NAME, new ArrayList<>());
        request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
    }
}
