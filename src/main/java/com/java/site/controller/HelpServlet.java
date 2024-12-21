package com.java.site.controller;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.model.Support;
import com.java.model.User;
import com.java.service.EmailService;
import com.java.utils.JpaUtils;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;

import javax.persistence.EntityTransaction;

@WebServlet("/HelpServlet")
public class HelpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageInfo.routeSite(request, response, PageType.HELP_PAGE);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String subject = request.getParameter("subject");
        String messageContent = request.getParameter("message");
        String errorType = request.getParameter("errorType");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Vui lòng đăng nhập trước khi bạn muốn gửi trợ giúp.\"}");
            response.sendRedirect("LoginServlet");
            return;
        }

        String email = user.getEmail();

        EntityManager em = JpaUtils.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }

            Support support = new Support();
            support.setEmail(email);
            support.setSubject(subject);
            support.setMessage(messageContent);
            support.setErrorType(errorType);
            support.setStatus("Chưa xử lý");

            Date currentTime = new Date();
            support.setRequestTime(currentTime);

            em.persist(support);

            // Lock user account if errorType is "UserKey"
            if ("User Key".equalsIgnoreCase(errorType)) {
                User userToUpdate = em.find(User.class, user.getIdUsers());
                if (userToUpdate != null) {
                    userToUpdate.setActive(false);
                    userToUpdate.setKey("null");
                    em.merge(userToUpdate);
                    session.invalidate(); 
                }
                if(userToUpdate != null) {
                    String currentKey = user.getKey();

                    if (currentKey != null) {
                        // 3. Lưu key cũ vào bảng user_key_history
                        String sqlInsert = "INSERT INTO user_key_history (user_id, old_key, reason) VALUES (?, ?, ?)";
                        em.createNativeQuery(sqlInsert)
                          .setParameter(1, user.getIdUsers())
                          .setParameter(2, currentKey)
                          .setParameter(3, errorType)
                          .executeUpdate();
                    }
                }
            }

            transaction.commit();

            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\", \"message\":\"Yêu cầu hỗ trợ được gửi thành công!<br>Tài khoản của bạn đã bị khóa khi bạn gửi trợ giúp này!<br>Shop sẽ cố gắng hỗ trợ bạn trong thời gian sớm nhất ♥♥♥\"}");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Lỗi khi xử lý yêu cầu.\"}");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
