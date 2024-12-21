package com.java.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    private static final String EMAIL = "gearpro.shop.2024@gmail.com";
    private static final String PASSWORD = "jytbkuoffatojafe";

    // Cấu hình Session chung cho mọi email
    private Session configureMailSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });
    }

    // Phương thức gửi email chung
    public void sendEmail(String to, String subject, String content, boolean isHtml) {
        try {
            Session session = configureMailSession();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            if (isHtml) {
                message.setContent(content, "text/html; charset=UTF-8");
            } else {
                message.setText(content);
            }
            Transport.send(message);
            System.out.println("Email đã gửi thành công đến: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }

    // Gửi key khi tạo tài khoản
    public void sendKey(String userEmail, String base64PrivateKey, String base64PublicKey) {
        String subject = "Key của bạn";
        String content = "Gửi bạn,\n\n" +
                "Đây là key của bạn dùng để ký hợp đồng khi xác nhận đơn hàng thành công:\n" +
                base64PrivateKey + "\n\n" +
                "Đây là key công khai của bạn: \n" +
                base64PublicKey + "\n\n" +
                "Vui lòng, hãy giữ nó thật cẩn thận.\n\nTrân trọng,\nĐơn đăng ký của bạn đã thành công.";
        sendEmail(userEmail, subject, content, false);
    }

    // Gửi thông báo thay đổi trạng thái đơn hàng
    public void sendOrderStatusChangeEmail(String userEmail, String orderId, String action, String details, String changedAt) {
        String subject = "Xác nhận thay đổi trạng thái đơn hàng #" + orderId;
        String confirmationLink = "http://localhost:8080/btck-atbmhttt/OrderVerificationServlet?orderId=" + orderId + "&confirmation=yes";
        String deleteLink = "http://localhost:8080/btck-atbmhttt/OrderVerificationServlet?orderId=" + orderId + "&confirmation=delete";
        String content = "<h1>Xác nhận thay đổi trạng thái đơn hàng</h1>" +
                "<p>Thông tin đơn hàng của bạn đã được cập nhật:</p>" +
                "<ul>" +
                "<li><b>Mã đơn hàng:</b> " + orderId + "</li>" +
                "<li><b>Hành động:</b> " + action + "</li>" +
                "<li><b>Chi tiết:</b> " + details + "</li>" +
                "<li><b>Thời gian thay đổi:</b> " + changedAt + "</li>" +
                "</ul>" +
                "<p>Nếu bạn đúng là người thay đổi vui click vào link dưới đây để xác nhận:</p>" +
                "<a href=\"" + confirmationLink + "\">Đúng là tôi</a>" +
                "<p>Nếu không phải bạn vui lòng click vào link dưới đây để xóa đơn hàng ngay lập tức:</p>" +
                "<a href=\"" + deleteLink + "\">Không phải tôi</a>";
        sendEmail(userEmail, subject, content, true);
    }

    // Gửi email xác nhận đăng ký tài khoản
    public void sendConfirmationEmail(String userEmail, String base64PublicKey) {
        String subject = "Đăng ký tài khoản thành công";
        String content = "<p>Chúc mừng bạn đã đăng ký tài khoản thành công!</p>" +
                "<p>Gmail bạn đã đăng ký: </p>" +
                "<ul><li>" + userEmail + "</li></ul>" +
                "<p>Khóa công khai bạn đã đăng ký: </p>" +
                "<ul><li>" + base64PublicKey + "</li></ul>" +
                "<p>Hãy nhớ bảo quản thật tốt khóa bí mật của bạn.</p>";
        sendEmail(userEmail, subject, content, true);
    }
}
