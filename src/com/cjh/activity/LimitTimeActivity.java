package com.cjh.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cjh.adapter.GoodsOnofferAdapter;
import com.cjh.bean.GoodsItem;
import com.cjh.cjh_sell.R;

public class LimitTimeActivity extends BaseTwoActivity {
	private ListView mListView;
	private List<GoodsItem> goodsList;
	private GoodsOnofferAdapter goodsOnofferAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_limit_time);
		initView();
		initData();
		goodsOnofferAdapter = new GoodsOnofferAdapter(goodsList,
				LimitTimeActivity.this);
		mListView.setAdapter(goodsOnofferAdapter);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		title.setText("限时折扣");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setVisibility(View.VISIBLE);
		right_text.setText("+ 添加");
		right_text.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.mListView);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_text:
			startActivity(new Intent(LimitTimeActivity.this,
					LimitTimeAddActivity.class));
			break;

		default:
			break;
		}
	}
}
