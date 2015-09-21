package com.cjh.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.cjh.cjh_sell.R;
import com.cjh.common.MyGridView;

/**
 * 营销中心
 * @author pansen
 *
 */
public class AdActivity extends BaseTwoActivity {
	private MyGridView mGridView;
	private List<HashMap<String, Object>> maps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad);
		initView();
		initData();
	}

	@Override
	public void initView() {
		
		super.initView();
		mGridView = (MyGridView) findViewById(R.id.ad_gridview);
		maps = new ArrayList<HashMap<String, Object>>();
	}

	private void initData() {
		
		right_imgbtn.setVisibility(View.GONE);
		title.setText("营销推广");
		loadLocalData();

		SimpleAdapter adapter = new SimpleAdapter(AdActivity.this, maps,
				R.layout.item_ad, new String[] { "image", "title" }, new int[] {
						R.id.item_ad_image, R.id.item_ad_title });
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new Helper());
	}

	class Helper implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long len) {
			
//			HashMap<String, Object> map = maps.get(position);
//			String title = (String) map.get("title");
			switch (position) {
			case 0:
				startActivity(new Intent(AdActivity.this, CouponsActivity.class));
				break;
			case 1:
				startActivity(new Intent(AdActivity.this, LimitTimeActivity.class));
				break;
			case 2:
				startActivity(new Intent(AdActivity.this, SecretDiscountActivity.class));
				break;
			case 3:
				startActivity(new Intent(AdActivity.this, FreeShippingActivity.class));
				break;
			default:
				break;
			}
		}

	}

	private void loadLocalData() {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title", "店铺优惠券");
		map.put("image", R.drawable.ic_ad_ticket);
		maps.add(map);
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("title", "限时折扣");
		map1.put("image", R.drawable.ic_ad_time);
		maps.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("title", "私密优惠");
		map2.put("image", R.drawable.ic_ad_secret);
		maps.add(map2);
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("title", "满包邮");
		map3.put("image", R.drawable.ic_ad_trunk);
		maps.add(map3);

	}

}
