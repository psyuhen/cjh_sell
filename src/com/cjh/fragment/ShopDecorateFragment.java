package com.cjh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjh.cjh_sell.R;

public class ShopDecorateFragment extends Fragment {

	public static ShopDecorateFragment newInstance() {
		ShopDecorateFragment fragment = new ShopDecorateFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_decorate_my_shop,
				container, false);
		return contentView;
	}

}
