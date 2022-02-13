package com.infosys.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
//This class is use for the interaction of Object with the database.

@Entity
public class Seller {
	@Id
	@Column(name="seller_id")
	String sellerId;
	String name;
	String email;
	@Column(name="phone_no")
	String phoneNo;
	String password;
	@Column(name="is_active")
	Boolean isActive;
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
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
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
