package com.java.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
	@NamedQuery(name="User.findActive", query="SELECT u FROM User u WHERE u.active = :key")
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_users")
	private int idUsers;

	private String email;

	private String fullname;

	private String gender;  

	private String password;
	
	private boolean active = true;
	
	@Column(name="user_key")
	private String key;

	//bi-directional many-to-one association to CartItem
	@OneToMany(mappedBy="user")
	private List<CartItem> cartItems;

	//bi-directional many-to-one association to Order
	@OneToMany(mappedBy="user")
	private List<Order> orders;

	//bi-directional many-to-one association to Review
	@OneToMany(mappedBy="user")
	private List<Review> reviews;

	public User() {
	}

	public int getIdUsers() {
		return this.idUsers;
	}

	public void setIdUsers(int idUsers) {
		this.idUsers = idUsers;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getGender() { 
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<CartItem> getCartItems() {
		return this.cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public CartItem addCartItem(CartItem cartItem) {
		getCartItems().add(cartItem);
		cartItem.setUser(this);
		return cartItem;
	}

	public CartItem removeCartItem(CartItem cartItem) {
		getCartItems().remove(cartItem);
		cartItem.setUser(null);
		return cartItem;
	}

	public List<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Order addOrder(Order order) {
		getOrders().add(order);
		order.setUser(this);
		return order;
	}

	public Order removeOrder(Order order) {
		getOrders().remove(order);
		order.setUser(null);
		return order;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setUser(this);
		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setUser(null);
		return review;
	}
	
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "User [idUsers=" + idUsers + ", email=" + email + ", fullname=" + fullname + ", gender=" + gender
				+ ", password=" + password + ", active=" + active + "]";
	}
}
