package com.java.dao;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.java.model.User;
import com.java.service.EmailService;
import com.java.utils.JpaUtils;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAO extends EntityDAO<User> {

    public UserDAO() {
        super(User.class);
    }

    public List<User> getActive(boolean key){
        try {
            EntityManager em = JpaUtils.getEntityManager();
            
            TypedQuery<User> query = em.createNamedQuery("User.findActive", User.class);
            
            query.setParameter("key", key);
            
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(String email, String password) {
        EntityManager em = JpaUtils.getEntityManager();
        
        try {
            String jpql = "SELECT u FROM User u WHERE u.email = :email";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("email", email);
            
            User user = query.getSingleResult();
            
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return user;  // Login success
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;  // Invalid email or password
    }

    public String getUserKeyById(int idUser) {
        EntityManager em = JpaUtils.getEntityManager();
        try {
            String jpql = "SELECT u.key FROM User u WHERE u.idUsers = :id";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            query.setParameter("id", idUser);

            return query.getSingleResult(); // Trả về user_key
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isUniqueEmail(String email) {
        EntityManager em = JpaUtils.getEntityManager();
        em.clear();  // Làm mới bộ nhớ cache của EntityManager
        try {
            String jpql = "SELECT u FROM User u WHERE u.email = :email";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("email", email);

            List<User> users = query.getResultList();
            
            // In ra số lượng người dùng tìm được với email đó
            System.out.println("Number of users found with email " + email + ": " + users.size());
            
            return !users.isEmpty();  // Kiểm tra xem email đã tồn tại hay chưa
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getMessage(User user) {
        if (user.getFullname().isEmpty()) {
            return "Hãy nhập fullname!";
        } else if (user.getEmail().isEmpty()) {
            return "Hãy nhập email!";
        } else if (user.getPassword().isEmpty()) {
            return "Hãy nhập password!";
        } else if (user.getGender() == null) {
            return "Hãy chọn Giới tính!";
        } else if (isUniqueEmail(user.getEmail())) {
            return "Email đã được đăng ký";
        }
        return null;
    }

    public void registerUser(User user) {
        EntityManager em = JpaUtils.getEntityManager();
        try {
            // Hash mật khẩu
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            // Tạo cặp khóa RSA
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Mã hóa publicKey thành chuỗi base64
            String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            // Lưu publicKey vào cơ sở dữ liệu (dưới dạng base64)
            user.setKey(base64PublicKey);

            // Gửi privateKey qua email
            String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            EmailService emailService = new EmailService();
            emailService.sendKey(user.getEmail(), base64PrivateKey, base64PublicKey);

            // Persist user vào cơ sở dữ liệu
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}
