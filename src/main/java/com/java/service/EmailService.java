package com.java.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    // Gửi email với private key và public key
    public void sendKey(String userEmail, String base64PrivateKey, String base64PublicKey) {
        String to = userEmail;  
        String from = "gearpro.shop.2024@gmail.com";  

        // Cấu hình các thuộc tính cho kết nối SMTP
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Tạo phiên làm việc
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gearpro.shop.2024@gmail.com", "jytbkuoffatojafe");
            }
        });

        try {
            // Tạo tin nhắn email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Khóa bí mật và khóa công khai");

            String content = "<p>Dưới đây là khóa bí mật và khóa công khai của bạn:</p>" +
                             "<p>Khóa bí mật: " + base64PrivateKey + "</p>" +
                             "<p>Khóa công khai: " + base64PublicKey + "</p>";

            message.setContent(content, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);
            System.out.println("Email đã gửi thành công đến: " + to);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
    
    // Phương thức kiểm tra sự thay đổi dữ liệu đơn hàng trong data
    public void sendOrderStatusChangeEmail(String userEmail, String orderId, String action, String details, String changedAt) {
        String to = userEmail;  
        String from = "gearpro.shop.2024@gmail.com";  

        // Cấu hình các thuộc tính cho kết nối SMTP
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Tạo phiên làm việc
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gearpro.shop.2024@gmail.com", "jytbkuoffatojafe");
            }
        });

        try {
            // Tạo tin nhắn email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Xác nhận thay đổi trạng thái đơn hàng #" + orderId);

            // Tạo các liên kết xác nhận và xóa đơn hàng
            String confirmationLink = "http://localhost:8080/btck-atbmhttt/OrderVerificationServlet?orderId=" + orderId + "&confirmation=yes";
            String deleteLink = "http://localhost:8080/btck-atbmhttt/OrderVerificationServlet?orderId=" + orderId + "&confirmation=delete";

            // Nội dung email
            String emailContent = "<h1>Xác nhận thay đổi trạng thái đơn hàng</h1>"
                    + "<p>Thông tin đơn hàng của bạn đã được cập nhật:</p>"
                    + "<ul>"
                    + "<li><b>Mã đơn hàng:</b> " + orderId + "</li>"
                    + "<li><b>Hành động:</b> " + action + "</li>"
                    + "<li><b>Chi tiết:</b> " + details + "</li>"
                    + "<li><b>Thời gian thay đổi:</b> " + changedAt + "</li>"
                    + "</ul>"
                    + "<p>Nếu bạn đúng là người thay đổi vui click vào link dưới đây để xác nhận:</p>"
                    + "<a href=\"" + confirmationLink + "\">Đúng là tôi</a>"
                    + "<p>Nếu không phải bạn vui lòng click vào link dưới đây để xóa đơn hàng ngay lập tức:</p>"
                    + "<a href=\"" + deleteLink + "\">Không phải tôi</a>";

            message.setContent(emailContent, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);
            System.out.println("Email đã gửi thành công đến: " + to);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}
