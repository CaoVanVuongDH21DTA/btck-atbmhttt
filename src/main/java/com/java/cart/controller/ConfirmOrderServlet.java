package com.java.cart.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.OrderDAO;

@WebServlet("/confirmOrder")
public class ConfirmOrderServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");

        if (orderIdStr != null) {
            try {
                int orderId = Integer.parseInt(orderIdStr);

                OrderDAO orderDAO = new OrderDAO();

                // Cập nhật trạng thái của đơn hàng
                orderDAO.updateOrderStatus(orderId, "Đã Duyệt");

                // Gửi phản hồi HTML để đóng trang
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write("<html><body>");
                response.getWriter().write("<h2>Đơn hàng đã được xác nhận.</h2>");
                response.getWriter().write("<script>");
                response.getWriter().write("setTimeout(function(){ window.close(); }, 1);"); 
                response.getWriter().write("</script>");
                response.getWriter().write("</body></html>");
            } catch (NumberFormatException e) {
                response.getWriter().write("ID đơn hàng không hợp lệ.");
            }
        } else {
            response.getWriter().write("Không có ID đơn hàng.");
        }
    }
}
