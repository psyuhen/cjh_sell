package com.cjh.adapter;



import com.cjh.fragment.ShopDecorateFragment;
import com.cjh.fragment.ShopMyFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ShopFragmentActivityAdapter extends FragmentPagerAdapter{
	private int pageCount=1;  //页面数
	private Context context;
	/*
	 * 构造函数
	 */
	public ShopFragmentActivityAdapter(FragmentManager fm) {
		this(fm, null);
	}
	public ShopFragmentActivityAdapter(FragmentManager fm,Context context) {
		super(fm);
		this.context = context;
	}
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 *获取每一个页面的内容
	 */
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		//正在进行的订单
		case 0:
			return new ShopMyFragment(context);
		//已完成的订单
		case 1:
			return ShopDecorateFragment.newInstance();
		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return super.getPageTitle(position);
	}
	/**
	 * destory当前的pager
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	/**
	 * 获取总数
	 */
	@Override
	public int getCount() {
		return pageCount;
	}

}
