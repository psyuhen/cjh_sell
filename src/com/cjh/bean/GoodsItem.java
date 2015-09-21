package com.cjh.bean;

import java.util.List;

import android.graphics.Bitmap;

public class GoodsItem {
	private int id;
	private String title;
	private int stock;
	private int sellmount;
	private float price;
	private String standard;
	private String img;
	private String tag;
	private Bitmap bitmap;
	private String create_time;
	
	private List<MerchDisacount> merchDisacounts;
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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getSellmount() {
		return sellmount;
	}

	public void setSellmount(int sellmount) {
		this.sellmount = sellmount;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	
	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public List<MerchDisacount> getMerchDisacounts() {
		return merchDisacounts;
	}

	public void setMerchDisacounts(List<MerchDisacount> merchDisacounts) {
		this.merchDisacounts = merchDisacounts;
	}
	
}
