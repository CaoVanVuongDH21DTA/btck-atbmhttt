package com.java.admin.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.UserDAO;
import com.java.model.User;
import com.java.utils.FormUtils;

@WebServlet("/UserUpdateServlet")
public class UserUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UserUpdateServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();
			
			int id_user = Integer.parseInt(request.getParameter("id"));
			
			User user = userDAO.findById(id_user);
			
			request.setAttribute("user", user);
			
			PageInfo.routeAdmin(request, response, PageType.ADMIN_USER_UPDATE_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			UserDAO userDAO = new UserDAO();
			
			FormUtils formUtils = new FormUtils();
			
			int idUser = Integer.parseInt(request.getParameter("id"));
			
			User user = formUtils.getBean(request, User.class);
			
			String message = userDAO.getMessage(user);
			
			if(message == null || message.equals("Email đã được đăng ký")) {
				user.setIdUsers(idUser);
				
				userDAO.update(user);
				
				response.sendRedirect("AdminUserServlet");
			}else {
				request.setAttribute("message", message);
				
				doGet(request, response);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
