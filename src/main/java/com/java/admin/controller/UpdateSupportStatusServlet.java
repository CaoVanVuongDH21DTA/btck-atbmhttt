package com.java.admin.controller;

import com.java.dao.ManagerDAO;
import com.java.model.Manager;
import com.java.model.Support;
import com.java.utils.JpaUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.Date;

@WebServlet("/UpdateSupportStatusServlet")
public class UpdateSupportStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");	
    	int requestId = Integer.parseInt(request.getParameter("request_id"));
        String newStatus = request.getParameter("status");
        
        HttpSession session = request.getSession();

	     // Lấy thông tin tên người dùng từ session
	     String username = (String) session.getAttribute("admin");
	
	     if (username == null) {
	         response.getWriter().write("Người dùng chưa đăng nhập.");
	         return;
	     }
	
	     // Truy xuất đối tượng Manager từ database dựa trên username
	     ManagerDAO managerDAO = new ManagerDAO();
	     Manager manager = managerDAO.findByUsername(username);
	
	     if (manager == null) {
	         response.getWriter().write("Không tìm thấy thông tin người quản lý.");
	         return;
	     }
	
	     // Lấy thông tin người phụ trách từ Manager
	     String assignedTo = manager.getUsername();

        EntityManager em = JpaUtils.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            // Bắt đầu giao dịch
            transaction.begin();

            // Truy xuất yêu cầu hỗ trợ theo ID
            Support support = em.find(Support.class, requestId);
            if (support != null) {
                // Cập nhật trạng thái
            	support.setStatus(newStatus);
            	
            	// Cập nhật người phụ trách
                if (assignedTo != null && !assignedTo.trim().isEmpty()) {
                    support.setAssignedTo(assignedTo.trim());
                }
            	
                // Cập nhật lại thời gian hiện tại
                support.setRequestTime(new Date());
                
                em.merge(support);
            }

            // Cam kết giao dịch
            transaction.commit();

            response.sendRedirect("AdminSupportServlet");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Nếu có lỗi, rollback giao dịch
            }
            e.printStackTrace();
            response.getWriter().write("Có lỗi xảy ra khi cập nhật trạng thái yêu cầu.");
        } finally {
            em.close();
        }
    }
}
