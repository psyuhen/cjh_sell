package com.cjh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cjh.cjh_sell.R;

public class FreeShippingActivity extends BaseTwoActivity{
	private Button add_freeship_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_free_ship);
		initView();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		title.setText("满包邮");
		right_imgbtn.setVisibility(View.GONE);
		add_freeship_btn=(Button) findViewById(R.id.add_freeship_btn);
		add_freeship_btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.add_freeship_btn:
			startActivity(new Intent(FreeShippingActivity.this, FreeShippingAddActivity.class));
			break;
		default:
			break;
		}
	}
}
