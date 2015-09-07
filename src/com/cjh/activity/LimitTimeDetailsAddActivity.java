package com.cjh.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cjh.cjh_sell.R;

public class LimitTimeDetailsAddActivity extends BaseTwoActivity{
	private TextView limit_time_title;
	private TextView limit_time_price;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_limit_time_details);
		initView();
		initData();
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		right_imgbtn.setVisibility(View.GONE);
		right_text.setText("完成");
		title.setText("限时折扣");
		right_text.setOnClickListener(this);
		limit_time_title=(TextView) findViewById(R.id.limit_time_title);
		limit_time_price=(TextView) findViewById(R.id.limit_time_price);
	}
	private void initData() {
		// TODO Auto-generated method stub
		limit_time_title.setText("新鲜水果甘肃静宁红富士苹果水果10斤24个80mm红富士苹果特价包邮");
		limit_time_price.setText("￥ 12.0");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_text:
			
			break;

		default:
			break;
		}
	}
	
}
