package com.java.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/changeLanguage")
public class ChangeLanguage extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String lang = request.getParameter("lang");
        PrintWriter out = response.getWriter();

        if (lang != null && (lang.equals("vi") || lang.equals("en"))) {
            // Cập nhật ngôn ngữ trong session
            request.getSession().setAttribute("locale", lang);

            // Phản hồi thành công
            out.print("{\"status\":\"success\"}");
        } else {
            // Log lỗi
            System.err.println("Ngôn ngữ không hợp lệ: " + lang);

            // Phản hồi lỗi
            out.print("{\"status\":\"error\", \"message\":\"Invalid language\"}");
        }
        out.flush();
    }
}
