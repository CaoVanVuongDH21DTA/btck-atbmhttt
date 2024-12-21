package com.java.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.dao.OrderDAO;
import com.java.model.Order;

@WebServlet("/CheckNewOrderServlet")
public class CheckNewOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CheckNewOrderServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            OrderDAO orderDAO = new OrderDAO();
            
            // Lấy tất cả các đơn hàng có trạng thái "Đã duyệt"
            List<Order> newOrders = orderDAO.getNewOrder(); 

            // Lấy đơn hàng mới nhất
            Order latestOrder = orderDAO.getLatestOrder(); 

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonResponse = new HashMap<>();

            // Kiểm tra và xử lý đơn hàng mới
            if (!newOrders.isEmpty()) {
                jsonResponse.put("newOrderNotification", "Bạn có " + newOrders.size() + " đơn hàng mới!");
                jsonResponse.put("orderIds", newOrders);
            } 

            // Nếu có đơn hàng mới nhất, trả về thông tin đơn hàng mới nhất
            if (latestOrder != null) {
                jsonResponse.put("latestOrderNotification", "Đơn hàng mới nhất có ID: " + latestOrder.getIdOrders());
                jsonResponse.put("latestOrder", latestOrder);
            }

            response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"newOrderNotification\":\"Có lỗi xảy ra\", \"orderIds\":[]}");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

