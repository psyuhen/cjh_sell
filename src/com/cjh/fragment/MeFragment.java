package com.cjh.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjh.activity.AboutUsActivity;
import com.cjh.activity.AdActivity;
import com.cjh.activity.CategoryActivity;
import com.cjh.activity.GoodActivity;
import com.cjh.activity.MainActivity;
import com.cjh.activity.MarketingActivity;
import com.cjh.activity.OrdersActivity;
import com.cjh.activity.ShopActivity;
import com.cjh.cjh_sell.R;
/**
 * 首页民航
 * @author ps
 *
 */
public class MeFragment extends Fragment implements OnClickListener {
	private RelativeLayout[] relativeLayouts = new RelativeLayout[7];
	private int ids[] = { R.id.me_order_rl, R.id.me_goods_rl,
			R.id.me_category_rl, R.id.me_marketing_rl, R.id.me_ad_rl,
			R.id.me_shop_rl, R.id.me_help_ll };
//	private Button login_btn;
	
	private TextView order_title;
	
	private Context context;
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		View contentView = inflater.inflate(R.layout.fragmeng_me, container,
				false);
		for (int i = 0; i < relativeLayouts.length; i++) {
			relativeLayouts[i] = (RelativeLayout) contentView
					.findViewById(ids[i]);
			relativeLayouts[i].setOnClickListener(this);
		}
//		login_btn = (Button) contentView.findViewById(R.id.me_login_btn);
//		login_btn.setOnClickListener(this);
		order_title = (TextView)contentView.findViewById(R.id.order_title);
		
		initData();
		
		return contentView;
	}
	
	//显示商家名称，从MainActivity里面获取，不再查询数据库
	private void initData(){
		if(this.context != null){
			MainActivity activity = (MainActivity)this.context;
			String storeName = activity.sessionManager.getStoreName();
			order_title.setText(storeName);
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.me_order_rl:
			startActivity(new Intent(getActivity(), OrdersActivity.class));
			break;
		case R.id.me_goods_rl:
			startActivity(new Intent(getActivity(), GoodActivity.class));
			break;
		case R.id.me_category_rl:
			startActivity(new Intent(getActivity(), CategoryActivity.class));
			break;
		case R.id.me_marketing_rl:
			startActivity(new Intent(getActivity(), MarketingActivity.class));
			break;
		case R.id.me_shop_rl:
			startActivity(new Intent(getActivity(), ShopActivity.class));
			break;
		/*case R.id.me_login_btn:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;*/
		case R.id.me_ad_rl:
			startActivity(new Intent(getActivity(), AdActivity.class));
			break;
		case R.id.me_help_ll:
			startActivity(new Intent(getActivity(), AboutUsActivity.class));
			break;
		default:
			break;
		}
	}

}
