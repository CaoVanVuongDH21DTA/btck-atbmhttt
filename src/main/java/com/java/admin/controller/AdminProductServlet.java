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
import com.java.dao.ProductDAO;
import com.java.model.Product;

@WebServlet("/AdminProductServlet")
public class AdminProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminProductServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			ProductDAO productDAO = new ProductDAO();
			
			List<Product> listProducts = productDAO.findByActive(true);
			
			request.setAttribute("listProducts", listProducts);
			
			PageInfo.routeAdmin(request, response, PageType.ADMIN_PRODUCT_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			ProductDAO productDAO = new ProductDAO();
			
			String reload = request.getParameter("reload");
			
			List<Product> listProducts;
			boolean flag = true;
			if(reload == null) flag = false;
			
			if(flag) {
				listProducts = productDAO.getAll();
			}else {
				listProducts = productDAO.findByActive(true);
			}
			
			request.setAttribute("listProducts", listProducts);
			
			PageInfo.routeAdmin(request, response, PageType.ADMIN_PRODUCT_PAGE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
