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

@WebServlet("/AdminOrderServlet")
public class AdminOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminOrderServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			OrderDAO orderDAO = new OrderDAO();
			
			List<Order> listOrders = orderDAO.getActive();
			
			request.setAttribute("listOrders", listOrders);
					
			List<String> listStatus = new ArrayList<String>();
			
			listStatus.add("Chờ duyệt");
			listStatus.add("Đã duyệt");
			listStatus.add("Đang vận chuyển");
			listStatus.add("Giao thành công");
			
			request.setAttribute("listStatus", listStatus);
			
			PageInfo.routeAdmin(request, response, PageType.ADMIN_ORDER_PAGE);
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
