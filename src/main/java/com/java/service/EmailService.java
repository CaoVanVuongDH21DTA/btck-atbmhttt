package com.java.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    public void sendPrivateKeyEmail(String userEmail, String base64PrivateKey) {
        String to = userEmail;
        String from = "gearpro.shop.2024@gmail.com";

        // Lấy mật khẩu từ biến môi trường
        String emailPassword = System.getenv("EMAIL_PASSWORD");

        // Cấu hình SMTP
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, emailPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Key của bạn");

            // Nội dung HTML
            String emailContent = "<h1>Chào bạn,</h1>" +
                    "<p>Đây là <b>private key</b> của bạn dùng để ký hợp đồng khi xác nhận đơn hàng thành công:</p>" +
                    "<pre>" + base64PrivateKey + "</pre>" +
                    "<p>Vui lòng giữ nó thật cẩn thận.</p>" +
                    "<p>Trân trọng,<br/>GearPro Shop</p>";
            message.setContent(emailContent, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("Email đã được gửi thành công đến: " + to);

        } catch (MessagingException e) {
            // Không làm gián đoạn quy trình
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}

