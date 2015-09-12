package com.cjh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.cjh.adapter.OrderFragmentAdapter;
import com.cjh.cjh_sell.R;

/**
 * 我的订单
 * @author pansen
 *
 */
public class OrdersActivity extends BaseTwoActivity {
	private RadioGroup radioGroup;
	private ViewPager mViewPager;
	private OrderFragmentAdapter mFragmentAdapter;
	private EditText order_search_edit;

	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		setContentView(R.layout.activity_orders);
		initView();
		initData();
		radioGroup = (RadioGroup) findViewById(R.id.order_radiogroup);
		mViewPager = (ViewPager) findViewById(R.id.order_viewpager);
		mFragmentAdapter = new OrderFragmentAdapter(
				OrdersActivity.this.getSupportFragmentManager(),
				OrdersActivity.this);
		mViewPager.setAdapter(mFragmentAdapter);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				switch (checkedId) {
				case R.id.order_running_rbtn:
					mViewPager.setCurrentItem(0);
					break;
				case R.id.order_complete_rbtn:
					mViewPager.setCurrentItem(1);
					break;
				case R.id.order_close_rbtn:
					mViewPager.setCurrentItem(2);
					break;
				}
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				
				switch (position) {
				case 0:
					radioGroup.check(R.id.order_running_rbtn);
					break;
				case 1:
					radioGroup.check(R.id.order_complete_rbtn);
					break;
				case 2:
					radioGroup.check(R.id.order_close_rbtn);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				

			}
		});
		order_search_edit = (EditText) findViewById(R.id.order_search_edit);
		order_search_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(OrdersActivity.this,
						OrderSearchActivity.class));
			}
		});
	}

	@Override
	public void initView() {
		
		super.initView();
		title.setText("订单详情");
		right_imgbtn.setVisibility(View.GONE);
	}

	private void initData() {
		

	}

	@Override
	public void onClick(View v) {
		
		super.onClick(v);
	}

}
