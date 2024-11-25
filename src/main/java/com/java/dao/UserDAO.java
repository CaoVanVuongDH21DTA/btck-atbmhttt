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
import com.java.utils.JpaUtils;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAO extends EntityDAO<User> {

	public UserDAO() {
		super(User.class);
		// TODO Auto-generated constructor stub
	}
	
	public List<User> getActive(boolean key){
		try {
			EntityManager em = JpaUtils.getEntityManager();
			
			TypedQuery<User> query = em.createNamedQuery("User.findActive", User.class);
			
			query.setParameter("key", key);
			
			return query.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public User getUser(String email, String password) {
		EntityManager em = JpaUtils.getEntityManager();
		
		try {
			String jpql = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password";
			
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			
			query.setParameter("email", email);
			
			query.setParameter("password", password);
			
			return query.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	public static boolean isUniqueEmail(String email) {
		EntityManager em = JpaUtils.getEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.email = :email";
			
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			
			query.setParameter("email", email);
			
			User user = query.getSingleResult();
			
			return user != null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	public String getMessage(User user) {
		if(user.getFullname().isEmpty()) {
			return "Hãy nhập fullname!";
		}else if(user.getEmail().isEmpty()) {
			return "Hãy nhập emal!";
		}else if(user.getPassword().isEmpty()) {
			return "Hãy nhập password!";
		}else if(user.getGender() == null) {
			return "Hãy chọn Giới tính!";
		}else if(isUniqueEmail(user.getEmail())) {
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

            // Lưu privateKey vào file hoặc hệ thống bên ngoài
            savePrivateKey(privateKey, user.getFullname());

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

    private void savePrivateKey(PrivateKey privateKey, String fullname) {
        try {
            // Định nghĩa tên tệp với fullname trong tên tệp
            String fileName = "private_key_" + fullname.replaceAll("\\s+", "_") + ".pem"; // thay thế khoảng trắng bằng "_"
            
            // Định nghĩa đường dẫn đến tệp private_key.pem trong thư mục src/key_user
            java.nio.file.Path path = java.nio.file.Paths.get("src/key_user/" + fileName);

            // Kiểm tra nếu thư mục chưa tồn tại, tạo thư mục
            java.nio.file.Path parentDir = path.getParent();
            if (parentDir != null && !java.nio.file.Files.exists(parentDir)) {
                java.nio.file.Files.createDirectories(parentDir);
            }

            // Mã hóa private key thành base64
            String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            // Ghi chuỗi base64 vào tệp
            java.nio.file.Files.write(path, base64PrivateKey.getBytes());

            System.out.println("Private key has been saved in base64 format to: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
