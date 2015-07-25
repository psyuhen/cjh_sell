package com.cjh.fragment;

import com.cjh.activity.CategoryActivity;
import com.cjh.activity.GoodsActivity;
import com.cjh.activity.LoginActivity;
import com.cjh.activity.MarketingActivity;
import com.cjh.activity.OrderActivity;
import com.cjh.activity.ShopActivity;
import com.cjh.cjh_sell.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MeFragment extends Fragment implements OnClickListener {
	private RelativeLayout[] relativeLayouts = new RelativeLayout[5];
	private int ids[] = { R.id.me_order_rl, R.id.me_goods_rl,
			R.id.me_category_rl, R.id.me_marketing_rl, R.id.me_shop_rl };
	private Button login_btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View contentView = inflater.inflate(R.layout.fragmeng_me, container,
				false);
		for (int i = 0; i < relativeLayouts.length; i++) {
			relativeLayouts[i] = (RelativeLayout) contentView
					.findViewById(ids[i]);
			relativeLayouts[i].setOnClickListener(this);
		}
		login_btn = (Button) contentView.findViewById(R.id.me_login_btn);
		login_btn.setOnClickListener(this);
		return contentView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.me_order_rl:
			startActivity(new Intent(getActivity(), OrderActivity.class));
			break;
		case R.id.me_goods_rl:
			startActivity(new Intent(getActivity(), GoodsActivity.class));
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
		case R.id.me_login_btn:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		default:
			break;
		}
	}

}
