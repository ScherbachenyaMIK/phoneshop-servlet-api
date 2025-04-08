package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String ORDER_ATTRIBUTE_NAME = "order";

    private static final String JSP_LOCATION_PATH = "/WEB-INF/pages/orderOverview.jsp";

    private OrderDao orderDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = request.getPathInfo().substring(1);
        Order order = orderDao.getBySecureId(secureOrderId);

        request.setAttribute(ORDER_ATTRIBUTE_NAME, order);

        request.getRequestDispatcher(JSP_LOCATION_PATH).forward(request, response);
    }
}
