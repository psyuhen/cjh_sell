package com.cjh.activity;

import com.cjh.cjh_sell.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SettingActivity extends BaseTwoActivity implements OnClickListener {

	private RelativeLayout setting_item_about_rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
		initData();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		setting_item_about_rl=(RelativeLayout) findViewById(R.id.setting_item_about_rl);
		setting_item_about_rl.setOnClickListener(this);
	}

	private void initData() {
		right_imgbtn.setVisibility(View.GONE);
		title.setText("设置");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.setting_item_about_rl:
			startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
			break;

		default:
			break;
		}
	}
}
