package com.ppdai.ppdaitool.vo;

/**
 * 逾期收回VO
 */
public class LateBackVo {
	private int id; //主键
	private String paymentDate; //还款日期
	private String code; //列表编号
	private String borrower; //借款人
	private double capitalRecovery; //本金收回
	private double interestRecovery; //利息收回
	private int overDays; //逾期天数
	private double needReturnAmount; //剩余待还本息
	private int periods; //期数
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPaymentDate() {
		return paymentDate;
	}
	
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
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
	
	public double getCapitalRecovery() {
		return capitalRecovery;
	}
	
	public void setCapitalRecovery(double capitalRecovery) {
		this.capitalRecovery = capitalRecovery;
	}
	
	public double getInterestRecovery() {
		return interestRecovery;
	}
	
	public void setInterestRecovery(double interestRecovery) {
		this.interestRecovery = interestRecovery;
	}
	
	public int getOverDays() {
		return overDays;
	}
	
	public void setOverDays(int overDays) {
		this.overDays = overDays;
	}
	
	public double getNeedReturnAmount() {
		return needReturnAmount;
	}
	
	public void setNeedReturnAmount(double needReturnAmount) {
		this.needReturnAmount = needReturnAmount;
	}
	
	public int getPeriods() {
		return periods;
	}
	
	public void setPeriods(int periods) {
		this.periods = periods;
	}
	
}
