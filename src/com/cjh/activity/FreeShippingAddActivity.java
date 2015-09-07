package com.cjh.activity;

import android.os.Bundle;
import android.view.View;

import com.cjh.cjh_sell.R;

public class FreeShippingAddActivity extends BaseTwoActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_free_ship_add);
		initView();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		title.setText("满包邮");
		right_imgbtn.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		default:
			break;
		}
	}
}
