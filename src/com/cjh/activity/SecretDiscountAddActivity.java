package com.cjh.activity;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.DateTimePickDialogUtil;

public class SecretDiscountAddActivity extends BaseTwoActivity {
	private EditText secret_discount_starttime_edit;
	private EditText secret_discount_enddate_edit;
	private Button add_secret_discount_btn;

	private String initStartDateTime = ""; // 初始化开始时间
//	private String initStartDateTime = CommonsUtil.getChooseDateStr(new Date()); // 初始化开始时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secret_discount_add);
		initView();
		initData();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		secret_discount_starttime_edit = (EditText) findViewById(R.id.secret_discount_starttime_edit);
		secret_discount_enddate_edit = (EditText) findViewById(R.id.secret_discount_enddate_edit);
		add_secret_discount_btn = (Button) findViewById(R.id.add_secret_discount_btn);
		add_secret_discount_btn.setOnClickListener(this);
		secret_discount_starttime_edit
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
								SecretDiscountAddActivity.this,
								initStartDateTime);
						dateTimePicKDialog
								.dateTimePicKDialog(secret_discount_starttime_edit);
					}
				});
		secret_discount_enddate_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						SecretDiscountAddActivity.this, initStartDateTime);
				dateTimePicKDialog
						.dateTimePicKDialog(secret_discount_enddate_edit);
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		title.setText("添加私密优惠");
		right_imgbtn.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.add_secret_discount_btn:
			startActivity(new Intent(SecretDiscountAddActivity.this,
					SecretDiscountActivity.class));
			SecretDiscountAddActivity.this.finish();
			break;
		default:
			break;
		}
	}
}
