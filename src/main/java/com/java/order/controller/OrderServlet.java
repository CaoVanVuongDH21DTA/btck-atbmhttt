package com.java.order.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.PageInfo;
import com.java.common.PageType;
import com.java.dao.OrderDAO;
import com.java.model.User;
import com.java.service.EmailService;
import com.java.model.Order;
import com.java.utils.SessionUtils;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public OrderServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        try {
            OrderDAO orderDAO = new OrderDAO();
            SessionUtils sessionUtils = new SessionUtils();
            User user = (User) sessionUtils.getSession(request, "user");

            List<Order> listOrders = orderDAO.getByUser(user.getIdUsers());

            // Kiểm tra nếu danh sách đơn hàng rỗng
            if (listOrders == null || listOrders.isEmpty()) {
                request.setAttribute("message", "Empty List");
            }

            request.setAttribute("listOrders", listOrders);
            request.setAttribute("flag", "Orders");

            PageInfo.routeSite(request, response, PageType.ORDER_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("updateOrderStatus".equals(action)) {
            updateOrderStatus(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");

            // Lấy Order từ cơ sở dữ liệu
            OrderDAO orderDAO = new OrderDAO();
            EmailService emailService = new EmailService();

            // Tìm đơn hàng theo ID
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                response.getWriter().write("Không tìm thấy đơn hàng với ID: " + orderId);
                return;
            }

            // Lưu thay đổi trạng thái vào cơ sở dữ liệu
            boolean isUpdated = orderDAO.updateOrderDetails(orderId, address, phone);

            if (!isUpdated) {
                response.getWriter().write("Không thể cập nhật trạng thái đơn hàng.");
                return;
            }

            // Sau khi lưu thay đổi, gửi email xác nhận
            String confirmationLink = "http://localhost:8080/btck-atbmhttt/OrderVerificationServlet?orderId=" 
                                       + orderId + "&confirmation=yes";
            String deleteLink = "http://localhost:8080/btck-atbmhttt/OrderVerificationServlet?orderId=" 
                                + orderId + "&confirmation=delete";

            String emailContent = "<h1>Xác nhận thay đổi trạng thái đơn hàng</h1>"
                    + "<p>Thông tin đơn hàng của bạn đã được cập nhật:</p>"
                    + "<ul>"
                    + "<li><b>Mã đơn hàng:</b> " + orderId + "</li>"
                    + "<li><b>Địa chỉ:</b> " + address + "</li>"
                    + "<li><b>Số điện thoại:</b> " + phone + "</li>"
                    + "</ul>"
                    + "<p>Nếu bạn đúng là người thay đổi vui click vào link dưới đây để xác nhận.</p>"
                    +"<a href=\"" + confirmationLink + "\">Đúng là tôi</a>"
                    + "<p>Nếu không phải bạn vui lòng click vào link dưới đây để xóa đơn hàng ngay lập tức</p>"
                    + "<a href=\"" + deleteLink + "\">Không phải tôi</a>";

            try {
                String customerEmail = order.getUser().getEmail();
                emailService.sendEmail(customerEmail, "Xác nhận thay đổi đơn hàng", emailContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
