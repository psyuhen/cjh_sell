package com.cjh.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.cjh.adapter.ShopFragmentActivityAdapter;
import com.cjh.cjh_sell.R;

/**
 * 
 * 店铺管理
 * @author ps
 *
 */
public class ShopActivity extends BaseTwoActivity implements OnClickListener {
	public static final String TAG = "ShopActivity";
	
	private ViewPager mViewPager;
	private ShopFragmentActivityAdapter mFragmentAdapter;
	private ImageButton edit_imaggbtn;

	private PopupWindow addshopPopWindow = null;

//	private ImageView content_add_image;
//
//	private AlertDialog imageChooseDialog = null;

	private RelativeLayout shop_left_rl;
	private RelativeLayout shop_right_rl;

	private View shop_left_line;
	private View shop_right_line;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		sessionManager.checkLogin();
		
		initView();
		initData();
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					shop_left_line.setVisibility(View.VISIBLE);
					shop_right_line.setVisibility(View.INVISIBLE);
					break;
				case 1:
					shop_left_line.setVisibility(View.INVISIBLE);
					shop_right_line.setVisibility(View.VISIBLE);
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
	public void initView() {
		super.initView();
		mViewPager = (ViewPager) findViewById(R.id.shop_viewpager);
		edit_imaggbtn = (ImageButton) findViewById(R.id.right_imgbtn);
		edit_imaggbtn.setOnClickListener(this);
		edit_imaggbtn.setVisibility(View.GONE);
		shop_left_rl = (RelativeLayout) findViewById(R.id.shop_left_rl);
		shop_left_rl.setOnClickListener(this);
		shop_right_rl = (RelativeLayout) findViewById(R.id.shop_right_rl);
		shop_right_rl.setOnClickListener(this);
		shop_left_line = findViewById(R.id.shop_left_line);
		shop_right_line = findViewById(R.id.shop_right_line);
	}

	private void initData() {
		title.setText("店铺管理");
		mFragmentAdapter = new ShopFragmentActivityAdapter(
				getSupportFragmentManager(), this);
		mViewPager.setAdapter(mFragmentAdapter);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_imgbtn:
			break;
		case R.id.pop_main_head_left_text:
			addshopPopWindow.dismiss();
			break;
		case R.id.pop_main_head_right_text:
			addshopPopWindow.dismiss();
			break;
		case R.id.shop_left_rl:
			shop_left_line.setVisibility(View.VISIBLE);
			shop_right_line.setVisibility(View.INVISIBLE);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.shop_right_rl:
			shop_left_line.setVisibility(View.INVISIBLE);
			shop_right_line.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
}
