package com.cjh.fragment;


import com.cjh.cjh_sell.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ADCategoryFragment extends Fragment{
	public static ADCategoryFragment newInstance() {
		ADCategoryFragment fragment = new ADCategoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView=inflater.inflate(R.layout.view_goods_ad, container,false);
		return contentView;
	}
}
