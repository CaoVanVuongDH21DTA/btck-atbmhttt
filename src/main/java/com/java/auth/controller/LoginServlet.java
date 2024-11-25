package com.java.auth.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.java.dao.AccountDAO;
import com.java.dao.UserDAO;
import com.java.model.Account;
import com.java.model.User;
import com.java.utils.CookieUtils;
import com.java.utils.SessionUtils;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        AccountDAO accountDAO = new AccountDAO();
	        UserDAO userDAO = new UserDAO();

	        Account account = new Account();
	        BeanUtils.populate(account, request.getParameterMap());

	        // Lấy thông tin user và kiểm tra mật khẩu đã mã hóa
	        User user = userDAO.getUser(account.getEmail(), account.getPassword());

	        boolean b_isValid = user != null;

	        if (b_isValid) {
	            if (!user.isActive()) {
	                request.setAttribute("message", "Locked account. Contact to admin for more information!");
	                request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
	            } else {
	                // Kiểm tra ghi nhớ tài khoản
	                boolean b_isRemember = account.isRemember();

	                if (b_isRemember) {
	                    // Thêm cookie
	                    CookieUtils cookieUtils = new CookieUtils();
	                    cookieUtils.addCookie(response, "email", account.getEmail(), 1);
	                }

	                // Thêm session
	                SessionUtils sessionUtils = new SessionUtils();
	                sessionUtils.setSession(request, "user", user);

	                response.sendRedirect("HomeServlet");
	            }
	        } else {
	            request.setAttribute("message", "Wrong account. Try again!");
	            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
