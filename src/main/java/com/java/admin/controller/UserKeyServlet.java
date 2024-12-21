package com.java.admin.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.UserDAO;

@WebServlet("/UserKeyServlet")
public class UserKeyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserDAO userDAO = new UserDAO();

            int id_user = Integer.parseInt(request.getParameter("id"));
            String userKey = userDAO.getUserKeyById(id_user);

            // Gửi key về client
            response.setContentType("text/plain");
            response.getWriter().write(userKey != null ? userKey : "Key không tồn tại!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi lấy key của user.");
        }
    }
}
