package com.cjh.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kymjs.aframe.ui.widget.KJListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cjh.adapter.CommonAdapter;
import com.cjh.adapter.ViewHolder;
import com.cjh.bean.CouponsItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;

public class CouponsActivity extends BaseTwoActivity {
	private List<CouponsItem> couponsList;
	private KJListView coupons_listview;
	private Button add_coupons_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupons);
		initView();
		initData();
		coupons_listview.setAdapter(showAdapter());
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		coupons_listview = (KJListView) findViewById(R.id.coupons_listview);
		add_coupons_btn = (Button) findViewById(R.id.add_coupons_btn);
		add_coupons_btn.setOnClickListener(this);
		couponsList = new ArrayList<CouponsItem>();
	}

	private void initData() {
		// TODO Auto-generated method stub
		title.setText("店铺优惠券");
		right_imgbtn.setVisibility(View.GONE);
		for (int i = 0; i < 20; i++) {
			CouponsItem couponsItem = new CouponsItem();
			couponsItem.setStartDate(new Date());
			couponsItem.setEndDate(new Date());
			if (i % 2 == 0) {
				couponsItem.setMoney(30.0);
				couponsItem.setEnoughmoney(99);
				couponsItem.setSurplusnum(100);
				couponsItem.setStatus('1');
			} else if (i % 3 == 1) {
				couponsItem.setMoney(50.0);
				couponsItem.setEnoughmoney(19);
				couponsItem.setSurplusnum(500);
				couponsItem.setStatus('2');
			} else {
				couponsItem.setMoney(10.0);
				couponsItem.setEnoughmoney(19);
				couponsItem.setSurplusnum(100);
				couponsItem.setStatus('3');
			}

			couponsList.add(couponsItem);
		}

	}

	public CommonAdapter<CouponsItem> showAdapter() {

		return new CommonAdapter<CouponsItem>(CouponsActivity.this,
				couponsList, R.layout.item_coupons) {
			@Override
			public void convert(ViewHolder helper, CouponsItem item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.item_coupons_money, "￥" + item.getMoney());
				helper.setText(R.id.item_coupons_eenoughmoney,
						"满" + item.getEnoughmoney() + "元可用");
//				helper.setText(R.id.item_coupons_startdate,
//						CommonsUtil.getDateStr(item.getStartDate()));
//				helper.setText(R.id.item_coupons_enddate,
//						CommonsUtil.getDateStr(item.getEndDate()));
				helper.setText(R.id.item_coupons_surplusnum,
						"剩余" + item.getSurplusnum() + "张");
				helper.setText(R.id.item_coupons_status,
						CommonsUtil.getCouponsStatus(item.getStatus()));

			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.add_coupons_btn:
			startActivity(new Intent(CouponsActivity.this,
					CouponAddActivity.class));
			break;

		default:
			break;
		}
	}

}
