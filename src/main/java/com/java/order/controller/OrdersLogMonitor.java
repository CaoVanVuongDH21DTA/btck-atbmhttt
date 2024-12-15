package com.java.order.controller;

import com.java.model.OrdersLog;
import com.java.service.EmailService;
import com.java.utils.JpaUtils;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;

public class OrdersLogMonitor {
    private static final long CHECK_INTERVAL = 10000; 
    private Timestamp lastCheckedTime = new Timestamp(System.currentTimeMillis()); 
    private final EmailService emailService = new EmailService(); 

    public void startMonitoring() {
        Thread monitorThread = new Thread(() -> {
            while (true) {
                try {
                    checkForChanges();
                    Thread.sleep(CHECK_INTERVAL); // Kiểm tra thay đổi sau mỗi 3 giây
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        monitorThread.setDaemon(true); // Để chương trình chính có thể tắt mà không chờ thread này
        monitorThread.start();
    }

    private void checkForChanges() {
        EntityManager em = JpaUtils.getEntityManager();
        try {
            em.getTransaction().begin();

            // Truy vấn các bản ghi mới được thay đổi
            String jpql = "SELECT l FROM OrdersLog l WHERE l.changedAt > :lastCheckedTime";
            Query query = em.createQuery(jpql);
            query.setParameter("lastCheckedTime", lastCheckedTime);

            List<OrdersLog> changes = query.getResultList();
            if (!changes.isEmpty()) {
                System.out.println("Có thay đổi mới trong bảng orders_log.");

                for (OrdersLog change : changes) {
                    int orderId = change.getIdOrders();
                    int userId = change.getUserId();
                    String action = change.getAction();
                    Timestamp changedAt = change.getChangedAt();
                    String details = change.getDetails();

                    // Kiểm tra thông tin của bản ghi
                    System.out.println("Order ID: " + orderId + ", User ID: " + userId + ", Action: " + action + ", Changed At: " + changedAt);

                    // Lấy email của người dùng
                    String userEmail = getUserEmail(userId);
                    if (userEmail != null) {
                        // Gửi email thông báo
                        System.out.println("Đang gửi email đến: " + userEmail);
                        emailService.sendOrderStatusChangeEmail(userEmail, String.valueOf(orderId), action, details, changedAt.toString());
                    } else {
                        System.out.println("Không tìm thấy email cho user ID: " + userId);
                    }
                }

                // Cập nhật thời gian kiểm tra cuối
                lastCheckedTime = new Timestamp(System.currentTimeMillis());
                System.out.println("Đã cập nhật thời gian kiểm tra cuối: " + lastCheckedTime);
            } else {
                System.out.println("Không có thay đổi nào mới.");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Phương thức lấy email trong user
    private String getUserEmail(int userId) {
        EntityManager em = JpaUtils.getEntityManager();
        try {
            String jpql = "SELECT u.email FROM User u WHERE u.idUsers = :userId"; 
            Query query = em.createQuery(jpql);
            query.setParameter("userId", userId);
            return (String) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        OrdersLogMonitor monitor = new OrdersLogMonitor();
        monitor.startMonitoring();

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
