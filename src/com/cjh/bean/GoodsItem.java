package com.cjh.bean;

public class GoodsItem {
	// ���
	private int id;
	// ����
	private String title;
	// ���
	private int stock;
	// ����
	private int sellmount;
	// ��Ǯ
	private float price;
	//���
	private String standard;
	// ͼƬ
	private String img;
	// �Ƽ�ͼƬ
	private String tag;

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

}
