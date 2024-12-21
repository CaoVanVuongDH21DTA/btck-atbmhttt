package com.java.cart.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java.dao.OrderDAO;
import com.java.dao.SupportDAO;
import com.java.model.Order;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet("/confirmOrder")
public class ConfirmOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        String hashCodeFromRequest = request.getParameter("hashCode");
        String publicKey = request.getParameter("publicKey");

        HttpSession session = request.getSession();

        if (orderIdStr == null || hashCodeFromRequest == null || publicKey == null) {
            session.setAttribute("message", "Thiếu tham số đầu vào.");
            session.setAttribute("alertType", "danger");
            response.sendRedirect("OrderServlet");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.findById(orderId);

            if (order == null) {
                session.setAttribute("message", "ID đơn hàng không hợp lệ.");
                session.setAttribute("alertType", "danger");
                response.sendRedirect("OrderServlet");
                return;
            }

            if (!order.getHashCode().equals(hashCodeFromRequest)) {
                session.setAttribute("message", "Mã xác nhận không hợp lệ.");
                session.setAttribute("alertType", "danger");
                response.sendRedirect("OrderServlet");
                return;
            }

            Timestamp createdTimestamp = new Timestamp(order.getCreated().getTime());
            if (!isKeyValidForOrder(order.getUser().getEmail(), publicKey, createdTimestamp)) {
                session.setAttribute("message", "Public key này đã bị khóa hoặc không hợp lệ.");
                session.setAttribute("alertType", "danger");
                response.sendRedirect("OrderServlet");
                return;
            }

            // Tạo PDF trước
            generateOrderPdf(order, response);

            // Cập nhật trạng thái
            orderDAO.updateOrderStatus(orderId, "Đã duyệt");

            // Gửi thông báo thành công
            session.setAttribute("message", "Đơn hàng đã xác nhận và PDF đã được tạo.");
            session.setAttribute("alertType", "success");

            // Sau khi tạo PDF, chuyển hướng tới trang OrderServlet
            response.sendRedirect("OrderServlet");


        } catch (NumberFormatException e) {
            session.setAttribute("message", "ID đơn hàng không hợp lệ.");
            session.setAttribute("alertType", "danger");
            response.sendRedirect("OrderServlet");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Đã xảy ra lỗi trong quá trình xử lý.");
            session.setAttribute("alertType", "danger");
            response.sendRedirect("OrderServlet");
        }
    }

    private void generateOrderPdf(Order order, HttpServletResponse response) {
        // Đặt thông tin tiêu đề và kiểu dữ liệu trả về cho PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Order_" + order.getIdOrders() + ".pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            
            // Lấy font tùy chỉnh từ tài nguyên
            BaseFont baseFont = BaseFont.createFont("/fonts/Times New Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font contentFont = new Font(baseFont, 12);
            
            // Tiêu đề PDF
            document.add(new Paragraph("Thông Tin Đơn Hàng", titleFont));
            document.add(new Paragraph("\n"));
            
            // Thông tin chi tiết đơn hàng
            document.add(new Paragraph("Mã đơn hàng: " + order.getIdOrders(), contentFont));
            document.add(new Paragraph("Tên khách hàng: " + order.getUser().getFullname(), contentFont));
            document.add(new Paragraph("Địa chỉ: " + order.getAddress(), contentFont));
            document.add(new Paragraph("Số điện thoại: " + order.getPhone(), contentFont));
            document.add(new Paragraph("Tổng tiền: " + order.getAmount(), contentFont));
            document.add(new Paragraph("Ngày tạo: " + order.getCreated(), contentFont));
            document.add(new Paragraph("Trạng thái: " + order.getStatus(), contentFont));
            
            // Đóng tài liệu
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean isKeyValidForOrder(String email, String publicKey, Timestamp orderCreatedTime) {
        SupportDAO supportDAO = new SupportDAO();

        // 1. Kiểm tra publicKey hiện tại trong bảng users
        String currentPublicKey = supportDAO.getPublicKeyFromUser(email);
        if (currentPublicKey != null && currentPublicKey.equals(publicKey)) {
            return true; // Key hợp lệ nếu khớp với key hiện tại
        }

        // 2. Kiểm tra key cũ trong bảng user_key_history
        Timestamp oldKeyCreatedTime = supportDAO.getOldKeyCreatedTime(email, publicKey);
        if (oldKeyCreatedTime != null) {
            // 3. So sánh thời gian tạo đơn hàng với thời gian tạo key cũ
            if (orderCreatedTime.before(oldKeyCreatedTime) || orderCreatedTime.equals(oldKeyCreatedTime)) {
                return true; // Key hợp lệ nếu được tạo trước hoặc cùng thời điểm đơn hàng
            } else {
                System.out.println("Key cũ đã được tạo sau thời gian tạo đơn hàng. Không hợp lệ.");
            }
        } else {
            System.out.println("Public key không tồn tại trong bảng .");
        }
        
        return false; // Key không hợp lệ
    }
}