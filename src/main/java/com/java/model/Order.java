package com.java.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="orders")
@NamedQueries({
    @NamedQuery(name="Order.findAll", query="SELECT o FROM Order o"),
    @NamedQuery(name="Order.findByStatus", query="SELECT o FROM Order o WHERE o.status IN (:statuses)"),
    @NamedQuery(name="Order.findActive", query="SELECT o FROM Order o WHERE o.active = true")
})
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_orders")
    private int idOrders;

    private String address;

    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    private String phone;

    private String status;

    private boolean active = true;  // Thuộc tính này phải có giá trị mặc định là true

    // Thuộc tính mới để lưu mã hash
    @Column(name="hash_code")
    private String hashCode;

    // Bi-directional many-to-one association to OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems;

    // Bi-directional many-to-one association to User
    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;

    public Order() {
    }

    public int getIdOrders() {
        return this.idOrders;
    }

    public void setIdOrders(int idOrders) {
        this.idOrders = idOrders;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderItem addOrderItem(OrderItem orderItem) {
        getOrderItems().add(orderItem);
        orderItem.setOrder(this);

        return orderItem;
    }

    public OrderItem removeOrderItem(OrderItem orderItem) {
        getOrderItems().remove(orderItem);
        orderItem.setOrder(null);

        return orderItem;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public String toString() {
        return "Order [idOrders=" + idOrders + ", address=" + address + ", amount=" + amount + ", created=" + created
                + ", phone=" + phone + ", status=" + status + ", hashCode=" + hashCode + "]";
    }
}
