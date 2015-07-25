package com.cjh.activity;

import com.cjh.cjh_sell.R;

import android.os.Bundle;
import android.view.View;

/**
 * 关于我们
 * @author zkq
 *
 */
public class AboutUsActivity extends BaseTwoActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
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
		right_imgbtn.setVisibility(View.GONE);
		title.setText("关于我们");
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
