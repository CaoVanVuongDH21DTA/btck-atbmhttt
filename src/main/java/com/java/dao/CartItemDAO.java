package com.java.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.java.model.CartItem;
import com.java.utils.JpaUtils;

public class CartItemDAO extends EntityDAO<CartItem> {

	public CartItemDAO() {
		super(CartItem.class);
		// TODO Auto-generated constructor stub
	}
	
	public List<CartItem> getByUser(int id_user){
		try {
			EntityManager em = JpaUtils.getEntityManager();
			
			String jpql = "SELECT c FROM CartItem c WHERE c.user.idUsers = :id";
			
			TypedQuery<CartItem> query =  em.createQuery(jpql, CartItem.class);
			
			query.setParameter("id", id_user);
			
			return query.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		
		CartItemDAO cartItemDAO = new CartItemDAO();
		
		List<CartItem> list = cartItemDAO.getByUser(1);
		
		for(CartItem cartItem: list) {
			System.out.println(cartItem.toString());
		}
	}
}
