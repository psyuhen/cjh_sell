package com.cjh.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cjh.adapter.CommonAdapter;
import com.cjh.adapter.ViewHolder;
import com.cjh.bean.SecretDiscountItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;

public class SecretDiscountActivity extends BaseTwoActivity {
	private ListView mListView;
	private List<SecretDiscountItem> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secret_discount);
		initView();
		initData();
		mListView.setAdapter(showAdapter());
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		title.setText("私密优惠");
		list = new ArrayList<SecretDiscountItem>();
		mListView=(ListView) findViewById(R.id.mListView);
		right_imgbtn.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 2; i++) {
			SecretDiscountItem secretDiscountItem = new SecretDiscountItem();
			secretDiscountItem.setDiscount(9);
			secretDiscountItem.setStartdate(new Date());
			secretDiscountItem.setEnddate(new Date());
			list.add(secretDiscountItem);
		}
	}

	public CommonAdapter<SecretDiscountItem> showAdapter() {

		return new CommonAdapter<SecretDiscountItem>(
				SecretDiscountActivity.this, list,
				R.layout.item_secret_discount) {
			@Override
			public void convert(ViewHolder helper, SecretDiscountItem item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.item_secret_discounts_discount,
						 item.getDiscount()+"折");
//				helper.setText(R.id.item_secret_discounts_startdate,
//						"开始    "+CommonsUtil.getDateStr(item.getStartdate()));
//				helper.setText(R.id.item_secret_discounts_enddate,
//						"结束    "+CommonsUtil.getDateStr(item.getEnddate()));

			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_imgbtn:
			startActivity(new Intent(SecretDiscountActivity.this,
					SecretDiscountAddActivity.class));
			SecretDiscountActivity.this.finish();
			break;
		default:
			break;
		}
	}
}
