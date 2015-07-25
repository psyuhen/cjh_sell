package com.cjh.adapter;


import com.cjh.fragment.GoodsOnOfferFragment;
import com.cjh.fragment.GoodsSoldOutFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * 商品列表适配器
 * @author ps
 *
 */
public class GoodsFragmentAdapter extends FragmentPagerAdapter{

	private int pageCount=2;
	private Context context;
	
	public GoodsFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	public GoodsFragmentAdapter(FragmentManager fm,Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new GoodsOnOfferFragment(context);
		case 1:
			return new GoodsSoldOutFragment(context);
		
		}
		return null;
	}
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	@Override
	public int getCount() {
		return pageCount;
	}

}
