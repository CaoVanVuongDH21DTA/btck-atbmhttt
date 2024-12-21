package com.java.auth.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java.dao.UserDAO;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Kiểm tra lại mật khẩu xác nhận trên server (dù đã kiểm tra ở client)
        if (newPassword.equals(confirmPassword)) {
            UserDAO userDAO = new UserDAO();
            userDAO.updatePassword(email, newPassword);
            session.setAttribute("messageForgetPassword", "Đặt lại mật khẩu thành công! Hãy đăng nhập.");
            session.removeAttribute("messageForgetPassword");
            session.removeAttribute("email");
        } else {
            session.setAttribute("messageForgetPassword", "Mật khẩu xác nhận không khớp!");
            response.sendRedirect("ForgotPasswordServlet");
            return;
        }

        response.sendRedirect("LoginServlet");
    }
}
