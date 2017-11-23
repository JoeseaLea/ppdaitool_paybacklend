package com.ppdai.ppdaitool.vo;

/**
 * 投标还款中、已还清的VO
 */
public class PaybacklendVo {
	private int id; //主键
	private String tenderFinishDate; //投标完成时间
	private double paidAmount; //已还清金额
	private String code; //列表编号
	private String borrower; //借款人
	private double tenderAmount; //实际投标金额
	private String dataType; //数据类型：收款中，已还清
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTenderFinishDate() {
		return tenderFinishDate;
	}
	public void setTenderFinishDate(String tenderFinishDate) {
		this.tenderFinishDate = tenderFinishDate;
	}
	public double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public double getTenderAmount() {
		return tenderAmount;
	}
	public void setTenderAmount(double tenderAmount) {
		this.tenderAmount = tenderAmount;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
