package com.cjh.fragment;

import com.cjh.activity.SettingActivity;
import com.cjh.adapter.OrderFragmentAdapter;
import com.cjh.cjh_sell.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class OrderFragment extends Fragment implements OnClickListener{
	private ViewPager mViewPager;
	private OrderFragmentAdapter mFragmentAdapter;
	
	private RelativeLayout order_top_rl;
	private RelativeLayout order_left_rl;
	private RelativeLayout order_right_rl;

	private View order_left_line;
	private View order_right_line;
	
	//private ImageButton settingBtn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.fragment_order, container,
				false);
		order_top_rl=(RelativeLayout) contentView.findViewById(R.id.order_top_rl);
		order_top_rl.setOnClickListener(this);
		order_left_rl = (RelativeLayout) contentView.findViewById(R.id.order_left_rl);
		order_left_rl.setOnClickListener(this);
		order_right_rl = (RelativeLayout) contentView.findViewById(R.id.order_right_rl);
		order_right_rl.setOnClickListener(this);
		order_left_line = contentView.findViewById(R.id.order_left_line);
		order_right_line = contentView.findViewById(R.id.order_right_line);
		mViewPager = (ViewPager) contentView.findViewById(R.id.order_viewpager);
		mFragmentAdapter = new OrderFragmentAdapter(
				getActivity().getSupportFragmentManager(), getActivity());
		mViewPager.setAdapter(mFragmentAdapter);
		
		initView();
		initData();
		return contentView;
	}
	private void initView() {
		
	}
	private void initData() {
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
	public void onClick(View v) {
		
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
		case R.id.order_top_rl:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
		}
	}
}
