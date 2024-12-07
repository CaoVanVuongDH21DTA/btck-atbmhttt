package com.java.admin.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.UserDAO;
import com.java.model.User;

@WebServlet("/UserRestoreServlet")
public class UserRestoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UserRestoreServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();
			
			int id_user = Integer.parseInt(request.getParameter("id"));
			
			User user = userDAO.findById(id_user);
			
			user.setActive(true);
			
			userDAO.update(user);
			
			response.sendRedirect("AdminUserrServlet");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
