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
import com.java.dao.OrderDAO;
import com.java.dao.ProductDAO;
import com.java.model.Product;
import com.java.utils.SessionUtils;

@WebServlet("/AdminHomeServlet")
public class AdminHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminHomeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();
			
			ProductDAO productDAO = new ProductDAO();
			
			OrderDAO orderDAO = new OrderDAO();
			
			SessionUtils sessionUtils = new SessionUtils();
			
			request.setAttribute("count_User", userDAO.getCount());
			
			request.setAttribute("count_Product", productDAO.getCount());
			
			request.setAttribute("count_Order", orderDAO.getCount());
			
			request.setAttribute("admin", sessionUtils.getSession(request, "admin"));
			
			PageInfo.routeAdmin(request, response, PageType.ADMIN_HOME_PAGE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
