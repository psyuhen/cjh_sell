package com.cjh.activity;

import com.cjh.cjh_sell.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShopDetailActivity extends BaseTwoActivity {
	private Button shop_add_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopdetails);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		shop_add_btn = (Button) findViewById(R.id.shop_add_btn);
		shop_add_btn.setOnClickListener(this);
	}

	private void initData() {
		title.setText("店铺编辑");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setVisibility(View.GONE);
		// right_text.setText("发布");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.shop_add_btn:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("是否完成编辑?")
					.setCancelable(false)
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									startActivity(new Intent(ShopDetailActivity.this, MainActivity.class));
								}
								
							})
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).create().show();
			break;

		default:
			break;
		}
	}
}
