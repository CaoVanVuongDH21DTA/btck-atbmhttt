package com.java.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
@JsonIgnoreProperties({"orderItems"})
@NamedQueries({
    @NamedQuery(name="Order.findAll", query="SELECT o FROM Order o"),
    @NamedQuery(name="Order.findByStatus", query="SELECT o FROM Order o WHERE o.status = :status"),
    @NamedQuery(name="Order.findActive", query="SELECT o FROM Order o WHERE o.active = true")
})
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_orders")
    @JsonProperty("idOrders")
    private int idOrders;

    private String address;

    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    private String phone;

    private String status;

    private boolean active = true;

    // Thuộc tính mới để lưu mã hash
    @Column(name="hash_code")
    private String hashCode;

    // Bi-directional many-to-one association to OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    @JsonManagedReference
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

    // get and set
    public String getHashCode() {
        return hashCode;
    }

    public String setHashCode(String hashCode) {
        return this.hashCode = hashCode;
    }

    @Override
    public String toString() {
        return "Order [idOrders=" + idOrders + ", address=" + address + ", amount=" + amount + ", created=" + created
                + ", phone=" + phone + ", status=" + status + ", hashCode=" + hashCode + "]";
    }
}
