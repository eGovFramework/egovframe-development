package org.egovframe.rte.sample.model;

/**
 * @version 1.0
 * @created 05-8-2009 오후 3:07:54
 */
public class Product {

	private String asYn;
	private Category category;
	private String manufactureDay;
	private String prodDetail;
	private String productName;
	private String productNo;
	private Timestamp regDate;
	private int sellAmount;
	private String sellerId;
	private int sellQuantity;
	
	public String getAsYn() {
		return asYn;
	}

	public void setAsYn(String asYn) {
		this.asYn = asYn;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getManufactureDay() {
		return manufactureDay;
	}

	public void setManufactureDay(String manufactureDay) {
		this.manufactureDay = manufactureDay;
	}

	public String getProdDetail() {
		return prodDetail;
	}

	public void setProdDetail(String prodDetail) {
		this.prodDetail = prodDetail;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public int getSellAmount() {
		return sellAmount;
	}

	public void setSellAmount(int sellAmount) {
		this.sellAmount = sellAmount;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public int getSellQuantity() {
		return sellQuantity;
	}

	public void setSellQuantity(int sellQuantity) {
		this.sellQuantity = sellQuantity;
	}	

}