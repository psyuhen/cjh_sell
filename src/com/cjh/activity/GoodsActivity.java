package com.cjh.activity;

import com.cjh.adapter.GoodsFragmentAdapter;
import com.cjh.cjh_sell.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
/**
 * 商品管理
 * @author ps
 *
 */
public class GoodsActivity extends BaseActivity implements OnClickListener {
	private ViewPager mViewPager;
	private GoodsFragmentAdapter mFragmentAdapter;

	private RelativeLayout goods_left_rl;
	private RelativeLayout goods_right_rl;

	private View goods_left_line;
	private View goods_right_line;

	private ImageButton edit_imaggbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		sessionManager.checkLogin();
		
		initView();
		initData();
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
				switch (position) {
				case 0:
					goods_left_line.setVisibility(View.VISIBLE);
					goods_right_line.setVisibility(View.INVISIBLE);
					break;
				case 1:
					goods_left_line.setVisibility(View.INVISIBLE);
					goods_right_line.setVisibility(View.VISIBLE);
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

	private void initData() {
		title.setText("商品管理");
		mViewPager.setAdapter(mFragmentAdapter);
	}

	@Override
	public void initView() {
		super.initView();
		mViewPager = (ViewPager) findViewById(R.id.goods_viewpager);
		mFragmentAdapter = new GoodsFragmentAdapter(
				getSupportFragmentManager(), this);
		goods_left_rl = (RelativeLayout) findViewById(R.id.goods_left_rl);
		goods_left_rl.setOnClickListener(this);
		goods_right_rl = (RelativeLayout) findViewById(R.id.goods_right_rl);
		goods_right_rl.setOnClickListener(this);
		goods_left_line = findViewById(R.id.goods_left_line);
		goods_right_line = findViewById(R.id.goods_right_line);
		edit_imaggbtn = (ImageButton) findViewById(R.id.right_imgbtn);
		edit_imaggbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.goods_left_rl:
			goods_left_line.setVisibility(View.VISIBLE);
			goods_right_line.setVisibility(View.INVISIBLE);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.goods_right_rl:
			goods_left_line.setVisibility(View.INVISIBLE);
			goods_right_line.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(2);
			break;
		case R.id.right_imgbtn:
			startActivity(new Intent(GoodsActivity.this, GoodsAddActivity.class));
			finish();
			break;

		}
	}

}
