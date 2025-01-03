package com.java.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.DiscountDAO;
import com.java.model.Discount;

@WebServlet("/AdminDiscountServlet")
public class AdminDiscountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public AdminDiscountServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DiscountDAO discountDAO = new DiscountDAO();
			
			List<Discount> listDiscounts = discountDAO.getAll();
			
			request.setAttribute("listDiscounts", listDiscounts);
			
			PageInfo.routeAdmin(request, response, PageType.ADMIN_DISCOUNT_PAGE);
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
