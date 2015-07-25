package com.cjh.bean;

import java.util.Date;

public class OrderSourceItem {
	private int id;
	private Date date;
	private int pocketcount;
	private int othercount;
	private int allcount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getPocketcount() {
		return pocketcount;
	}
	public void setPocketcount(int pocketcount) {
		this.pocketcount = pocketcount;
	}
	public int getOthercount() {
		return othercount;
	}
	public void setOthercount(int othercount) {
		this.othercount = othercount;
	}
	public int getAllcount() {
		return allcount;
	}
	public void setAllcount(int allcount) {
		this.allcount = allcount;
	}
	
}
