package com.java.site.controller;

import javax.crypto.Cipher;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
@WebServlet("/digitalsignature")
public class DigitalSignatureServlet extends HttpServlet {

    // Hiển thị trang ký hợp đồng
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng đến trang ký hợp đồng
        request.getRequestDispatcher("/views/sites/digital_signature.jsp").forward(request, response);
        
    }

    // Xử lý yêu cầu POST cho việc ký và xác minh chữ ký
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html; charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
        
    	String action = request.getParameter("action");

        if (action != null && action.equals("sign")) {
            handleSignRequest(request, response);
        } else if (action != null && action.equals("verify")) {
            handleVerifyRequest(request, response);
        } else {
            response.getWriter().write("Action không hợp lệ.");
        }
    }

 // Xử lý yêu cầu ký chữ ký
    private void handleSignRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");

        Part filePart = request.getPart("file");
        String privateKeyString = request.getParameter("privateKey");

        if (filePart == null || privateKeyString == null || privateKeyString.isEmpty()) {
            sendNotification(response, "Vui lòng chọn file và nhập private key.", "error");
            return;
        }

        // Đọc nội dung tệp từ InputStream
        InputStream fileContent = filePart.getInputStream();
        byte[] fileBytes = fileContent.readAllBytes();

        try {
            // Giải mã khóa riêng từ Base64 mà người dùng nhập vào
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

            // Khởi tạo đối tượng Signature để ký
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(fileBytes);

            // Ký dữ liệu
            byte[] signedData = signature.sign();

            // Chuyển chữ ký sang dạng Base64
            String base64Signature = Base64.getEncoder().encodeToString(signedData);

            // Đặt tên file cho file ký
            String signedFileName = filePart.getSubmittedFileName() + ".signed.txt";

            // Mã hóa tên file để hỗ trợ Unicode
            String encodedFileName = URLEncoder.encode(signedFileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");

            // Đặt tiêu đề để tải file
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            response.setHeader("Content-Type", "application/octet-stream");

            // Ghi dữ liệu chữ ký vào phản hồi
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(base64Signature.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            sendNotification(response, "Lỗi trong quá trình ký chữ ký: " + ex.getMessage(), "error");
        }
    }

    // Phương thức dùng để xác minh và kiểm tra file đã ký
    private void handleVerifyRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Part originalFilePart = request.getPart("originalFilePath");
        Part signedFilePart = request.getPart("signedFilePath");
        String publicKeyString = request.getParameter("publicKey");

        if (originalFilePart == null || signedFilePart == null || publicKeyString == null || publicKeyString.isEmpty()) {
            sendNotification(response, "Vui lòng tải lên cả tệp gốc, tệp đã ký và nhập public key.", "error");
            return;
        }

        try {
            // Đọc nội dung tệp gốc
            InputStream originalFileContent = originalFilePart.getInputStream();
            byte[] originalFileBytes = originalFileContent.readAllBytes();

            // Đọc nội dung tệp đã ký và giải mã Base64
            InputStream signedFileContent = signedFilePart.getInputStream();
            String base64Signature = new String(signedFileContent.readAllBytes(), StandardCharsets.UTF_8);
            byte[] signedFileBytes = Base64.getDecoder().decode(base64Signature);

            // Giải mã khóa công khai từ Base64
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            // Khởi tạo đối tượng Signature để xác minh chữ ký
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(originalFileBytes);

            // Xác minh chữ ký
            boolean isVerified = signature.verify(signedFileBytes);
            if (isVerified) {
                sendNotification(response, "Chữ ký hợp lệ!", "success");
            } else {
                sendNotification(response, "Chữ ký không hợp lệ!", "error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendNotification(response, "Lỗi trong quá trình xác minh chữ ký: " + ex.getMessage(), "error");
        }
    }

    // Phương thức để gửi thông báo dưới dạng HTML (modal hoặc bảng thông báo)
    private void sendNotification(HttpServletResponse response, String message, String type) throws IOException {
        String color = type.equals("success") ? "green" : "red";
        String htmlResponse = "<html><body>"
                + "<div style='padding: 20px; margin: 10px; border: 1px solid " + color + "; background-color: " + color + "; color: white; border-radius: 5px;'>"
                + message
                + "</div>"
                + "</body></html>";
        response.getWriter().write(htmlResponse);
    }
}