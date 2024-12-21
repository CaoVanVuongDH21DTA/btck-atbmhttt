package com.java.order.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.OrderItemDAO;
import com.java.model.OrderItem;

@WebServlet("/DetailOrderServlet")
public class DetailOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DetailOrderServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			OrderItemDAO orderItemDAO = new OrderItemDAO();
			
			int id_order = Integer.parseInt(request.getParameter("id"));
			
			List<OrderItem> list = orderItemDAO.getByOrder(id_order);
			
			request.setAttribute("list", list);
			
			PageInfo.routeSite(request, response, PageType.DETAIL_ORDER_PAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
