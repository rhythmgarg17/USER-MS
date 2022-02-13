package com.infosys.user.dto;

import javax.validation.constraints.NotNull;
//Validations are used in DTO to validate the inputs and providing user friendly errors.
public class CartDTO {
	String buyerId;
	String productId;
	@NotNull(message = "{buyer.quantity.absent}")
	Integer quantity;
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
