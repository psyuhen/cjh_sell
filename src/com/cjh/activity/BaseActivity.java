package com.cjh.activity;

import com.cjh.auth.SessionManager;
import com.cjh.cjh_sell.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity implements OnClickListener {
	public TextView title;
	public TextView right_text;
	public TextView back;
	public ImageButton right_imgbtn;
	private RelativeLayout order_top_rl;
	public SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sessionManager = new SessionManager(getApplicationContext());
	}

	public void initView() {
		title = (TextView) findViewById(R.id.title_text);
		right_text = (TextView) findViewById(R.id.right_text);
		back = (TextView) findViewById(R.id.back);
		right_text.setVisibility(View.VISIBLE);
		right_text.setClickable(true);
		right_text.setOnClickListener(this);
		back.setOnClickListener(this);
		right_imgbtn=(ImageButton) findViewById(R.id.right_imgbtn);
		right_imgbtn.setOnClickListener(this);
		order_top_rl=(RelativeLayout) findViewById(R.id.order_top_rl);
		order_top_rl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			
			break;
		case R.id.back:
			onBackPressed();
			break;
		case R.id.order_top_rl:
			startActivity(new Intent(BaseActivity.this, SettingActivity.class));
			break;
		}
	}

}
