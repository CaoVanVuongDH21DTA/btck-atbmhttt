package com.java.auth.controller;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

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

            // Kiểm tra xem người dùng có chọn "Tôi đã có publicKey" không
            String hasPublicKey = request.getParameter("hasPublicKey");
            String userKey = request.getParameter("publicKey");

            if ("on".equalsIgnoreCase(hasPublicKey) && userKey != null && !userKey.trim().isEmpty()) {
                // Người dùng đã cung cấp publicKey => chỉ lưu vào cơ sở dữ liệu
                user.setKey(userKey);
                emailService.sendConfirmationEmail(user.getEmail(),  userKey);
            } else {
                // Người dùng không cung cấp publicKey => tạo mới public và private key
            	KeyPair keyPair = generateKeyPair();
            	PublicKey publicKey = keyPair.getPublic();
            	PrivateKey privateKey = keyPair.getPrivate();
            	
            	String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            	user.setKey(base64PublicKey);

            	 // Gửi privateKey qua email
              String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
              EmailService emailService1 = new EmailService();
              emailService1.sendKey(user.getEmail(), base64PrivateKey, base64PublicKey);
            }

            // Kiểm tra tính hợp lệ của người dùng
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

	private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}