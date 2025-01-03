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
    
    // Phương thức lấy đơn hàng theo ID
    public Order getOrderById(int orderId) {
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();

            // Truy vấn lấy đơn hàng theo ID
            String jpql = "SELECT o FROM Order o WHERE o.idOrders = :orderId";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("orderId", orderId);

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
        return null;  // Trả về null nếu không tìm thấy đơn hàng 
    }

    //  Phương thức kiểm tra nội dung thay đổi
  public boolean updateOrderDetails(int orderId, String address, String phone) {
      EntityManager em = null;
      try {
          em = JpaUtils.getEntityManager();
          em.getTransaction().begin();

          // Tìm kiếm đơn hàng theo ID
          Order order = em.find(Order.class, orderId);
          if (order != null) {
              // Cập nhật thông tin đơn hàng
              if (address != null && !address.trim().isEmpty()) {
                  order.setAddress(address);
              }
              if (phone != null && !phone.trim().isEmpty()) {
                  order.setPhone(phone);
              }
              em.merge(order); 
              em.getTransaction().commit();
              return true; // Cập nhật thành công
          } else {
              em.getTransaction().rollback();
              return false; 
          }
      } catch (Exception e) {
          if (em != null && em.getTransaction().isActive()) {
              em.getTransaction().rollback(); 
          }
          e.printStackTrace();
          return false; 
      } finally {
          if (em != null) {
              em.close(); 
          }
      }
  }	


    // Phương thức insertByOrder đã được cập nhật để lưu mã hash
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
    
    // Phương thức lấy đơn hàng mới nhất
    public Order getLatestOrder() {
        Order latestOrder = null;
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();

            // Truy vấn lấy đơn hàng mới nhất dựa trên thuộc tính created
            String jpql = "SELECT o FROM Order o ORDER BY o.created DESC";

            // Giới hạn kết quả trả về 1 đơn hàng
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setMaxResults(1);  // Chỉ lấy 1 đơn hàng mới nhất

            // Kiểm tra kết quả trả về
            List<Order> result = query.getResultList();
            if (!result.isEmpty()) {
                latestOrder = result.get(0);
            } else {
                System.out.println("Không có đơn hàng nào được trả về.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return latestOrder;
    }
    
    public List<Order> getNewOrder() {
        List<Order> orders = new ArrayList<>();
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();
            
            // Sửa lại truy vấn JPQL
            String jpql = "SELECT o FROM Order o WHERE o.status = :status";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("status", "Đã duyệt"); 

            orders = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return orders;
    }

    // Phương thức lấy đơn hàng theo ID và mã hash
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

    // Phương thức lấy đơn hàng theo người dùng
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
    
 // Phương thức lấy tổng số đơn hàng của người dùng
    public int getTotalOrdersByUser(int userId) {
        int total = 0;
        try {
            // Tạo EntityManager
            EntityManager em = JpaUtils.getEntityManager();
            
            // Tạo câu truy vấn JPQL để đếm số lượng đơn hàng
            String jpql = "SELECT COUNT(o) FROM Order o WHERE o.user.idUsers = :userId AND o.active = true";
            
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("userId", userId);
            
            total = query.getSingleResult().intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    
 // Phương thức lấy đơn hàng phân trang cho người dùng
    public List<Order> getOrdersByPage(int userId, int pageNumber, int pageSize) {
        List<Order> orders = new ArrayList<>();
        try {
            // Tạo EntityManager
            EntityManager em = JpaUtils.getEntityManager();
            
            // Tạo câu truy vấn JPQL với phân trang
            String jpql = "SELECT o FROM Order o WHERE o.user.idUsers = :userId AND o.active = true";
            
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("userId", userId);
            
            // Đặt số bản ghi và phân trang
            query.setFirstResult((pageNumber - 1) * pageSize);  // Tính offset
            query.setMaxResults(pageSize);  // Giới hạn số bản ghi trả về
            
            orders = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Phương thức lấy đơn hàng theo trạng thái
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

    // Phương thức lấy các đơn hàng đang hoạt động
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

    // Phương thức xác nhận đơn hàng
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
            // Lấy thời gian hiện tại
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.SECOND, -seconds);
            Date recentTime = calendar.getTime();

            // Truy vấn đơn hàng được tạo gần đây
            String jpql = "SELECT o FROM Order o WHERE o.created >= :recentTime";
            return em.createQuery(jpql, Order.class)
                     .setParameter("recentTime", recentTime)
                     .getResultList();
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
