package com.java.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "support_requests")
@NamedQueries({
    @NamedQuery(name = "Support.findAll", query = "SELECT s FROM Support s")
})
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_supports")
    private int id;

    private String email;

    private String subject;

    private String message;

    @Column(name = "request_time")
    private Date requestTime;  // Trường thời gian yêu cầu

    @Column(name = "error_type")
    private String errorType;  // Trường lỗi nếu cần

    @Column(name = "status")
    private String status;  // Lưu trạng thái dưới dạng chuỗi

    @Column(name = "assigned_to")
    private String assignedTo;  // Trường người quản lý

    // Các getter và setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}

