package com.cjh.bean;

import java.util.Date;

public class CouponsItem {
	private int id; // 编号
	private String title; // 标题
	private Date startDate; // 开始日期
	private Date endDate; // 结束日期
	private double money; // 金额
	private double enoughmoney; // 需求金额
	private int surplusnum; // 剩余数量
	private char status;
	private int limitnum;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getEnoughmoney() {
		return enoughmoney;
	}

	public void setEnoughmoney(double enoughmoney) {
		this.enoughmoney = enoughmoney;
	}

	public int getSurplusnum() {
		return surplusnum;
	}

	public void setSurplusnum(int surplusnum) {
		this.surplusnum = surplusnum;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getLimitnum() {
		return limitnum;
	}

	public void setLimitnum(int limitnum) {
		this.limitnum = limitnum;
	}

}
