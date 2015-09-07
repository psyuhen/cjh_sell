package com.cjh.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cjh.adapter.GoodsOnofferAdapter;
import com.cjh.bean.GoodsItem;
import com.cjh.cjh_sell.R;

public class LimitTimeAddActivity extends BaseTwoActivity {
	private ListView mListView;
	private List<GoodsItem> goodsList;
	private GoodsOnofferAdapter goodsOnofferAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_limit_time_add);
		initView();
		initData();
		goodsOnofferAdapter = new GoodsOnofferAdapter(goodsList,
				LimitTimeAddActivity.this);
		mListView.setAdapter(goodsOnofferAdapter);
		mListView.setOnItemClickListener(new Helper());
	}

	private class Helper implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			startActivity(new Intent(LimitTimeAddActivity.this, LimitTimeDetailsAddActivity.class));
		}
		
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		right_imgbtn.setVisibility(View.GONE);
		title.setText("选择折扣商品");
		mListView = (ListView) findViewById(R.id.mListView);
		goodsList = new ArrayList<GoodsItem>();
	}

	private void initData() {
		// TODO Auto-generated method stub
		goodsList = new ArrayList<GoodsItem>();
		for (int i = 3; i < 13; i++) {
			GoodsItem goodsItem = new GoodsItem();
			goodsItem.setId(i);
			goodsItem.setImg("image");
			goodsItem.setPrice(i * 12);
			goodsItem.setSellmount(i * 23 + 34);
			goodsItem.setStandard("个");
			goodsItem.setStock(i * 34 - 6);
			goodsItem.setTitle("霜降更甜10斤 山东烟台栖霞苹果水果新鲜 红富士苹果");
			goodsList.add(goodsItem);
		}
	}

}
