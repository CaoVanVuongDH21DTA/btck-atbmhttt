package com.java.cart.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.OrderDAO;
import com.java.model.Order;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@WebServlet("/confirmOrder")
public class ConfirmOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        String hashCodeFromRequest = request.getParameter("hashCode");
        String publicKey = request.getParameter("publicKey"); // Nhập publicKey

        if (orderIdStr != null && hashCodeFromRequest != null && publicKey != null) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                
                OrderDAO orderDAO = new OrderDAO();
                
                // Lấy đơn hàng từ cơ sở dữ liệu theo orderId
                Order order = orderDAO.findById(orderId);
                
                if (order != null) {
                    // Kiểm tra hashCode trong database với hashCode từ request
                    if (order.getHashCode().equals(hashCodeFromRequest)) {
                        // Kiểm tra publicKey trong database
                        String storedPublicKey = order.getUser().getKey(); // Lấy publicKey từ user
                        
                        if (storedPublicKey != null && storedPublicKey.equals(publicKey)) {
                            // Cập nhật trạng thái đơn hàng nếu hashCode và publicKey trùng khớp
                            orderDAO.updateOrderStatus(orderId, "Đã xác thực");

                            // Tạo file PDF và trả về cho người dùng
                            generateOrderPdf(order, response);

                            // Chuyển hướng người dùng về OrderServlet
//                            response.sendRedirect("http://localhost:8080/btck-atbmhttt/OrderServlet");
                        } else {
                            // Nếu publicKey không trùng khớp, thông báo lỗi
                            response.getWriter().write("<html><body><h2>Public key không hợp lệ.</h2></body></html>");
                        }
                    } else {
                        // Nếu hashCode không trùng khớp, thông báo lỗi
                        response.getWriter().write("<html><body><h2>Mã xác nhận không hợp lệ.</h2></body></html>");
                    }
                } else {
                    // Nếu không tìm thấy đơn hàng trong cơ sở dữ liệu
                    response.getWriter().write("<html><body><h2>ID đơn hàng không hợp lệ.</h2></body></html>");
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("ID đơn hàng không hợp lệ.");
            }
        } else {
            response.getWriter().write("Không có ID đơn hàng hoặc mã xác nhận.");
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
}
