package com.java.admin.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.DiscountDAO;
import com.java.model.Discount;
import com.java.utils.FormUtils;

@WebServlet("/DiscountUpdateServlet")
public class DiscountUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DiscountUpdateServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int id_discount = Integer.parseInt(request.getParameter("id"));
			
			DiscountDAO discountDAO = new DiscountDAO();
			
			Discount discount = discountDAO.findById(id_discount);
			
			request.setAttribute("discount", discount);
			
			PageInfo.routeAdmin(request, response, PageType.ADMIN_DISCOUNT_UPDATE_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			int id_discount = Integer.parseInt(request.getParameter("id"));
			
			FormUtils formUtils = new FormUtils();
			
			DiscountDAO discountDAO = new DiscountDAO();
			
			Discount discount = formUtils.getBean(request, Discount.class);
			
			discount.setIdDiscounts(id_discount);
			
			discountDAO.update(discount);
			
			response.sendRedirect("AdminDiscountServlet");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
