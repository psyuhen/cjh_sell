package com.cjh.activity;

import com.cjh.adapter.CategoryFragmentAdapter;
import com.cjh.cjh_sell.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
/**
 * 类型管理
 * @author ps
 *
 */
public class CategoryActivity extends BaseActivity {
	private ViewPager mViewPager;
	private CategoryFragmentAdapter mFragmentAdapter;
	private RelativeLayout category_left_rl;
	private RelativeLayout ad_right_rl;

	private View category_left_line;
	private View ad_right_line;
	private ImageButton edit_imaggbtn;

	private PopupWindow addADPopWindow = null;

	private AlertDialog imageChooseDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		initView();
		initData();
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					category_left_line.setVisibility(View.VISIBLE);
					ad_right_line.setVisibility(View.INVISIBLE);
					break;
				case 1:
					category_left_line.setVisibility(View.INVISIBLE);
					ad_right_line.setVisibility(View.VISIBLE);
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
		mViewPager = (ViewPager) findViewById(R.id.category_viewpager);
		mFragmentAdapter = new CategoryFragmentAdapter(
				getSupportFragmentManager(), this);
		category_left_rl = (RelativeLayout) findViewById(R.id.category_left_rl);
		category_left_rl.setOnClickListener(this);
		ad_right_rl = (RelativeLayout) findViewById(R.id.ad_right_rl);
		ad_right_rl.setOnClickListener(this);
		category_left_line = findViewById(R.id.category_left_line);
		ad_right_line = findViewById(R.id.ad_right_line);
		edit_imaggbtn = (ImageButton) findViewById(R.id.right_imgbtn);
		edit_imaggbtn.setOnClickListener(this);
	}

	private void initData() {
		title.setText("类别管理");
		mViewPager.setAdapter(mFragmentAdapter);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.category_left_rl:
			category_left_line.setVisibility(View.VISIBLE);
			ad_right_line.setVisibility(View.INVISIBLE);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.ad_right_rl:
			category_left_line.setVisibility(View.INVISIBLE);
			ad_right_line.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(2);
			break;
		case R.id.right_imgbtn:
			if (mViewPager.getCurrentItem() == 0) {
				startActivity(new Intent(CategoryActivity.this,
						CategoryAddActivity.class));
			}
			if (mViewPager.getCurrentItem() == 1) {
				startActivity(new Intent(CategoryActivity.this,
						AdAddActivity.class));
			}
			break;
		case R.id.pop_main_head_left_text:
			addADPopWindow.dismiss();
			break;
		case R.id.pop_main_head_right_text:
			addADPopWindow.dismiss();
			break;
		case R.id.content_add_image:
			showImageChoose();
			break;
		case R.id.dialog_album:
			imageChooseDialog.dismiss();
			break;
		case R.id.dialog_cancel:
			imageChooseDialog.dismiss();
			break;
		}
	}

	private void showImageChoose() {
		imageChooseDialog = new AlertDialog.Builder(CategoryActivity.this)
				.create();
		imageChooseDialog.show();
		imageChooseDialog.getWindow().setContentView(
				R.layout.image_choose_dialog);
		Button dialog_album = (Button) imageChooseDialog
				.findViewById(R.id.dialog_album);
		dialog_album.setOnClickListener(this);
		Button dialog_cancel = (Button) imageChooseDialog
				.findViewById(R.id.dialog_cancel);
		dialog_cancel.setOnClickListener(this);
	}

}
