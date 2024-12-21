package com.java.auth.controller;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.java.dao.UserDAO;
import com.java.model.Account;
import com.java.model.User;
import com.java.utils.CookieUtils;
import com.java.utils.SessionUtils;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    HttpSession session = req.getSession(false); // Không tạo session mới nếu không tồn tại
	    if (session == null || session.getAttribute("user") == null) {
	        // Nếu session không tồn tại hoặc không có user trong session, yêu cầu đăng nhập lại
	        req.setAttribute("message", "Vui lòng đăng nhập");
	        req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
	    } else {
	        // Nếu session còn hiệu lực, tiếp tục xử lý bình thường
	        req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	    	HttpSession session = request.getSession();
	    	
	        UserDAO userDAO = new UserDAO();

	        Account account = new Account();
	        BeanUtils.populate(account, request.getParameterMap());

	        // Lấy thông tin user và kiểm tra mật khẩu đã mã hóa
	        User user = userDAO.getUser(account.getEmail(), account.getPassword());

	        boolean b_isValid = user != null;

	        if (b_isValid) {
	        	if (!user.isActive()) {
	        	    String privateKey = request.getParameter("privateKey");

	        	    if (privateKey != null && !privateKey.isEmpty()) {
	        	        boolean isKeyValid = validateKeyPair(user.getKey(), privateKey);

	        	        if (isKeyValid) {
	        	            userDAO.unlockUser(user.getIdUsers());
	        	            session.removeAttribute("lockedUser");
	        	            session.setAttribute("message", "Tài khoản đã được mở khóa. Đăng nhập lại!");
	        	            response.sendRedirect("/btck-atbmhttt/LoginServlet");
	        	        } else {
	        	            session.setAttribute("lockedUser", user); // Đặt giá trị `lockedUser`
	        	            session.setAttribute("message", "Khóa riêng tư không hợp lệ!");
	        	            response.sendRedirect("/btck-atbmhttt/LoginServlet");
	        	        }
	        	    } else {
	        	        session.setAttribute("lockedUser", user); // Đặt giá trị `lockedUser`
	        	        session.setAttribute("message", "Tài khoản bị khóa. Nhập khóa riêng tư để mở lại hoặc liên hệ với Shop!");
	        	        response.sendRedirect("/btck-atbmhttt/LoginServlet");
	        	    }
	        	}
    			else {
	                // Kiểm tra ghi nhớ tài khoản
	                boolean b_isRemember = account.isRemember();

	                if (b_isRemember) {
	                    // Thêm cookie
	                    CookieUtils cookieUtils = new CookieUtils();
	                    cookieUtils.addCookie(response, "email", account.getEmail(), 1);
	                }

	                // Thêm session
	                SessionUtils sessionUtils = new SessionUtils();
	                sessionUtils.setSession(request, "user", user);
	                
	                response.sendRedirect("HomeServlet");
	            }
	        } else {
	            session.setAttribute("message", "Tài khoản hoặc mật khẩu sai. Thử lại!");
	            response.sendRedirect("/btck-atbmhttt/LoginServlet");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static boolean validateKeyPair(String publicKeyStr, String privateKeyStr) {
	    try {
	        // Chuyển đổi publicKey từ String sang PublicKey
	        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
	        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

	        // Chuyển đổi privateKey từ String sang PrivateKey
	        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
	        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
	        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

	        // Dữ liệu mẫu để kiểm tra
	        String sampleData = "test";
	        byte[] sampleBytes = sampleData.getBytes();

	        // Mã hóa bằng privateKey
	        Cipher cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
	        byte[] encryptedData = cipher.doFinal(sampleBytes);

	        // Giải mã bằng publicKey
	        cipher.init(Cipher.DECRYPT_MODE, publicKey);
	        byte[] decryptedData = cipher.doFinal(encryptedData);

	        // So sánh dữ liệu gốc với dữ liệu đã giải mã
	        return sampleData.equals(new String(decryptedData));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}
