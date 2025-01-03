package com.java.site.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.ProductDAO;
import com.java.dao.ReviewDAO;
import com.java.model.Product;
import com.java.model.Review;

@WebServlet("/DetailServlet")
public class DetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			int id_product = Integer.parseInt(request.getParameter("id"));
			
			ProductDAO productDAO = new ProductDAO();
			
			ReviewDAO reviewDAO = new ReviewDAO();
			
			List<Review> listReviews = reviewDAO.getByProduct(id_product);
			
			Product product = productDAO.findById(id_product);
			
			product.setView(product.getView() + 1);
			
			productDAO.update(product);
			
			request.setAttribute("item", product);
			
			request.setAttribute("listReviews", listReviews);
			
			PageInfo.routeSite(request, response, PageType.DETAIL_PRODUCT_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
