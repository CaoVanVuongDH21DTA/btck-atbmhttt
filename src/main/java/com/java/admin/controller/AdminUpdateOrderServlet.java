package com.java.admin.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.OrderDAO;
import com.java.model.Order;

@WebServlet("/AdminUpdateOrderServlet")
public class AdminUpdateOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminUpdateOrderServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			OrderDAO orderDAO = new OrderDAO();
			
			//lấy dữ liệu từ form
			int id_Order = Integer.parseInt(request.getParameter("id"));
			
			String str_status = request.getParameter("status");
			
			//lấy đối tượng và cập nhật dữ liệu
			
			Order order = orderDAO.findById(id_Order);
			
			order.setStatus(str_status);
			
			//cập nhật thông tin trong csdl
			orderDAO.update(order);
			
			//chuyển trang
			response.sendRedirect("AdminOrderServlet");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
