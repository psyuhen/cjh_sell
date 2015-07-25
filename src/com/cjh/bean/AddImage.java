package com.cjh.bean;

import android.graphics.Bitmap;

public class AddImage {

	private int id;
	private Bitmap bitmap;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public AddImage(int id, Bitmap bitmap) {
		super();
		this.id = id;
		this.bitmap = bitmap;
	}

	public AddImage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
