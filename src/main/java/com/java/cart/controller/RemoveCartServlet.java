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

@WebServlet("/RemoveCartServlet")
public class RemoveCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RemoveCartServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			CartItemDAO cartItemDAO = new CartItemDAO();
			
			SessionUtils sessionUtils = new SessionUtils();
			
			User user = (User)sessionUtils.getSession(request, "user");
			
			List<CartItem> list = cartItemDAO.getByUser(user.getIdUsers());
			
			int num_id = Integer.parseInt(request.getParameter("id"));
			
			for(CartItem cartItem: list) {
				if(cartItem.getProduct().getIdProducts() ==  num_id) {
					cartItemDAO.delete(cartItem.getIdCartItem());
				}
			}
			
			
			response.sendRedirect("CartServlet");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
