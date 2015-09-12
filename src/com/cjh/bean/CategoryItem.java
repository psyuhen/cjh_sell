package com.cjh.bean;

import android.graphics.Bitmap;

public class CategoryItem {
	private int id;
	private String title;
	private int num;
	private int img;
	private String detail;
	private Bitmap bitmap;

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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
}
