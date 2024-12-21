package com.java.dao;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.java.utils.JpaUtils;

public class SupportDAO {

	public Timestamp getOldKeyCreatedTime(String email, String publicKey) {
	    Timestamp createdAt = null;
	    EntityManager em = null;

	    try {
	        em = JpaUtils.getEntityManager();
	        String sql = "SELECT created_at FROM user_key_history WHERE user_id = (SELECT id_users FROM users WHERE email = :email) AND old_key = :publicKey";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("email", email);
	        query.setParameter("publicKey", publicKey);

	        Object result = query.getSingleResult();
	        if (result != null) {
	            createdAt = (Timestamp) result;
	        }
	    } catch (NoResultException e) {
	        // Không tìm thấy thời gian tạo key cũ
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (em != null && em.isOpen()) {
	            em.close();
	        }
	    }

	    return createdAt;
	}

	public String getPublicKeyFromUser(String email) {
	    String publicKey = null;
	    EntityManager em = null;

	    try {
	        em = JpaUtils.getEntityManager();

	        // Truy vấn public_key từ bảng users theo email
	        String sql = "SELECT user_key FROM users WHERE email = :email";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("email", email);

	        Object result = query.getSingleResult();
	        if (result != null) {
	            publicKey = result.toString();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (em != null && em.isOpen()) {
	            em.close();
	        }
	    }

	    return publicKey;
	}

}