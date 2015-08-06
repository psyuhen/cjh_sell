package com.cjh.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cjh.auth.SessionManager;
import com.cjh.cjh_sell.R;
import com.cjh.fragment.MeFragment;
import com.cjh.fragment.OrderFragment;
import com.cjh.fragment.ShopFragment;
/**
 * 首页
 * @author ps
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
	// 静态fragment管理器
	private static FragmentManager fMgr;
	// 设置按钮
	private ImageButton settingBtn;
	// 编辑按钮
	private ImageButton edit_imaggbtn;
	private PopupWindow addshopPopWindow = null;
	private ImageButton shop_add_details_image_image;
	private ImageButton shop_add_head_image;
	private AlertDialog imageChooseDialog = null;
	public SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sessionManager = new SessionManager(getApplicationContext());
		setContentView(R.layout.activity_main);
		sessionManager.checkLogin();
		
		settingBtn = (ImageButton) findViewById(R.id.top_more_right);
		edit_imaggbtn = (ImageButton) findViewById(R.id.top_edit_right);
		initData();
		fMgr = getSupportFragmentManager();
		initFragment();
		dealBottomButtonsClickEvent();
	}

	private void initData() {
		settingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(MainActivity.this,
						SettingActivity.class));
			}
		});
	}

	/**
	 * 初始化首个Fragment
	 */
	private void initFragment() {
		FragmentTransaction ft = fMgr.beginTransaction();
		MeFragment homeFragment = new MeFragment();
		homeFragment.setContext(MainActivity.this);
		ft.add(R.id.fragmentRoot, homeFragment, "navFragment");

		ft.addToBackStack("navFragment");
		settingBtn.setVisibility(View.GONE);
		ft.commit();
	}

	/**
	 * 处理底部点击事件
	 */
	private void dealBottomButtonsClickEvent() {
		//首页导航
		findViewById(R.id.rbnav).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fMgr.findFragmentByTag("navFragment") != null
						&& fMgr.findFragmentByTag("navFragment").isVisible()) {
					return;
				}
				popAllFragmentsExceptTheBottomOne();
				initTopRightIcon();
			}

			private void initTopRightIcon() {
				
				settingBtn.setVisibility(View.GONE);
				edit_imaggbtn.setVisibility(View.GONE);
			}
		});
		//订单
		findViewById(R.id.rbHome).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("navFragment"));
				OrderFragment gf = new OrderFragment();
				gf.setContext(MainActivity.this);
				ft.add(R.id.fragmentRoot, gf, "OrderFragment");
				ft.addToBackStack("OrderFragment");
				ft.commit();
				initTopRightIcon();
			}

			private void initTopRightIcon() {
				
				settingBtn.setVisibility(View.GONE);
				edit_imaggbtn.setVisibility(View.GONE);
			}
		});
		//商家
		findViewById(R.id.rbMe).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("navFragment"));
				ShopFragment mg = new ShopFragment();
				mg.setContext(MainActivity.this);
				ft.add(R.id.fragmentRoot, mg, "ShopFragment");
				ft.addToBackStack("ShopFragment");
				ft.commit();
				initTopRightIcon();
			}

			private void initTopRightIcon() {
				settingBtn.setVisibility(View.GONE);
				edit_imaggbtn.setVisibility(View.GONE);
				edit_imaggbtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						showpopaddshop(MainActivity.this, v);
					}
				});
			}
		});
		// findViewById(R.id.rbMe).setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// 
		// popAllFragmentsExceptTheBottomOne();
		// FragmentTransaction ft = fMgr.beginTransaction();
		// ft.hide(fMgr.findFragmentByTag("homeFragment"));
		// MeFragment mg = new MeFragment();
		// ft.add(R.id.fragmentRoot, mg, "MeFragment");
		// ft.addToBackStack("MeFragment");
		// ft.commit();
		// }
		// });

	}

	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
			fMgr.popBackStack();
		}
	}

	// 点击返回按钮
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@Override
	public void onBackPressed() {
		if (fMgr.findFragmentByTag("navFragment") != null
				&& fMgr.findFragmentByTag("navFragment").isVisible()) {
			MainActivity.this.finish();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_main_head_left_text:
			addshopPopWindow.dismiss();
			break;
		case R.id.pop_main_head_right_text:
			addshopPopWindow.dismiss();
			break;
		case R.id.shop_add_details_image_image:
			showImageChoose();
			break;
		case R.id.shop_add_head_image:
			showImageChoose();
			break;
		case R.id.dialog_album:
			imageChooseDialog.dismiss();
			break;
		case R.id.dialog_cancel:
			imageChooseDialog.dismiss();
			break;
		default:
			break;
		}
	}

	private void showImageChoose() {
		imageChooseDialog = new AlertDialog.Builder(MainActivity.this).create();
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

	private void showpopaddshop(Context context, View parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.pop_add_shop, null,false);
		// 屏幕高度和宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		addshopPopWindow = new PopupWindow(vPopWindow, dm.widthPixels,
				dm.heightPixels - 50, true);
		addshopPopWindow.showAtLocation(parent, Gravity.CENTER, 0, 50);
		TextView left_text = (TextView) vPopWindow
				.findViewById(R.id.pop_main_head_left_text);
		left_text.setOnClickListener(this);
		TextView right_text = (TextView) vPopWindow
				.findViewById(R.id.pop_main_head_right_text);
		right_text.setOnClickListener(this);
		TextView pop_main_head_text = (TextView) vPopWindow
				.findViewById(R.id.pop_main_head_text);
		pop_main_head_text.setText("快捷广告推广");
		shop_add_details_image_image = (ImageButton) vPopWindow
				.findViewById(R.id.shop_add_details_image_image);
		shop_add_details_image_image.setOnClickListener(this);
		shop_add_head_image = (ImageButton) vPopWindow
				.findViewById(R.id.shop_add_head_image);
		shop_add_head_image.setOnClickListener(this);
	}

}
