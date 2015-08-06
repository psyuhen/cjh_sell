package com.cjh.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjh.activity.MainActivity;
import com.cjh.activity.SettingActivity;
import com.cjh.adapter.ShopFragmentAdapter;
import com.cjh.cjh_sell.R;

public class ShopFragment extends Fragment implements OnClickListener {
	private ViewPager mViewPager;
	private ShopFragmentAdapter mFragmentAdapter;
	private RelativeLayout order_top_rl;
	
	private TextView order_title;//商家名称
	
	private Context context;
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.fragment_shop, container, false);
		mViewPager = (ViewPager) contentView.findViewById(R.id.shop_viewpager);
		mFragmentAdapter = new ShopFragmentAdapter(getActivity().getSupportFragmentManager(), getActivity());
		mViewPager.setAdapter(mFragmentAdapter);
		
		order_top_rl = (RelativeLayout) contentView.findViewById(R.id.order_top_rl);
		order_top_rl.setOnClickListener(this);
		order_title = (TextView)contentView.findViewById(R.id.order_title);
		
		initData();
		return contentView;
	}
	
	//显示商家名称，从MainActivity里面获取，不再查询数据库
	private void initData(){
		if(this.context != null){
			MainActivity activity = (MainActivity)this.context;
			String storeName = activity.sessionManager.get("store_name");
			order_title.setText(storeName);
		}
		
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
