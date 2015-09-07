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

public class CouponAddActivity extends BaseTwoActivity {
	private EditText coupons_startdate_edit;
	private EditText coupons_enddate_edit;
	private Button add_coupons_btn;
	// 预设时间对话框
	// 时间控件常量

	private String initStartDateTime = ""; // 初始化开始时间
//	private String initStartDateTime = CommonsUtil.getChooseDateStr(new Date()); // 初始化开始时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_coupons);
		initView();
		initData();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		coupons_startdate_edit = (EditText) findViewById(R.id.coupons_startdate_edit);
		coupons_enddate_edit = (EditText) findViewById(R.id.coupons_enddate_edit);
		add_coupons_btn = (Button) findViewById(R.id.add_coupons_btn);
		add_coupons_btn.setOnClickListener(this);
		coupons_startdate_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						CouponAddActivity.this, initStartDateTime);
				dateTimePicKDialog.dateTimePicKDialog(coupons_startdate_edit);
			}
		});
		coupons_enddate_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						CouponAddActivity.this, initStartDateTime);
				dateTimePicKDialog.dateTimePicKDialog(coupons_enddate_edit);
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		title.setText("添加优惠券");
		right_imgbtn.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.add_coupons_btn:
			startActivity(new Intent(CouponAddActivity.this,
					CouponsActivity.class));
			CouponAddActivity.this.finish();
			break;
		default:
			break;
		}
	}

}
