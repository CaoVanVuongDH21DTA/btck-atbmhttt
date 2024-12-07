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
import com.java.dao.CategoryDAO;
import com.java.dao.ProductDAO;
import com.java.model.Category;
import com.java.model.Product;
import com.java.utils.FormUtils;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
	        ProductDAO productDAO = new ProductDAO();
	        FormUtils formUtils = new FormUtils();
	        CategoryDAO categoryDAO = new CategoryDAO();

	        // Đếm số trang
	        Long num_productCount = productDAO.getCount();

	        if (num_productCount == null || num_productCount <= 0) {
	            num_productCount = 0L;
	        }

	        Long num_countPage = num_productCount % 6 == 0 ? num_productCount / 6 : num_productCount / 6 + 1;

	        // Lấy trang hiện tại
	        int num_page = formUtils.getInt(request, "page", 1);

	        // Lấy danh sách sản phẩm
	        List<Product> listProducts = productDAO.getAll(true, (num_page - 1) * 6, 6);

	        if (listProducts == null) {
	            listProducts = List.of(); // Đặt danh sách rỗng thay thế
	        }

	        // Lấy danh sách loại sản phẩm
	        List<Category> listCategories = categoryDAO.getAll();

	        if (listCategories != null) {
	            for (Category category : listCategories) {
	                category.setName(category.getName().toUpperCase());
	            }
	        } else {
	            listCategories = List.of(); // Đặt danh sách rỗng thay thế
	        }

	        // Xử lý dữ liệu
	        request.setAttribute("pageCount", num_countPage);
	        request.setAttribute("index", num_page);
	        request.setAttribute("listProducts", listProducts);
	        request.setAttribute("listCategories", listCategories);
	        request.setAttribute("flag", "Products");

	        PageInfo.routeSite(request, response, PageType.PRODUCT_PAGE);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
	    }
	}
}

