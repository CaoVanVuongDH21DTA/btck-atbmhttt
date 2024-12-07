package com.java.admin.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.ManagerDAO;
import com.java.model.Manager;
import com.java.utils.SessionUtils;

@WebServlet("/AdminSigninServlet")
public class AdminSigninServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminSigninServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/views/admins/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
	        request.setCharacterEncoding("UTF-8");

	        SessionUtils sessionUtils = new SessionUtils();

	        ManagerDAO managerDAO = new ManagerDAO();

	        String username = request.getParameter("username");
	        String password = request.getParameter("password");

	        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
	            request.setAttribute("message", "Username or password cannot be empty!");
	            doGet(request, response);
	            return;
	        }

	        Manager manager = managerDAO.getManager(username, password);

	        if(manager == null) {
	            request.setAttribute("message", "Wrong account. Try again!");
	            doGet(request, response);
	        } else {
	            sessionUtils.setSession(request, "admin", username);
	            response.sendRedirect("AdminHomeServlet"); // redirect đến trang admin khác sau khi đăng nhập thành công
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("message", "An error occurred. Please try again.");
	        doGet(request, response);
	    }
	}
}
