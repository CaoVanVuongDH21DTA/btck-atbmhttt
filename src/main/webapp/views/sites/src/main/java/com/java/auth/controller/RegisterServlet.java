package com.java.auth.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.java.dao.UserDAO;
import com.java.model.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();
			
			User user = new User();
			
			BeanUtils.populate(user, request.getParameterMap());
			
			String message = userDAO.getMessage(user);
			
			if(message == null) {

				userDAO.insert(user);
				
				request.setAttribute("message", "Register successfully!");
				
				request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
			}else {
				request.setAttribute("message", message);
				
				doGet(request, response);
			}
			
		} catch (Exception e) {
			request.setAttribute("message", "Register failed!");
			doGet(request, response);
		}
	}

}
