package com.java.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import com.java.model.Order;
import com.java.utils.JpaUtils;
import java.util.UUID;

public class OrderDAO extends EntityDAO<Order> {

    public OrderDAO() {
        super(Order.class);
    }

    public void insertByOrder(Order order) {
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();
            em.getTransaction().begin();

            // Tạo mã hash và gán cho đơn hàng
            String hashCode = UUID.randomUUID().toString();  // Ví dụ sử dụng UUID làm mã hash
            order.setHashCode(hashCode);  // Set hash code vào đối tượng order

            // Persist the order
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

    public Order getOrderByIdAndHashCode(int orderId, String hashCode) {
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();

            // Truy vấn lấy đơn hàng theo ID
            String jpql = "SELECT o FROM Order o WHERE o.idOrders = :orderId AND o.hashCode = :hashCode";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("orderId", orderId);
            query.setParameter("hashCode", hashCode);

            // Trả về đơn hàng nếu có
            List<Order> result = query.getResultList();
            if (!result.isEmpty()) {
                return result.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;  // Trả về null nếu không tìm thấy đơn hàng hoặc mã hash không khớp
    }

    public List<Order> getByUser(int id_user) {
        try {
            EntityManager em = JpaUtils.getEntityManager();

            String jpql = "SELECT o FROM Order o WHERE o.user.idUsers = :id AND o.active = true";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("id", id_user);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getByStatus(List<String> statuses) {
        try {
            EntityManager em = JpaUtils.getEntityManager();

            TypedQuery<Order> query = em.createNamedQuery("Order.findByStatus", Order.class);

            query.setParameter("statuses", statuses);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getActive() {
        try {
            EntityManager em = JpaUtils.getEntityManager();

            TypedQuery<Order> query = em.createNamedQuery("Order.findActive", Order.class);

            return query.getResultList();
        } catch (Exception e) {
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

    public void updateOrderHash(Order order) {
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();
            em.getTransaction().begin();

            // Tìm kiếm đơn hàng theo ID
            Order existingOrder = em.find(Order.class, order.getIdOrders());
            if (existingOrder != null) {
                // Cập nhật mã hash
                existingOrder.setHashCode(order.getHashCode());
                em.merge(existingOrder);
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

    public List<Order> getRecentOrders(int seconds) {
        EntityManager em = JpaUtils.getEntityManager();
        try {
            // Trả về danh sách tất cả các đơn hàng
            String jpql = "SELECT o FROM Order o";
            List<Order> result = em.createQuery(jpql, Order.class).getResultList();
            return result.isEmpty() ? new ArrayList<>() : result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    // Main method for testing
    public static void main(String[] args) {
        List<Order> listOrders = new OrderDAO().getActive();

        for (Order order : listOrders) {
            System.out.println(order.toString());
        }
    }
}

