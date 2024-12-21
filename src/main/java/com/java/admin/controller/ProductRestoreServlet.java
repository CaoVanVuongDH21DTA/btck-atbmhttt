package com.java.admin.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.ProductDAO;
import com.java.model.Product;

@WebServlet("/ProductRestoreServlet")
public class ProductRestoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductRestoreServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ProductDAO productDAO = new ProductDAO();
			
			int id_product = Integer.parseInt(request.getParameter("id"));
			
			Product product = productDAO.findById(id_product);
			
			product.setActive(true);
			
			productDAO.update(product);
			
			response.sendRedirect("AdminProductServlet");
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
