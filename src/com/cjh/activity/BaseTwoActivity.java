package com.cjh.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cjh.auth.SessionManager;
import com.cjh.cjh_sell.R;

public class BaseTwoActivity extends FragmentActivity implements OnClickListener{
	public TextView title;
	public TextView right_text;
	public TextView back;
	public ImageButton right_imgbtn;
	
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
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			
			break;
		case R.id.back:
			onBackPressed();
			break;
		}
	}
}
