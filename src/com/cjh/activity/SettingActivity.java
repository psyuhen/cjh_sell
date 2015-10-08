package com.cjh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjh.cjh_sell.R;
/**
 * 个人信息
 * @author pansen
 *
 */
public class SettingActivity extends BaseTwoActivity implements OnClickListener {

	private RelativeLayout setting_item_about_rl;
	private TextView setting_name_text_details;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		right_text.setVisibility(View.VISIBLE);
		right_text.setText("完成");
		right_imgbtn.setVisibility(View.GONE);
		title.setText("个人信息");
		
		setting_item_about_rl=(RelativeLayout) findViewById(R.id.setting_item_about_rl);
		setting_item_about_rl.setOnClickListener(this);
		setting_name_text_details = (TextView)findViewById(R.id.setting_name_text_details);
		setting_name_text_details.setText(sessionManager.getUserName());
	}

	private void initData() {
		
	}
	
	@Override
	public void onClick(View v) {
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
