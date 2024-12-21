package com.java.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.java.dao.BrandDAO;
import com.java.dao.CategoryDAO;
import com.java.dao.DiscountDAO;
import com.java.dao.ProductDAO;
import com.java.model.Product;

@MultipartConfig
@WebServlet("/ProductUploadServlet")
public class ProductUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        request.setCharacterEncoding("UTF-8");

	        HttpSession session = request.getSession();

	        Part part = request.getPart("excelFile");

	        String str_submit = part.getSubmittedFileName();

	        if (str_submit.isEmpty()) {
	            session.setAttribute("message", "Chọn file excel trước!");
	            response.sendRedirect("AdminProductServlet");
	            return;
	        }

	        BrandDAO brandDAO = new BrandDAO();
	        CategoryDAO categoryDAO = new CategoryDAO();
	        DiscountDAO discountDAO = new DiscountDAO();
	        ProductDAO productDAO = new ProductDAO();

	        File file = new File(request.getServletContext().getRealPath("/documents"));

	        if (!file.exists()) {
	            file.mkdir();
	        }

	        File excel_file = new File(file, str_submit);
	        part.write(excel_file.getAbsolutePath());

	        // Tiến hành đọc file excel
	        FileInputStream fis = new FileInputStream(excel_file);
	        Workbook workbook = new XSSFWorkbook(fis);
	        Sheet sheet = workbook.getSheetAt(0);
	        DataFormatter df = new DataFormatter();
	        Iterator<Row> itr = sheet.iterator();

	        List<Product> listProducts = new ArrayList<Product>();

	        while (itr.hasNext()) {
	            Row current = itr.next();

	            Product product = new Product();
	            product.setName(current.getCell(0).getStringCellValue());
	            product.setDescription(current.getCell(1).getStringCellValue());
	            product.setPrice(Float.parseFloat(df.formatCellValue(current.getCell(2))));
	            product.setImage(current.getCell(3).getStringCellValue());
	            product.setBrand(brandDAO.findById(Integer.parseInt(df.formatCellValue(current.getCell(4)))));
	            product.setCategory(categoryDAO.findById(Integer.parseInt(df.formatCellValue(current.getCell(5)))));
	            product.setDiscount(discountDAO.findById(Integer.parseInt(df.formatCellValue(current.getCell(6)))));
	            product.setAverageStar(new BigDecimal(0.0));
	            
	            listProducts.add(product);
	        }

	        // Chèn tất cả các sản phẩm vào cơ sở dữ liệu
	        for (Product item : listProducts) {
	            productDAO.insert(item);
	        }

	        // Gửi thông báo thành công và redirect
	        session.setAttribute("message", "Thêm thành công!");
	        response.sendRedirect("AdminProductServlet");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
