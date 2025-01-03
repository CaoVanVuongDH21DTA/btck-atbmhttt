package com.java.admin.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.OrderDAO;
import com.java.model.Order;

@WebServlet("/OrderCancelServlet")
public class OrderCancelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OrderCancelServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			
			OrderDAO orderDAO = new OrderDAO();
			
			int id_Order = Integer.parseInt(request.getParameter("id"));
			
			Order order = orderDAO.findById(id_Order);
			
			order.setActive(false);
			
			orderDAO.update(order);
			
			response.sendRedirect("AdminOrderServlet");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
