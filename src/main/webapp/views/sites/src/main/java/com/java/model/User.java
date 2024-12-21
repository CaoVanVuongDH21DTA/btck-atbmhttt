package com.java.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="users")
@NamedQueries({
    @NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
    @NamedQuery(name="User.findActive", query="SELECT u FROM User u WHERE u.active = :key")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // Auto-generated ID

    private String name;

    private String email;

    private String password;

    private String city;

    private String role;

    @Column(name="user_key")
    private String userKey; // Changed to 'user_key' to match the database column name

    private boolean active = true; // Assuming you still want an 'active' flag field.

    public User() {
    }

    // Getters and setters for the fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", city=" + city 
            + ", role=" + role + ", userKey=" + userKey + ", active=" + active + "]";
    }
}
