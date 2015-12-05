package com.cjh.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cjh.adapter.GoodsFragmentAdapter;
import com.cjh.auth.SessionManager;
import com.cjh.cjh_sell.R;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 新版的商品信息
 * @author ps
 *
 */
public class GoodActivity extends FragmentActivity implements OnClickListener {
	private Logger LOGGER = LoggerFactory.getLogger(GoodActivity.class);

	private RadioGroup mRadioGroup;
	private ViewPager mViewPager;
	private GoodsFragmentAdapter mAdapter;
	private TextView back;
	public SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good);
		sessionManager = new SessionManager(getApplicationContext());
		
		initView();
		mAdapter = new GoodsFragmentAdapter(getSupportFragmentManager(), GoodActivity.this);
		mViewPager.setAdapter(mAdapter);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.btn_selling://出售中
					mViewPager.setCurrentItem(0);
					break;
				case R.id.btn_offsheld://已下架
					mViewPager.setCurrentItem(1);
					break;
				case R.id.btn_category://分类
					mViewPager.setCurrentItem(2);
					break;
				}
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				switch(position) {
				case 0:
					mRadioGroup.check(R.id.btn_selling);
					break;
				case 1:
					mRadioGroup.check(R.id.btn_offsheld);
					break;
				case 2:
					mRadioGroup.check(R.id.btn_category);
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
	}

	private void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.goods_radioGroup);
		mViewPager = (ViewPager) findViewById(R.id.goods_viewpager);
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;

		default:
			break;
		}

	}
}
