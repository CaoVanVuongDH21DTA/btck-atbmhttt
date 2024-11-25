package com.java.utils;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtils {
    private static EntityManagerFactory factory;
    private static EntityManager em;

    public static final String FINAL_URL = "javax.persistence.jdbc.url";
    public static final String FINAL_USER = "javax.persistence.jdbc.user";
    public static final String FINAL_PASSWORD = "javax.persistence.jdbc.password";
    public static final String FINAL_DRIVER = "javax.persistence.jdbc.driver";

    public static EntityManagerFactory getFactory() {
        Map<String, String> map = new HashMap<>();
        
        // Nhập trực tiếp giá trị cấu hình ở đây
        map.put(FINAL_URL, "jdbc:mysql://localhost:3306/apptmdt");
        map.put(FINAL_USER, "apptmdt");
        map.put(FINAL_PASSWORD, "1");
        map.put(FINAL_DRIVER, "com.mysql.cj.jdbc.Driver");
        
        if (factory == null || !factory.isOpen()) {
            factory = Persistence.createEntityManagerFactory("btck-atbmhttt", map);
        }
        
        return factory;
    }

    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            em = getFactory().createEntityManager();
        }
        
        return em;
    }

    public static void main(String[] args) {
        EntityManager em = JpaUtils.getEntityManager();
        if (em != null) {
            System.out.println("OK");
        } else {
            System.out.println("NOT");
        }
    }
}
