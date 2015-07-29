package com.cjh.fragment;

import com.cjh.activity.SettingActivity;
import com.cjh.adapter.ShopFragmentAdapter;
import com.cjh.cjh_sell.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class ShopFragment extends Fragment implements OnClickListener {
	private ViewPager mViewPager;
	private ShopFragmentAdapter mFragmentAdapter;
	private RelativeLayout order_top_rl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.fragment_shop, container,
				false);
		mViewPager = (ViewPager) contentView.findViewById(R.id.shop_viewpager);
		mFragmentAdapter = new ShopFragmentAdapter(getActivity()
				.getSupportFragmentManager(), getActivity());
		mViewPager.setAdapter(mFragmentAdapter);
		order_top_rl = (RelativeLayout) contentView
				.findViewById(R.id.order_top_rl);
		order_top_rl.setOnClickListener(this);
		return contentView;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.order_top_rl:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;

		}
	}

}
