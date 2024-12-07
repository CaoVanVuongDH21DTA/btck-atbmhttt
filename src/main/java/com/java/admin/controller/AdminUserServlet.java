package com.java.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.UserDAO;
import com.java.dao.ManagerDAO;
import com.java.model.User;
import com.java.model.Manager;

@WebServlet("/AdminUserServlet")
public class AdminUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AdminUserServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();

			List<User> listUsers = userDAO.getActive(true);

			request.setAttribute("listUsers", listUsers);

			PageInfo.routeAdmin(request, response, PageType.ADMIN_USER_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reload = request.getParameter("reload");

		boolean flag = true;
		if (reload == null) {
			flag = false;
		} else {
			flag = true;
		}

		try {
			request.setCharacterEncoding("UTF-8");
			
			UserDAO userDAO = new UserDAO();

			List<User> listUsers;
			if(flag) {
				listUsers = userDAO.getAll();
			}else {
				listUsers = userDAO.getActive(true);
			}

			request.setAttribute("listUsers", listUsers);

			PageInfo.routeAdmin(request, response, PageType.ADMIN_USER_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
