package com.java.utils;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtils {
    
    private static EntityManagerFactory factory;
    private static EntityManager em;

    // Các hằng số để sử dụng trong việc cấu hình JDBC
    public static final String FINAL_URL = "javax.persistence.jdbc.url";
    public static final String FINAL_USER = "javax.persistence.jdbc.user";
    public static final String FINAL_PASSWORD = "javax.persistence.jdbc.password";
    public static final String FINAL_DRIVER = "javax.persistence.jdbc.driver";

    // Trả về EntityManagerFactory nếu chưa có
    public static EntityManagerFactory getFactory() {
        Map<String, String> map = new HashMap<>();
        
        // Cấu hình thông tin kết nối tới database, sử dụng thông tin này trong map
        map.put(FINAL_URL, "jdbc:mysql://localhost:3306/apptmdt");
        map.put(FINAL_USER, "root");
        map.put(FINAL_PASSWORD, "1132003");
        map.put(FINAL_DRIVER, "com.mysql.cj.jdbc.Driver");
        
        // Nếu chưa có factory hoặc factory đã đóng, tạo mới một EntityManagerFactory
        if (factory == null || !factory.isOpen()) {
            factory = Persistence.createEntityManagerFactory("btck-atbmhttt", map);
        }
        
        return factory;
    }

    // Trả về EntityManager nếu chưa có hoặc đã đóng
    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            em = getFactory().createEntityManager();
        }
        
        return em;
    }

    // Kiểm tra kết nối thành công
    public static void main(String[] args) {
        EntityManager em = JpaUtils.getEntityManager();
        if (em != null) {
            System.out.println("Kết nối thành công!");
        } else {
            System.out.println("Kết nối không thành công!");
        }
    }
}
