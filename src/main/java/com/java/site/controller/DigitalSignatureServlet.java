package com.java.site.controller;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if ("sign".equals(action)) {
        	handleSignRequest(request, response);
        } else if ("verify".equals(action)) {
            handleVerifyRequest(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

 // Xử lý yêu cầu ký chữ ký
    private void handleSignRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Part filePart = request.getPart("file");
        String privateKeyString = request.getParameter("privateKey");

        if (filePart == null || privateKeyString == null || privateKeyString.isEmpty()) {
            sendNotification(response, "Vui lòng chọn file và nhập private key.", "error");
            return;
        }

        // Đọc nội dung tệp từ InputStream
        InputStream fileContent = filePart.getInputStream();
        byte[] fileBytes = new byte[fileContent.available()];
        fileContent.read(fileBytes);

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
            
            //Chuyển chữ ký sang dạng base64
            String base64Signature = Base64.getEncoder().encodeToString(signedData);

            // Xác định thư mục Downloads
            String userHome = System.getProperty("user.home");
            Path downloadPath = Paths.get(userHome, "Downloads");

            // Đảm bảo thư mục tồn tại
            if (!Files.exists(downloadPath)) {
                Files.createDirectories(downloadPath);
            }

            // Lưu chữ ký vào thư mục Downloads với tên tệp mới
            String signedFileName = filePart.getSubmittedFileName() + ".signed";
            Path signedFilePath = downloadPath.resolve(signedFileName);
            Files.writeString(signedFilePath, base64Signature, StandardCharsets.UTF_8);

            // Phản hồi cho người dùng
            sendNotification(response, "Chữ ký đã được tạo thành công! Tệp được lưu tại: " + signedFilePath.toString(), "success");
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
                sendNotification(response, "Kiểm tra lại tệp hoặc Chữ ký không hợp lệ!", "error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendNotification(response, "Lỗi trong quá trình xác minh chữ ký: " + ex.getMessage(), "error");
        }
    }

    // Phương thức để gửi thông báo dưới dạng HTML (modal hoặc bảng thông báo)
    private void sendNotification(HttpServletResponse response, String message, String type) throws IOException {
        String color = type.equals("success") ? "green" : "red";
        String redirectUrl = "http://localhost:8080/btck-atbmhttt/digitalsignature";
        String htmlResponse = "<html><body>"
                + "<meta http-equiv='refresh' content='10;url=" + redirectUrl + "' />"
                + "<script>"
                + "let countdown = 10;" // Thời gian đếm ngược
                + "function updateCountdown() {"
                + "  document.getElementById('countdown').innerText = countdown;"
                + "  if (countdown > 0) {"
                + "    countdown--;"
                + "    setTimeout(updateCountdown, 1000);"
                + "  }"
                + "}"
                + "updateCountdown();"
                + "</script>"
                + "<div style='padding: 20px; margin: 10px; border: 1px solid " + color + "; background-color: " + color + "; color: white; border-radius: 5px;'>"
                + message + "<br>"
                + "Chuyển hướng sau: <span id='countdown'>10</span> giây"
                + "</div>"
                + "</body></html>";
        response.getWriter().write(htmlResponse);
    }
    
//    //khi người dùng rp báo mất privatekey thì xử lý bằng cách lấy time lúc báo mất và publickey chỉ được kiểm tra hợp đồng trước thời gian đó còn sau thời gian đó thì sẽ báo lỗi
// // Lưu thông tin thời gian mất khóa
//    Timestamp lostPrivateKeyTimestamp = new Timestamp(System.currentTimeMillis());
//    user.setLostPrivateKeyTime(lostPrivateKeyTimestamp);
//    public boolean verifyContract(String contractData, String signature, PublicKey publicKey, Timestamp lostPrivateKeyTimestamp) {
//        // Kiểm tra thời gian hợp đồng đã ký
//        Timestamp contractTimestamp = getContractTimestamp(contractData);
//
//        // Nếu hợp đồng được ký sau thời gian báo mất privateKey, từ chối
//        if (contractTimestamp.after(lostPrivateKeyTimestamp)) {
//            return false; // Hợp đồng không hợp lệ
//        }
//
//        // Nếu hợp đồng được ký trước thời gian báo mất, xác thực chữ ký
//        return verifySignature(contractData, signature, publicKey);
//    }


}
