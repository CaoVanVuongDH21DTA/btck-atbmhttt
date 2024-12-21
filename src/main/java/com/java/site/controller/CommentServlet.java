package com.java.site.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java.dao.ProductDAO;
import com.java.dao.ReviewDAO;
import com.java.model.User;
import com.java.model.Product;
import com.java.model.Review;
import com.java.utils.SessionUtils;

@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        request.setCharacterEncoding("UTF-8");

	        HttpSession session = request.getSession();
	        SessionUtils sessionUtils = new SessionUtils();
	        User user = (User) sessionUtils.getSession(request, "user");

	        if (user == null) {
	            session.setAttribute("message", "Bạn chưa đăng nhập. Vui lòng đăng nhập trước khi comment!");
	            session.setAttribute("alertType", "warning");
	            
	            response.sendRedirect("LoginServlet");
	            return;
	        }

	        ReviewDAO reviewDAO = new ReviewDAO();
	        Review review = new Review();
	        ProductDAO productDAO = new ProductDAO();

	        int id_product = Integer.parseInt(request.getParameter("id"));
	        Product product = productDAO.findById(id_product);

	        if (product == null) {
	            session.setAttribute("message", "Không tìm thấy sản phẩm!");
	            session.setAttribute("alertType", "error");
	            response.sendRedirect("DetailServlet?id=" + id_product);
	            return;
	        }

	        String str_comment = request.getParameter("comment");
	        if (str_comment == null || str_comment.trim().isEmpty()) {
	            response.sendRedirect("DetailServlet?id=" + id_product + "&error=commemtEmpty &error=ratingEmpty");
	            session.setAttribute("message", "Vui lòng nhập đầy đủ comment và đánh giá sản phẩm");
	            session.setAttribute("alertType", "warning");
	            return;
	        }

	        // Lấy giá trị rating từ form
	        String str_rating = request.getParameter("rating");
	        if (str_rating == null) {
	            response.sendRedirect("DetailServlet?id=" + id_product + "&error=ratingEmpty");
	            session.setAttribute("message", "Vui lòng đánh giá sản phẩm");
	            session.setAttribute("alertType", "warning");
	            return;
	        }
	        int vote = (str_rating != null && !str_rating.isEmpty()) ? Integer.parseInt(str_rating) : 0;

	        review.setUser(user);
	        review.setProduct(product);
	        review.setComment(str_comment);
	        review.setVote(vote);

	        reviewDAO.insert(review);
	        
			product.setRate(product.getRate() + 1);
			
			 // Tính lại averageStar
	        List<Review> allReviews = reviewDAO.getByProduct(id_product); // Lấy tất cả review của sản phẩm
	        BigDecimal totalStars = BigDecimal.ZERO;
	        
	        for (Review r : allReviews) {
	            totalStars = totalStars.add(new BigDecimal(r.getVote()));
	        }
	        
	        BigDecimal averageStar = totalStars.divide(new BigDecimal(allReviews.size()), 1, RoundingMode.HALF_UP);
	        product.setAverageStar(averageStar); // Cập nhật lại giá trị averageStar
			
			productDAO.update(product);

	        session.setAttribute("message", "Comment đã được thêm thành công!");
            session.setAttribute("alertType", "success");
	        response.sendRedirect("DetailServlet?id=" + id_product);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
