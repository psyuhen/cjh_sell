package com.cjh.activity;

import com.cjh.adapter.OrderFragmentAdapter;
import com.cjh.cjh_sell.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
/**
 * 订单管理
 * @author ps
 *
 */
public class OrderActivity extends BaseActivity implements OnClickListener {
	private ViewPager mViewPager;
	private OrderFragmentAdapter mFragmentAdapter;
	private RelativeLayout order_left_rl;
	private RelativeLayout order_right_rl;
	private View order_left_line;
	private View order_right_line;
	private ImageButton edit_imaggbtn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		initView();
		initData();
	}

	private void initData() {
		title.setText("订单管理");
		right_imgbtn.setVisibility(View.GONE);
		mFragmentAdapter = new OrderFragmentAdapter(
				getSupportFragmentManager(), this);
		mViewPager.setAdapter(mFragmentAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					order_left_line.setVisibility(View.VISIBLE);
					order_right_line.setVisibility(View.INVISIBLE);
					break;
				case 1:
					order_left_line.setVisibility(View.INVISIBLE);
					order_right_line.setVisibility(View.VISIBLE);
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

	@Override
	public void initView() {
		super.initView();
		mViewPager = (ViewPager) findViewById(R.id.order_viewpager);
		order_left_rl = (RelativeLayout) findViewById(R.id.order_left_rl);
		order_left_rl.setOnClickListener(this);
		order_right_rl = (RelativeLayout) findViewById(R.id.order_right_rl);
		order_right_rl.setOnClickListener(this);
		order_left_line = findViewById(R.id.order_left_line);
		order_right_line = findViewById(R.id.order_right_line);
		edit_imaggbtn = (ImageButton) findViewById(R.id.right_imgbtn);
		edit_imaggbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.order_left_rl:
			order_left_line.setVisibility(View.VISIBLE);
			order_right_line.setVisibility(View.INVISIBLE);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.order_right_rl:
			order_left_line.setVisibility(View.INVISIBLE);
			order_right_line.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(1);
			break;
		}
	}
	
	protected void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commitAllowingStateLoss();
    }
}
