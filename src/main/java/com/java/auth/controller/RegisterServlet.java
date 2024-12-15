package com.java.auth.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.java.dao.UserDAO;
import com.java.model.User;
import com.java.service.EmailService; // Giả sử bạn có một service gửi email

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RegisterServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            UserDAO userDAO = new UserDAO();
            EmailService emailService = new EmailService(); 
            
            User user = new User();
            BeanUtils.populate(user, request.getParameterMap());
            
            // Kiểm tra người dùng có chọn ô "Tôi đã có publicKey" không
            String havePublicKey = request.getParameter("hasPublicKey");
            String userKey = request.getParameter("publicKey"); // Nhận publicKey từ request

            if ("on".equalsIgnoreCase(havePublicKey) && userKey != null && !userKey.trim().isEmpty()) {
                // Người dùng đã cung cấp publicKey
                user.setKey(userKey); // Lưu vào cơ sở dữ liệu
            } else {
                // Tạo publicKey và privateKey nếu không có publicKey từ người dùng
                String generatedPublicKey = java.util.UUID.randomUUID().toString();
                String generatedPrivateKey = java.util.UUID.randomUUID().toString();

                user.setKey(generatedPublicKey); // Lưu publicKey vào cơ sở dữ liệu

                // Gửi email chứa privateKey
                try {
                    emailService.sendKey(userKey, generatedPrivateKey, generatedPublicKey);
                } catch (Exception e) {
                    System.err.println("Đã có publicKey, không tạo private");
                }
            }

            // Kiểm tra xem User đã hợp lệ chưa
            String message = userDAO.getMessage(user);

            if (message == null) {
                userDAO.registerUser(user); // Đăng ký người dùng
                
                request.setAttribute("message", "Register successfully!");
                request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            } else {
                request.setAttribute("message", message);
                doGet(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("message", "Register failed!");
            doGet(request, response);
        }
    }
}
