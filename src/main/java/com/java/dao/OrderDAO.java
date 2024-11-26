package com.java.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.java.model.Order;
import com.java.model.OrderItem;
import com.java.utils.JpaUtils;

public class OrderDAO extends EntityDAO<Order> {

	public OrderDAO() {
		super(Order.class);
		// TODO Auto-generated constructor stub
	}
	
	public void insertByOrder(Order order) {
	    EntityManager em = null;
	    try {
	        em = JpaUtils.getEntityManager();
	        em.getTransaction().begin();
	        
	        // Persist the orderItem
	        em.persist(order);
	        
	        em.getTransaction().commit();
	    } catch (Exception e) {
	        if (em != null) {
	            em.getTransaction().rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        if (em != null) {
	            em.close();
	        }
	    }
	}
	
	public List<Order> getByUser(int id_user){
		try {
			EntityManager em = JpaUtils.getEntityManager();
			
			String jpql = "SELECT o FROM Order o WHERE o.user.idUsers = :id AND o.active = true";
			
			TypedQuery<Order> query = em.createQuery(jpql, Order.class);
			
			query.setParameter("id", id_user);
			
			return query.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public int getLastId() {
		try {
			EntityManager em = JpaUtils.getEntityManager();
			
			String sql = "SELECT * FROM shopping.orders order by id_orders desc limit 1";
			
			Query query = em.createNativeQuery(sql, Order.class);
			
			List<Order> listOrders = query.getResultList();
			
			if(listOrders.size() == 0) {
				return 1;
			}else {
				return listOrders.get(0).getIdOrders();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public List<Order> getByStatus(List<String> statuses){
		try {
			EntityManager em = JpaUtils.getEntityManager();
			
			TypedQuery<Order> query = em.createNamedQuery("Order.findByStatus", Order.class);
			
			query.setParameter("statuses", statuses);
			
			return query.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Order> getActive(){
		try {
			EntityManager em = JpaUtils.getEntityManager();
			
			TypedQuery<Order> query = em.createNamedQuery("Order.findActive", Order.class);
			
			return query.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateOrderStatus(int orderId, String newStatus) {
	    EntityManager em = null;
	    try {
	        em = JpaUtils.getEntityManager();
	        em.getTransaction().begin();

	        // Tìm kiếm đơn hàng theo ID
	        Order order = em.find(Order.class, orderId);
	        if (order != null) {
	            // Cập nhật trạng thái đơn hàng
	            order.setStatus(newStatus);
	            em.merge(order);  // Cập nhật lại đối tượng order trong cơ sở dữ liệu
	        }

	        em.getTransaction().commit();
	    } catch (Exception e) {
	        if (em != null) {
	            em.getTransaction().rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        if (em != null) {
	            em.close();
	        }
	    }
	}

	
	public static void main(String[] args) {
		
		List<Order> listOrders = new OrderDAO().getActive();
		
		for(Order order: listOrders) {
			System.out.println(order.toString());
		}
	}
}
