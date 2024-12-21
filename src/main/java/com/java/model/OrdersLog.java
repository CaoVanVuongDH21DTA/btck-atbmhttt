package com.java.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "orders_log")
public class OrdersLog implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")  
    private int logId;

    @Column(name = "id_orders")  
    private int idOrders;

    @Column(name = "user_id")  
    private int userId;

    @Column(name = "action")
    private String action;

    @Column(name = "changed_at")  
    private Timestamp changedAt;

    @Column(name = "details")
    private String details;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getIdOrders() {
        return idOrders;
    }

    public void setIdOrders(int idOrders) {
        this.idOrders = idOrders;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "OrdersLog [logId=" + logId + ", idOrders=" + idOrders + ", userId=" + userId + ", action=" + action
                + ", changedAt=" + changedAt + ", details=" + details + "]";
    }
}
