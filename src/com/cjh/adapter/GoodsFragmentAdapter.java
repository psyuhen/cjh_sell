package com.cjh.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cjh.fragment.GoodsCategoryFragment;
import com.cjh.fragment.GoodsOnOfferFragment;
import com.cjh.fragment.GoodsSoldOutFragment;
/**
 * 商品列表适配器
 * @author ps
 *
 */
public class GoodsFragmentAdapter extends FragmentPagerAdapter{

	private int pageCount = 3;
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
			return new GoodsOnOfferFragment(context);//出售中
		case 1:
			return new GoodsSoldOutFragment(context);//已下架
		case 2:
			return new GoodsCategoryFragment(context);//分类
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
