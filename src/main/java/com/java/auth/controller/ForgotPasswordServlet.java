package com.java.auth.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java.dao.UserDAO;
import com.java.model.User;
import com.java.service.EmailService;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    // Xử lý GET yêu cầu từ người dùng nhấp vào liên kết trong email
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("reset".equals(action)) {
            HttpSession session = request.getSession();
            String email = request.getParameter("email");
            session.setAttribute("forgotPasswordMode", "reset"); 
            session.setAttribute("email", email); 
            
            // Chuyển hướng tới trang reset mật khẩu
            request.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(request, response);
            
            return;
        }
        
        HttpSession session = request.getSession();
        session.removeAttribute("forgotPasswordMode");
        session.removeAttribute("email");
        
        request.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(request, response);
    }

    // Xử lý POST yêu cầu khi người dùng yêu cầu quên mật khẩu
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String publicKey = request.getParameter("publicKey");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findUserByEmail(email);

        if (user != null) {
            // Lấy publicKey từ database để so sánh với privateKey
            String publicKeyDB = user.getKey();
            if (publicKeyDB != null && publicKeyDB.equals(publicKey)) {
                // Gửi email xác nhận
            	String resetLink = "http://localhost:8080/btck-atbmhttt/ForgotPasswordServlet?action=reset&email=" + email;
            	String emailContent = "<p>Bạn đã yêu cầu quên mật khẩu. Nếu đúng, nhấp vào liên kết dưới đây để đặt lại mật khẩu:</p>" +
            	                      "<p><a href='" + resetLink + "'>Tại đây</a></p>";

            	EmailService emailService = new EmailService();
            	emailService.sendEmail(email, "Xác nhận quên mật khẩu", emailContent, true);


                session.setAttribute("messageForgetPassword", "Email xác nhận đã được gửi. Vui lòng kiểm tra hộp thư của bạn!");
            } else {
                session.setAttribute("messageForgetPassword", "Khóa công khai không hợp lệ!");
            }
        } else {
            session.setAttribute("messageForgetPassword", "Email không tồn tại trong hệ thống!");
        }

        // Quay lại trang quên mật khẩu
        response.sendRedirect("ForgotPasswordServlet");
    }
}
