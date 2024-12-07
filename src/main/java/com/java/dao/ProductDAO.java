package com.java.dao;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.java.model.Product;
import com.java.utils.JpaUtils;

public class ProductDAO extends EntityDAO<Product> {

	public ProductDAO() {
		super(Product.class);
	}
	
	// Hàm lấy ngẫu nhiên 6 sản phẩm
	public List<Product> getRandom() {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			
			String sql = "SELECT p FROM Product p ORDER BY RAND()";
			TypedQuery<Product> query = em.createQuery(sql, Product.class);
			
			return query.setMaxResults(6).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
		return null;
	}

	// Hàm tính tổng tiền giỏ hàng
	public double getAmount(List<Product> listProducts, List<Integer> listQuantities) {
		double num_amount = 0;
		for (int i = 0; i < listProducts.size(); i++) {
			double num_currentPrice = (100 - listProducts.get(i).getDiscount().getPercent()) * listProducts.get(i).getPrice() / 100;
			num_amount += (num_currentPrice * listQuantities.get(i));
		}
		return num_amount;
	}
	
	// Hàm tìm sản phẩm theo tên
	public List<Product> findByName(String name) {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			TypedQuery<Product> query = em.createNamedQuery("Product.findName", Product.class);
			query.setParameter("keyword", "%" + name + "%");
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
		return null;
	}
	
	// Hàm tìm sản phẩm theo trạng thái (active hoặc không)
	public List<Product> findByActive(boolean key) {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			TypedQuery<Product> query = em.createNamedQuery("Product.findByActive", Product.class);
			query.setParameter("key", key);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		List<Product> products = new ProductDAO().findByActive(true);
		for (Product p : products) {
			System.out.println(p);
		}
	}
}
