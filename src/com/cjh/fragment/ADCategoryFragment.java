package com.cjh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjh.cjh_sell.R;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;


public class ADCategoryFragment extends Fragment{
	private Logger LOGGER = LoggerFactory.getLogger(ADCategoryFragment.class);

	private Context context;
	public ADCategoryFragment(Context context) {
		this.context = context;
	}
	
	/*public static ADCategoryFragment newInstance() {
		ADCategoryFragment fragment = new ADCategoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView=inflater.inflate(R.layout.view_goods_ad, container,false);
		return contentView;
	}
}
