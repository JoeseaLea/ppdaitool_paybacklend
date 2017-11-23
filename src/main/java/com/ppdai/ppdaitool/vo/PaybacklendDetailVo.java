package com.ppdai.ppdaitool.vo;

/**
 * 投标还款中、已还清的DetailVO
 */
public class PaybacklendDetailVo {
	private int id; //主键
	private String code; //列表编号，与PaybacklendVo表的code字段值一致，用于做表关联
	private String paymentDate; //还款日
	private double yisbx; //已收本息
	private double yisbj; //已收本金
	private double yislx; //已收利息
	private double yingsbx; //应收本息
	private double overTimeInterest; //逾期利息
	private int overDays; //逾期天数
	private String status; //状态
	private String dataType; //数据类型：收款中，已还清
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public double getYisbx() {
		return yisbx;
	}
	public void setYisbx(double yisbx) {
		this.yisbx = yisbx;
	}
	public double getYisbj() {
		return yisbj;
	}
	public void setYisbj(double yisbj) {
		this.yisbj = yisbj;
	}
	public double getYislx() {
		return yislx;
	}
	public void setYislx(double yislx) {
		this.yislx = yislx;
	}
	public double getYingsbx() {
		return yingsbx;
	}
	public void setYingsbx(double yingsbx) {
		this.yingsbx = yingsbx;
	}
	public double getOverTimeInterest() {
		return overTimeInterest;
	}
	public void setOverTimeInterest(double overTimeInterest) {
		this.overTimeInterest = overTimeInterest;
	}
	public int getOverDays() {
		return overDays;
	}
	public void setOverDays(int overDays) {
		this.overDays = overDays;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
