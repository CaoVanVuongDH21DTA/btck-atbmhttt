package com.java.admin.controller;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.model.User;
import com.java.service.EmailService;
import com.java.utils.JpaUtils;

@WebServlet("/RestoreUserKeyServlet")
public class RestoreUserKeyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdParam = request.getParameter("id");
        
        if (userIdParam != null) {
            try {
                int userId = Integer.parseInt(userIdParam);
                saveOldKeyAndGenerateNewKey(userId, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("Lỗi khi cấp lại key.");
            }
        } else {
            response.getWriter().write("Không tìm thấy ID.");
        }
    }

    public void saveOldKeyAndGenerateNewKey(int userId, HttpServletResponse response) throws IOException {
        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            em = JpaUtils.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            // 1. Truy xuất người dùng từ ID
            User user = em.find(User.class, userId);

            if (user != null) {
                // 2. Tạo cặp khóa RSA mới
                KeyPair keyPair = generateKeyPair();
                PublicKey publicKey = keyPair.getPublic();
                PrivateKey privateKey = keyPair.getPrivate();

                // 3. Mã hóa khóa thành Base64
                String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

                // 4. Cập nhật khóa mới vào user
                user.setKey(base64PublicKey);
                em.merge(user);

                // 5. Gửi khóa bí mật qua email
                EmailService emailService = new EmailService();
                String userEmail = user.getEmail(); // Giả sử người dùng có thuộc tính email
                emailService.sendKey(userEmail, base64PrivateKey, base64PublicKey);

                tx.commit();

                // Trả về thông báo thành công
                response.getWriter().write("Key được cấp lại thành công.");
            } else {
                response.getWriter().write("Không tìm thấy người dùng.");
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            response.getWriter().write("Có lỗi xảy ra khi cấp lại key.");
        } finally {
            if (em != null) em.close();
        }
    }

    // Phương thức tạo cặp khóa RSA
    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}
