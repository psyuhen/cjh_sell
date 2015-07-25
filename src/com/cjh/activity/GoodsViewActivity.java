package com.cjh.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjh.cjh_sell.R;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 商品预览
 * @author ps
 *
 */
public class GoodsViewActivity extends BaseTwoActivity {
	private ListView goodsViewListView;
	private List<Map<String, Object>> maps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_view);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		goodsViewListView = (ListView) findViewById(R.id.goods_view_listview);
		maps = new ArrayList<Map<String, Object>>();
	}

	private void initData() {
		title.setText("预览");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setVisibility(View.VISIBLE);
		right_text.setText("刷新");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("image", R.drawable.pg1);
		maps.add(map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("image", R.drawable.pg2);
		maps.add(map2);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("image", R.drawable.pg3);
		maps.add(map3);
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("image", R.drawable.pg4);
		maps.add(map4);
		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("image", R.drawable.pg5);
		maps.add(map5);
		Map<String, Object> map6 = new HashMap<String, Object>();
		map6.put("image", R.drawable.pg6);
		maps.add(map6);
		Map<String, Object> map7 = new HashMap<String, Object>();
		map7.put("image", R.drawable.pg7);
		maps.add(map7);
		SimpleAdapter simpleAdapter = new SimpleAdapter(GoodsViewActivity.this,
				maps, R.layout.item_goods_view, new String[] { "image" },
				new int[] { R.id.item_goods_view_image });
		goodsViewListView.setAdapter(simpleAdapter);
		setListViewHeight(goodsViewListView);
	}

	/**
	 * 设置listview高度，防止Listview只显示第一条
	 * 
	 * @param listView 
	 */
	public static void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(1, 1);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ listView.getPaddingTop() + listView.getPaddingBottom();
		listView.setLayoutParams(params);
	}
}
