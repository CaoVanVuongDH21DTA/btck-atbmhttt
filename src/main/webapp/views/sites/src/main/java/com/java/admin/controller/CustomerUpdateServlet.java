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

/**
 * Servlet implementation class CustomerUpdateServlet
 */
@WebServlet("/CustomerUpdateServlet")
public class CustomerUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();
			
			int id_user = Integer.parseInt(request.getParameter("id"));
			
			User user = userDAO.findById(id_user);
			
			request.setAttribute("user", user);
			
			PageInfo.routeAdmin(request, response, PageType.ADMIN_CUSTOMER_UPDATE_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();
			
			FormUtils formUtils = new FormUtils();
			
			int idUser = Integer.parseInt(request.getParameter("id"));
			
			User user = formUtils.getBean(request, User.class);
			
			String message = userDAO.getMessage(user);
			
			if(message == null || message.equals("Email đã được đăng ký")) {
				user.setId(idUser);
				
				userDAO.update(user);
				
				response.sendRedirect("AdminCustomerServlet");
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
