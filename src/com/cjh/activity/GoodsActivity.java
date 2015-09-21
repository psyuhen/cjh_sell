package com.cjh.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.kymjs.aframe.ui.widget.KJListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjh.adapter.GoodsFragmentAdapter;
import com.cjh.adapter.GoodsOnofferAdapter;
import com.cjh.auth.SessionManager;
import com.cjh.bean.GoodsItem;
import com.cjh.bean.MerchInfo;
import com.cjh.bean.Store;
import com.cjh.bean.User;
import com.cjh.cjh_sell.R;
import com.cjh.utils.FileUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 商品管理
 * @author ps
 *
 */
public class GoodsActivity extends BaseTwoActivity implements OnClickListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsActivity.class);

	private TextView order_title;
	
	//优化改动的
	private KJListView goods_listview;
	private GoodsOnofferAdapter adapter;
	private List<GoodsItem> goodsList;

	//分类ID
	private int classify_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		sessionManager.checkLogin();
		
		initView();
		initData();
	}

	private void initData() {
		//title.setText("商品管理");

		//queryMerch();
		
		adapter=new GoodsOnofferAdapter(goodsList, GoodsActivity.this);
		goods_listview.setAdapter(adapter);
		
		String storeName = sessionManager.get("store_name");
		//order_title.setText(storeName);
	}

	@Override
	public void initView() {
		super.initView();
//		title.setText("果蔬生鲜");
		goodsList = new ArrayList<GoodsItem>();
		right_imgbtn.setVisibility(View.GONE);
		goods_listview=(KJListView) findViewById(R.id.goods_listview);
		
		Intent intent = getIntent();
		String from = intent.getStringExtra("from");
		if("GoodsCategoryFragment".equals(from)){//从分类里面跳转过来的
			classify_id = intent.getIntExtra("classify_id", 0);
			String title = intent.getStringExtra("classify_name");
			this.title.setText(title);
			queryMerchByUser();
		}
	}

	//查询商品信息
	private void queryMerch(){
		int store_id = sessionManager.getInt("store_id");
		//根据商家ID查询商品信息
		String url = HttpUtil.BASE_URL + "/merch/querybystoreid.do?store_id="+store_id;
		try {
			String json = HttpUtil.getRequest(url);
			if(json != null){
				List<MerchInfo> list = JsonUtil.parse2ListMerchInfo(json);
				int length = list.size();
				
				for (int i = 0; i < length; i++) {
					MerchInfo merchInfo = list.get(i);
					
					GoodsItem goodsItem = new GoodsItem();
					goodsItem.setId(merchInfo.getMerch_id());
//					goodsItem.setImg("image");
					goodsItem.setPrice(merchInfo.getPrice());
					goodsItem.setSellmount(merchInfo.getSales_volume());//销量
					goodsItem.setStandard(merchInfo.getUnit());
					goodsItem.setStock(merchInfo.getIn_stock());
					goodsItem.setTitle(merchInfo.getName());
					
					goodsItem.setBitmap(FileUtil.getCacheFile(merchInfo.getImage_name()));
					goodsList.add(goodsItem);
				}
			}
		} catch (InterruptedException e) {
			LOGGER.error(">>> 根据商家ID查询商品信息失败",e);
		} catch (ExecutionException e) {
			LOGGER.error(">>> 根据商家ID查询商品信息失败",e);
		}
	}
	//查询商品信息
	private void queryMerchByUser(){
		//根据商家ID查询商品信息
		MerchInfo merch = new MerchInfo();
		merch.setClassify_id(classify_id);
		//?start=&limit=
		//TODO 未作分页
		String url = HttpUtil.BASE_URL + "/merch/queryby.do";
		try {
			String json = HttpUtil.postRequest(url,merch);
			if(json != null){
				List<MerchInfo> list = JsonUtil.parse2ListMerchInfo(json);
				int length = list.size();
				
				for (int i = 0; i < length; i++) {
					MerchInfo merchInfo = list.get(i);
					
					GoodsItem goodsItem = new GoodsItem();
					goodsItem.setId(merchInfo.getMerch_id());
//					goodsItem.setImg("image");
					goodsItem.setPrice(merchInfo.getPrice());
					goodsItem.setSellmount(merchInfo.getSales_volume());//销量
					goodsItem.setStandard(merchInfo.getUnit());
					goodsItem.setStock(merchInfo.getIn_stock());
					goodsItem.setTitle(merchInfo.getName());
					
					goodsItem.setBitmap(FileUtil.getCacheFile(merchInfo.getImage_name()));
					goodsList.add(goodsItem);
				}
			}
		} catch (InterruptedException e) {
			LOGGER.error(">>> 根据分类ID查询商品信息失败",e);
		} catch (ExecutionException e) {
			LOGGER.error(">>> 根据分类ID查询商品信息失败",e);
		}
	}
}
