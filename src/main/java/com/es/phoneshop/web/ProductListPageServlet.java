package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortingField;
import com.es.phoneshop.model.product.SortingOrder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao arrayListProductDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        arrayListProductDao = ArrayListProductDao.getInstance();
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
}
