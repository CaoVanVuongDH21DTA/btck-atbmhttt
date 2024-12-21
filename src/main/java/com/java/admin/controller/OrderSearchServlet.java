package com.java.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.OrderDAO;
import com.java.model.Order;

@WebServlet("/OrderSearchServlet")
public class OrderSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OrderSearchServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			List<Order> listOrders;
			
			OrderDAO orderDAO = new OrderDAO();
			
			String statuses[] = request.getParameterValues("statuses");
			
			if(statuses == null) {
				listOrders = orderDAO.getAll();
			}else {
				List<String> list = Arrays.asList(statuses);
				
				listOrders = orderDAO.getByStatus(list);
			}

			List<String> listStatus = new ArrayList<String>();

			listStatus.add("Chờ duyệt");
			listStatus.add("Đã duyệt");
			listStatus.add("Đang vận chuyển");
			listStatus.add("Giao thành công");

			request.setAttribute("listStatus", listStatus);

			request.setAttribute("listOrders", listOrders);

			PageInfo.routeAdmin(request, response, PageType.ADMIN_ORDER_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
