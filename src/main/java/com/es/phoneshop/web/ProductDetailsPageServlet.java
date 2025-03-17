package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao arrayListProductDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        arrayListProductDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.valueOf(request.getPathInfo().substring(1));
        request.setAttribute("product", arrayListProductDao.getProduct(id));

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }
}
