package com.cjh.activity;

import com.cjh.cjh_sell.R;

import android.os.Bundle;
import android.view.View;

public class AdAddActivity extends BaseTwoActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ad);
		initView();
		initData();
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
	}
	private void initData() {
		// TODO Auto-generated method stub
		title.setText("添加广告");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setText("添加");
	}
}
