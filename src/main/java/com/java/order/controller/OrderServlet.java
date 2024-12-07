package com.java.order.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.OrderDAO;
import com.java.model.User;
import com.java.model.Order;
import com.java.utils.SessionUtils;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public OrderServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            OrderDAO orderDAO = new OrderDAO();

            SessionUtils sessionUtils = new SessionUtils();

            User user = (User) sessionUtils.getSession(request, "user");

            List<Order> listOrders = orderDAO.getByUser(user.getIdUsers());

            // Kiểm tra nếu danh sách đơn hàng rỗng
            if (listOrders == null || listOrders.isEmpty()) {
                request.setAttribute("message", "Empty List");
            }

            request.setAttribute("listOrders", listOrders);
            request.setAttribute("flag", "Orders");

            PageInfo.routeSite(request, response, PageType.ORDER_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

