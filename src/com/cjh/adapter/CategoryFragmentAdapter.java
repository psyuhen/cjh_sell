package com.cjh.adapter;

import com.cjh.fragment.ADCategoryFragment;
import com.cjh.fragment.GoodsCategoryFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CategoryFragmentAdapter extends FragmentPagerAdapter {

	private int pageCount = 2;
	private Context context;

	public CategoryFragmentAdapter(FragmentManager fm) {
		this(fm,null);
	}

	public CategoryFragmentAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		
		switch (position) {
		case 0:
			return GoodsCategoryFragment.newInstance();
		case 1:
			return ADCategoryFragment.newInstance();
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
