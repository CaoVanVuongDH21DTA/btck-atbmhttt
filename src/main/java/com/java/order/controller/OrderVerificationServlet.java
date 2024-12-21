package com.java.order.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.OrderDAO;
import com.java.model.Order;

@WebServlet("/OrderVerificationServlet")
public class OrderVerificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processOrderVerification(request, response);
    }

    private void processOrderVerification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Lấy các tham số từ request
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String confirmation = request.getParameter("confirmation");

            // Tạo đối tượng OrderDAO và tìm đơn hàng theo orderId
            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getOrderById(orderId);

            // Kiểm tra nếu không tìm thấy đơn hàng
            if (order == null) {
                response.getWriter().write("Đơn hàng không hợp lệ hoặc mã xác nhận không đúng.");
                return;
            }

            // Xử lý xác nhận thay đổi trạng thái đơn hàng
            if ("yes".equals(confirmation)) {
                String newStatus = "Đã xử lý thành công"; 
                order.setStatus(newStatus);

                String newAddress = request.getParameter("address");
                String newPhone = request.getParameter("phone");

                // Cập nhật địa chỉ và số điện thoại mới vào đơn hàng
                if (newAddress != null && !newAddress.trim().isEmpty()) {
                    order.setAddress(newAddress);
                }
                if (newPhone != null && !newPhone.trim().isEmpty()) {
                    order.setPhone(newPhone);
                }

                // Lưu các thay đổi vào cơ sở dữ liệu
                orderDAO.updateOrderStatus(orderId, newStatus);
                orderDAO.updateOrderDetails(orderId, newAddress, newPhone);

                response.getWriter().write("Đơn hàng đã được xác nhận và trạng thái đã được cập nhật.");
            } else if ("delete".equals(confirmation)) {
                // Xóa đơn hàng nếu người dùng chọn 'delete'
                orderDAO.delete(orderId);
                response.getWriter().write("Đơn hàng đã bị xóa khỏi hệ thống.");
            } else {
                response.getWriter().write("Lỗi: Không có lựa chọn xác nhận hợp lệ.");
            }

            // Tải lại trang đơn hàng sau khi xử lý
            response.sendRedirect("OrderServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Lỗi khi xác nhận thay đổi trạng thái đơn hàng: " + e.getMessage());
        }
    }
}