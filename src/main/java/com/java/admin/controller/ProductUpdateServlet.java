package com.java.admin.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.BrandDAO;
import com.java.dao.CategoryDAO;
import com.java.dao.DiscountDAO;
import com.java.dao.ProductDAO;
import com.java.model.Product;
import com.java.utils.FormUtils;

@WebServlet("/ProductUpdateServlet")
public class ProductUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));

			ProductDAO productDAO = new ProductDAO();

			BrandDAO brandDAO = new BrandDAO();

			CategoryDAO categoryDAO = new CategoryDAO();

			DiscountDAO discountDAO = new DiscountDAO();

			Product product = productDAO.findById(id);

			request.setAttribute("item", product);

			request.setAttribute("listBrands", brandDAO.getAll());

			request.setAttribute("listCategories", categoryDAO.getAll());

			request.setAttribute("listDiscounts", discountDAO.getAll());

			PageInfo.routeAdmin(request, response, PageType.ADMIN_PRODUCT_UPDATE_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			ProductDAO productDAO = new ProductDAO();

			BrandDAO brandDAO = new BrandDAO();

			CategoryDAO categoryDAO = new CategoryDAO();

			DiscountDAO discountDAO = new DiscountDAO();

			FormUtils formUtils = new FormUtils();
			
			int productId = Integer.parseInt(request.getParameter("id"));
	        Product existingProduct = productDAO.findById(productId);
			
			Product product = new Product();

			product = formUtils.getBean(request, Product.class);
			
			if (product.getAverageStar() == null) {
	            product.setAverageStar(existingProduct.getAverageStar());
	        }

			if (product.getView() == 0) {
	            product.setView(existingProduct.getView());  
	        }
	        if (product.getRate() == 0) {
	            product.setRate(existingProduct.getRate()); 
	        }
			product.setBrand(brandDAO.findById(Integer.parseInt(request.getParameter("id_brand"))));

			product.setCategory(categoryDAO.findById(Integer.parseInt(request.getParameter("id_category"))));

			product.setDiscount(discountDAO.findById(Integer.parseInt(request.getParameter("id_discount"))));

			product.setIdProducts(Integer.parseInt(request.getParameter("id")));

			productDAO.update(product);

			response.sendRedirect("AdminProductServlet");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
