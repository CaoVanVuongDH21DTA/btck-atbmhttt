package com.java.dao;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
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
}