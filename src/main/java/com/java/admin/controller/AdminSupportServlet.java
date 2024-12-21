package com.java.admin.controller;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.model.Support;
import com.java.utils.JpaUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/AdminSupportServlet")
public class AdminSupportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = JpaUtils.getEntityManager();

        try {
            // Truy vấn tất cả yêu cầu hỗ trợ
            Query query = em.createQuery("SELECT s FROM Support s");
            List<Support> requests = query.getResultList();

            // Chuyển danh sách yêu cầu hỗ trợ vào request để hiển thị trong JSP
            request.setAttribute("supports", requests);
            PageInfo.routeAdmin(request, response, PageType.ADMIN_SUPPORT_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Có lỗi xảy ra khi truy xuất yêu cầu hỗ trợ.");
        } finally {
            em.close(); 
        }
    }
}
