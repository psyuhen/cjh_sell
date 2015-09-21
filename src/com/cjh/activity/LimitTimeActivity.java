package com.cjh.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.kymjs.aframe.ui.widget.KJListView;
import org.kymjs.aframe.ui.widget.KJListView.KJListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cjh.adapter.GoodsOnofferAdapter;
import com.cjh.auth.SessionManager;
import com.cjh.bean.GoodsItem;
import com.cjh.bean.MerchInfo;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.FileUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.PageUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 限时折扣
 * @author pansen
 *
 */
public class LimitTimeActivity extends BaseTwoActivity {
	private static final Logger LOGGER = LoggerFactory.getLogger(LimitTimeActivity.class);

	private KJListView kjListView;
	private List<GoodsItem> goodsList;
	private GoodsOnofferAdapter goodsOnofferAdapter;
	
	private int start = PageUtil.START;//分页的开始

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_limit_time);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		title.setText("限时折扣");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setVisibility(View.VISIBLE);
		right_text.setText("+ 添加");
		right_text.setOnClickListener(this);
		kjListView = (KJListView) findViewById(R.id.mListView);
		goodsList = new ArrayList<GoodsItem>();
		goodsOnofferAdapter = new GoodsOnofferAdapter(goodsList, LimitTimeActivity.this);
		kjListView.setAdapter(goodsOnofferAdapter);
		//上下拉刷新
		kjListView.setPullLoadEnable(true);
		kjListView.setKJListViewListener(new KJListViewListener() {
			@Override
			public void onRefresh() {
				querybyuserid(PageUtil.START);
			}

			@Override
			public void onLoadMore() {
				querybyuserid(LimitTimeActivity.this.start);
			}
		});
	}

	private void initData() {
		querybyuserid(PageUtil.START);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_text://添加
			startActivity(new Intent(LimitTimeActivity.this, LimitTimeAddActivity.class));
			break;

		default:
			break;
		}
	}
	
	//根据用户查询商品信息
	private void querybyuserid(int start){
		int user_id = sessionManager.getInt(SessionManager.KEY_USER_ID);
		
		MerchInfo info = new MerchInfo();
		info.setUser_id(user_id);
		info.setOut_published("0");
		
		String url = HttpUtil.BASE_URL + "/merch/querybypage.do?start="+start+"&limit="+PageUtil.LIMIT;
		
		try {
			String listJson = HttpUtil.postRequest(url,info);
			if(listJson == null){
				CommonsUtil.showLongToast(getApplicationContext(), "查询商品列表失败");
				kjListView.stopRefreshData();
				return;
			}
			
			List<MerchInfo> list = JsonUtil.parse2ListMerchInfo(listJson);
			if(list == null){
				LOGGER.warn("转换商品列表信息失败");
				kjListView.stopRefreshData();
				return;
			}
			
			int length = list.size();
			if(length == 0){
				kjListView.stopRefreshData();
				return;
			}
			
			//默认开始的时候，先清空列表数据
			if(start == PageUtil.START){
				goodsList.clear();
			}
			
			for (int i = 0; i < length; i++) {
				MerchInfo merchInfo = list.get(i);
				GoodsItem goodsItem = new GoodsItem();
				goodsItem.setId(merchInfo.getMerch_id());
//				goodsItem.setImg("image");
				goodsItem.setPrice(merchInfo.getPrice());
				goodsItem.setSellmount(merchInfo.getSales_volume());
				goodsItem.setStandard(merchInfo.getUnit());
				goodsItem.setStock(merchInfo.getIn_stock());
				goodsItem.setTitle(merchInfo.getName());
				goodsItem.setCreate_time(merchInfo.getCreate_time());
				
				goodsItem.setBitmap(FileUtil.getCacheFile(merchInfo.getImage_name()));
				
				goodsItem.setMerchDisacounts(merchInfo.getMerchDisacounts());
				goodsList.add(goodsItem);
			}
			
			this.start += PageUtil.LIMIT;//每次改变start的值 
			goodsOnofferAdapter.notifyDataSetChanged();
			kjListView.stopRefreshData();
		} catch (InterruptedException e) {
			LOGGER.error("查询商品列表失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询商品列表失败");
		} catch (ExecutionException e) {
			LOGGER.error("查询商品列表失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询商品列表失败");
		}
		
	}
}
