package com.java.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name="cart_items")
@NamedQuery(name="CartItem.findAll", query="SELECT c FROM CartItem c")
public class CartItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_cart_item")
	private int idCartItem;

	private int quantity;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonBackReference
	private User user;

	//bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="id_products")
	private Product product;

	public CartItem() {
	}
	
	

	public CartItem(int idCartItem, int quantity, User user, Product product) {
		super();
		this.idCartItem = idCartItem;
		this.quantity = quantity;
		this.user = user;
		this.product = product;
	}



	public int getIdCartItem() {
		return this.idCartItem;
	}

	public void setIdCartItem(int idCartItem) {
		this.idCartItem = idCartItem;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "CartItem [idCartItem=" + idCartItem + ", quantity=" + quantity + ", user=" + user + ", product="
				+ product + "]";
	}
}