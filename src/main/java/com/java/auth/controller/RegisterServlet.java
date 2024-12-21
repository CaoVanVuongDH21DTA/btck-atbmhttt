package com.java.auth.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.java.dao.UserDAO;
import com.java.model.User;
import com.java.service.EmailService;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		try {
			request.setCharacterEncoding("UTF-8");
			
			UserDAO userDAO = new UserDAO();
			
			User user = new User();
			
			EmailService emailService = new EmailService();
			
			BeanUtils.populate(user, request.getParameterMap());
			
			String hasPublicKey = request.getParameter("hasPublicKey");
            String userKey = request.getParameter("publicKey"); 
			
			if ("on".equalsIgnoreCase(hasPublicKey) && userKey != null && !userKey.trim().isEmpty()) {
				user.setKey(userKey);
				userDAO.registerUserWithExistingKey(user);
				emailService.sendConfirmationEmail(user.getEmail(),  userKey);
			}else {
				// Nếu người dùng chưa có key, tạo key mới
				userDAO.registerUser(user);
			}
			
			String message = userDAO.getMessage(user);
			if(message == null) {
				
				session.setAttribute("message", "Đăng ký thành công!");
				
				request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
			}else {
				session.setAttribute("message", message);
				
				doGet(request, response);
			}
			
		} catch (Exception e) {
			session.setAttribute("message", "Đăng ký thất bại!");
			doGet(request, response);
		}
	}
}
