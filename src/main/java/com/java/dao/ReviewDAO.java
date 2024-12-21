package com.java.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.java.model.Review;
import com.java.utils.JpaUtils;

public class ReviewDAO extends EntityDAO<Review> {

	public ReviewDAO() {
		super(Review.class);
		// TODO Auto-generated constructor stub
	}

	public List<Review> getByProduct(int id_product){
		try {
			EntityManager em = JpaUtils.getEntityManager();
			
			String jpql = "SELECT r FROM Review r WHERE r.product.idProducts = :id";
			
			TypedQuery<Review> query = em.createQuery(jpql, Review.class);
			
			query.setParameter("id", id_product);
			
			return query.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
