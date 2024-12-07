package com.java.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.java.model.Manager;
import com.java.utils.JpaUtils;

public class ManagerDAO extends EntityDAO<Manager> {

	public ManagerDAO() {
		super(Manager.class);
		// TODO Auto-generated constructor stub
	}
	
	public Manager getManager(String username, String password) {
	    EntityManager em = JpaUtils.getEntityManager();
	    try {
	        TypedQuery<Manager> query = em.createQuery("SELECT m FROM Manager m WHERE m.username = :username AND m.password = :password", Manager.class);
	        query.setParameter("username", username);
	        query.setParameter("password", password);
	        
	        return query.getSingleResult();
	    } catch (Exception e) {
	        e.printStackTrace(); // Log exception
	        return null;
	    } finally {
	        em.close();
	    }
	}
}
