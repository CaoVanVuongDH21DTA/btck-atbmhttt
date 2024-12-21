package com.java.dao;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.java.model.User;
import com.java.service.EmailService;
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
	
	public void unlockUser(int userId) {
	    EntityManager em = JpaUtils.getEntityManager();
	    EntityTransaction trans = em.getTransaction();

	    try {
	        trans.begin();
	        User user = em.find(User.class, userId);
	        if (user != null) {
	            user.setActive(true); // Mở khóa tài khoản
	            em.merge(user); // Cập nhật trạng thái
	        }
	        trans.commit();
	    } catch (Exception e) {
	        if (trans.isActive()) {
	            trans.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
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
	
	public User findUserByEmail(String email) {
	    EntityManager em = JpaUtils.getEntityManager();
	    try {
	        String jpql = "SELECT u FROM User u WHERE u.email = :email";
	        TypedQuery<User> query = em.createQuery(jpql, User.class);
	        query.setParameter("email", email);

	        return query.getSingleResult(); // Trả về đối tượng User nếu tìm thấy
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        em.close(); // Đảm bảo đóng EntityManager
	    }
	    return null; // Không tìm thấy hoặc xảy ra lỗi
	}

	public void updatePassword(String email, String newPassword) {
	    EntityManager em = JpaUtils.getEntityManager();
	    EntityTransaction trans = em.getTransaction();

	    try {
	        trans.begin();
	        
	        System.out.println("email: " + email);
	        System.out.println("password: " + newPassword);
	        
	        // Tìm user qua email
	        String jpql = "SELECT u FROM User u WHERE u.email = :email";
	        TypedQuery<User> query = em.createQuery(jpql, User.class);
	        query.setParameter("email", email);

	        User user = query.getSingleResult();
	        if (user != null) {
	            // Hash mật khẩu mới
	            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
	            user.setPassword(hashedPassword);

	            // Cập nhật user
	            em.merge(user);
	        }

	        trans.commit();
	    } catch (Exception e) {
	        if (trans.isActive()) {
	            trans.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        em.close(); // Đảm bảo đóng EntityManager
	    }
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
	    return null; // Nếu không tìm thấy hoặc có lỗi
	}
	
	public static boolean isUniqueEmail(String email) {
	    EntityManager em = JpaUtils.getEntityManager();
	    em.clear();
	    try {
	        String jpql = "SELECT u FROM User u WHERE u.email = :email";
	        
	        TypedQuery<User> query = em.createQuery(jpql, User.class);
	        query.setParameter("email", email);
	        
	        // Nếu tồn tại người dùng với email này, trả về true (email đã được đăng ký)
	        User user = query.getSingleResult();
	        return user != null;
	    } catch (NoResultException e) {
	        return false;
	    } catch (Exception e) {
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
		}else if(!isUniqueEmail(user.getEmail())) {
			return "Email đã được đăng ký";
		}
		return null;
	}
	
	public Integer getUserIdByEmail(String email) {
        EntityManager em = JpaUtils.getEntityManager();
        try {
            String jpql = "SELECT u.idUsers FROM User u WHERE u.email = :email";
            TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
            query.setParameter("email", email);

            return query.getSingleResult(); // Trả về id của user nếu tìm thấy
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close(); // Đảm bảo đóng EntityManager
        }
        return null; // Nếu không tìm thấy hoặc có lỗi
    }
	
//	Người dùng đã có key
	public void registerUserWithExistingKey(User user) {
	    EntityManager em = JpaUtils.getEntityManager();
	    try {
	        // Hash mật khẩu
	        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
	        user.setPassword(hashedPassword);

	        // Ghi user vào cơ sở dữ liệu (sử dụng key đã nhập)
	        em.getTransaction().begin();
	        em.persist(user);
	        em.getTransaction().commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	}

//	Người dùng chưa có key
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
