package com.ppdai.ppdaitool.vo;

public class NeedReturnRecordNext6MonthVo {
	/**
	 * 主键
	 */
	private int id;
	/**
	 * 完整用户名
	 */
	private String fullUsername;
	/**
	 * 手机app用户的闪电借款
	 */
	private String listingid;
	/**
	 * 时间
	 */
	private String dateTime;
	/**
	 * 金额
	 */
	private double amount;
	private String dataType; //数据类型：黑名单，收款中，已还清
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFullUsername() {
		return fullUsername;
	}
	public void setFullUsername(String fullUsername) {
		this.fullUsername = fullUsername;
	}
	public String getListingid() {
		return listingid;
	}
	public void setListingid(String listingid) {
		this.listingid = listingid;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
