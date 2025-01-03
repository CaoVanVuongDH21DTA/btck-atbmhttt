package com.java.cart.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.CartItemDAO;
import com.java.model.CartItem;
import com.java.model.User;
import com.java.utils.SessionUtils;

@WebServlet("/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UpdateCartServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			
			SessionUtils sessionUtils = new SessionUtils();
			
			CartItemDAO cartItemDAO = new CartItemDAO();
			
			User user = (User)sessionUtils.getSession(request, "user");
			
			List<CartItem> listCartItems = cartItemDAO.getByUser(user.getIdUsers());
			
			//lấy dữ liệu để cập nhật
			String id = request.getParameter("id");
			
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			
			for(CartItem cartItem: listCartItems) {
				if(cartItem.getProduct().getIdProducts() == Integer.parseInt(id)) {
					cartItem.setQuantity(quantity);
					cartItemDAO.update(cartItem);
				}
			}
			response.sendRedirect("CartServlet");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
