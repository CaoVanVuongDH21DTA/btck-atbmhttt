package com.java.cart.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java.dao.CartItemDAO;
import com.java.dao.OrderDAO;
import com.java.dao.OrderItemDAO;
import com.java.dao.ProductDAO;
import com.java.model.CartItem;
import com.java.model.User;
import com.java.model.Order;
import com.java.model.OrderItem;
import com.java.model.Product;
import com.java.utils.OrderUtils;
import com.java.utils.SessionUtils;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            SessionUtils sessionUtils = new SessionUtils();
            User user = (User) sessionUtils.getSession(request, "user");

            if (user == null) {
                response.sendRedirect("LoginServlet"); 
                return;
            }

            OrderDAO orderDAO = new OrderDAO();
            OrderItemDAO orderItemDAO = new OrderItemDAO();
            ProductDAO productDAO = new ProductDAO();
            CartItemDAO cartItemDAO = new CartItemDAO();

            List<CartItem> listCartItems = cartItemDAO.getByUser(user.getIdUsers());

            if (listCartItems.isEmpty()) {
                response.sendRedirect("CartServlet"); 
            } else {
                double num_amount = 0;
                for (CartItem cartItem : listCartItems) {
                    Product product = cartItem.getProduct();
                    double price = product.getPrice();
                    num_amount += (price * cartItem.getQuantity());
                }

                Date date_current = new Date(System.currentTimeMillis());
                String str_address = request.getParameter("address");
                String str_phone = request.getParameter("phone");

                Order order = new Order();
                order.setAmount(num_amount);
                order.setCreated(date_current);
                order.setAddress(str_address);
                order.setPhone(str_phone);
                order.setStatus("Chờ Duyệt");
                order.setUser(user);

                orderDAO.insertByOrder(order);
                
                // Tạo mã hash để xác thực đơn hàng
                String secret = "DaiHocNongLamCNTTHCM";
                String confirmationHash = OrderUtils.generateConfirmationHash(order.getIdOrders(), date_current.toString(), secret);
                order.setHashCode(confirmationHash);

                // Cập nhật đơn hàng với mã hash
                orderDAO.updateOrderHash(order);
                
                for (CartItem cartItem : listCartItems) {
                    OrderItem orderItem = new OrderItem();
                    Product current_Product = productDAO.findById(cartItem.getProduct().getIdProducts());
                    orderItem.setProduct(current_Product);
                    orderItem.setOrder(order);
                    orderItem.setQuantity(cartItem.getQuantity());

                    double currrent_Price = current_Product.getPrice(); // Removed discount
                    orderItem.setPrice(currrent_Price);
                    orderItemDAO.insertByOrderItem(orderItem);
                }

                // Xóa sản phẩm khỏi giỏ hàng
                for (CartItem cartItem : listCartItems) {
                    cartItemDAO.delete(cartItem.getIdCartItem());
                }

                // Send email confirmation
                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.starttls.required", "true");
                properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

                // Lấy thông tin xác thực email
                String username = "gearpro.shop.2024@gmail.com";
                String password = "jytbkuoffatojafe";

                if (username == null || password == null) {
                    throw new IllegalArgumentException("Thiếu biến môi trường cho thông tin đăng nhập email.");
                }

                // Tạo phiên với thông tin đăng nhập email
                Session emailSession = Session.getInstance(properties, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                    	return new PasswordAuthentication(username, password);
                    }
                });

                MimeMessage message = new MimeMessage(emailSession);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                
                // Tạo nội dung email chỉ chứa mã hash
                String emailContent = "Đơn hàng của bạn đã được đặt thành công vào lúc " + date_current.toString() +
                    "<br/>Mã xác nhận: " + confirmationHash + " <br/> Tuyệt đối không được chia sẻ mã này cho bất kì ai";

                message.setText(emailContent, "UTF-8", "html");
                message.setReplyTo(message.getFrom());

                Transport.send(message);

                // Hiển thị thông báo bằng alert và chuyển hướng
                PrintWriter out = response.getWriter();
                out.println("<html>");
                out.println("<head>");
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Đơn hàng đã được tạo thành công!\\nVui lòng kiểm tra email của bạn để xác nhận đơn hàng mã " + order.getIdOrders() + "');");
                out.println("window.location.href = 'OrderServlet';"); 
                out.println("</script>");
                out.println("</head>");
                out.println("<body>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
