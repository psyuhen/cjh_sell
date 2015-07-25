package com.cjh.fragment;

import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjh.activity.ShopActivity;
import com.cjh.activity.ShopEditActivity;
import com.cjh.bean.Store;
import com.cjh.bean.User;
import com.cjh.cjh_sell.R;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.auth.SessionManager;

/**
 * 店铺名片展示
 * @author ps
 *
 */
public class ShopMyFragment extends Fragment implements OnClickListener {
	public static final String TAG = "ShopMyFragment"; 
	private Context context;
	
	private LinearLayout view_in_shop_content_ll;
	private TextView shop_my_title;
	private TextView shop_my_name;
	private TextView shop_my_address;
	private TextView shop_my_tel;
	

	private RelativeLayout shop_nav_item_delivery_rl;
	private RelativeLayout shop_nav_item_supply_rl;
	private RelativeLayout shop_nav_item_clientservice_rl;

	/*public static ShopMyFragment newInstance() {
		ShopMyFragment fragment = new ShopMyFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}*/
	
	public ShopMyFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_in_my_shop,
				container, false);
		view_in_shop_content_ll = (LinearLayout) contentView
				.findViewById(R.id.view_in_shop_content_ll);
		view_in_shop_content_ll.setOnClickListener(this);
		
		shop_nav_item_delivery_rl = (RelativeLayout) contentView
				.findViewById(R.id.shop_nav_item_delivery_rl);
		shop_nav_item_delivery_rl.setOnClickListener(this);
		shop_nav_item_supply_rl = (RelativeLayout) contentView
				.findViewById(R.id.shop_nav_item_supply_rl);
		shop_nav_item_supply_rl.setOnClickListener(this);
		shop_nav_item_clientservice_rl = (RelativeLayout) contentView
				.findViewById(R.id.shop_nav_item_clientservice_rl);
		shop_nav_item_clientservice_rl.setOnClickListener(this);
		
		
		shop_my_title = (TextView)contentView.findViewById(R.id.shop_my_title);
		shop_my_name = (TextView)contentView.findViewById(R.id.shop_my_name);
		shop_my_address = (TextView)contentView.findViewById(R.id.shop_my_address);
		shop_my_tel = (TextView)contentView.findViewById(R.id.shop_my_tel);
		
		querybyuserid();
		return contentView;
	}
	
	private void querybyuserid(){
		ShopActivity activity = (ShopActivity)context;
		User user = activity.sessionManager.getUserDetails();
		//根据用户查询商家信息
		String url1 = HttpUtil.BASE_URL + "/store/querybyuser.do?user_id="+user.getUser_id();
		try {
			String json = HttpUtil.getRequest(url1);
			if(json != null){
				Store store = JsonUtil.parse2Object(json, Store.class);
				
				shop_my_title.setText(store.getName());
				shop_my_name.setText(user.getName());
				shop_my_address.setText(store.getAddress());
				shop_my_tel.setText(store.getPhone());
				activity.sessionManager.put("store_id", ""+store.getStore_id());
			}
		} catch (InterruptedException e) {
			Log.e(TAG, "根据用户获取商家信息失败", e);
		} catch (ExecutionException e) {
			Log.e(TAG, "根据用户获取商家信息失败", e);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_in_shop_content_ll:
			startActivity(new Intent(getActivity(), ShopEditActivity.class));
			break;
		case R.id.shop_nav_item_delivery_rl:
			Toast.makeText(getActivity(), "送货员", Toast.LENGTH_SHORT).show();
			break;
		case R.id.shop_nav_item_supply_rl:
			Toast.makeText(getActivity(), "供货链", Toast.LENGTH_SHORT).show();
			break;
		case R.id.shop_nav_item_clientservice_rl:
			Toast.makeText(getActivity(), "客户组", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

}
