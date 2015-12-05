package com.cjh.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.aframe.ui.widget.KJListView;
import org.kymjs.aframe.ui.widget.KJListView.KJListViewListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cjh.adapter.GoodsOnofferAdapter;
import com.cjh.bean.GoodsItem;
import com.cjh.bean.MerchInfo;
import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.cjh.utils.FileUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.PageUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 商品管理
 * @author ps
 *
 */
public class GoodsActivity extends BaseTwoActivity implements OnClickListener {
	private Logger LOGGER = LoggerFactory.getLogger(GoodsActivity.class);

//	private TextView order_title;
	
	//优化改动的
	private KJListView goods_listview;
	private GoodsOnofferAdapter adapter;
	private List<GoodsItem> goodsList;

	//分类ID
	private int classify_id;
	private int start = PageUtil.START;
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
		
		//String storeName = sessionManager.get("store_name");
		//order_title.setText(storeName);
	}

	@Override
	public void initView() {
		super.initView();
//		title.setText("果蔬生鲜");
		goodsList = new ArrayList<GoodsItem>();
		right_imgbtn.setVisibility(View.GONE);
		goods_listview=(KJListView) findViewById(R.id.goods_listview);
		//列表点击事件
		goods_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View convertView, int position,
					long len) {
				GoodsItem item = (GoodsItem)parent.getItemAtPosition(position);
				
				Intent intent=new Intent(getApplicationContext(), GoodsDetailsActivity.class);
				intent.putExtra("merch_id", item.getId());
				intent.putExtra("from", "GoodsActivity");//来自
				startActivityForResult(intent, Constants.CATEGORY_REQUEST_CODE);
			}
		});
		
		//上下拉刷新
		goods_listview.setPullLoadEnable(true);
		goods_listview.setKJListViewListener(new KJListViewListener() {
			@Override
			public void onRefresh() {//重新刷新
				GoodsActivity.this.start = PageUtil.START;
				queryMerchByUser(PageUtil.START);
			}

			@Override
			public void onLoadMore() {//加载更多
				queryMerchByUser(GoodsActivity.this.start);
			}
		});
		
		Intent intent = getIntent();
		String from = intent.getStringExtra("from");
		if("GoodsCategoryFragment".equals(from)){//从分类里面跳转过来的
			classify_id = intent.getIntExtra("classify_id", 0);
			String title = intent.getStringExtra("classify_name");
			this.title.setText(title);
			queryMerchByUser(PageUtil.START);
		}
	}

	//查询商品信息
	/*private void queryMerch(){
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
					goodsItem.setDesc(merchInfo.getDesc());
					goodsItem.setPrice(merchInfo.getPrice());
					goodsItem.setSellmount(merchInfo.getSales_volume());//销量
					goodsItem.setStandard(merchInfo.getUnit());
					goodsItem.setStock(merchInfo.getIn_stock());
					goodsItem.setTitle(merchInfo.getName());
					
					goodsItem.setBitmap(FileUtil.getCacheFile(merchInfo.getImage_name()));
					goodsList.add(goodsItem);
				}
			}
		} catch (Exception e) {
			LOGGER.error(">>> 根据商家ID查询商品信息失败",e);
		}
	}*/
	//根据分类查询商品
	private class queryGoodsTask extends AsyncTask<Integer, Void, List<GoodsItem>>{
		private int start;
		@Override
		protected List<GoodsItem> doInBackground(Integer... params) {
			int start = params[0];
			this.start = start;
			//根据商家ID查询商品信息
			MerchInfo merch = new MerchInfo();
			merch.setClassify_id(classify_id);
			//TODO 未作分页
			String url = HttpUtil.BASE_URL + "/merch/querybypage.do?start="+start+"&limit="+PageUtil.LIMIT;
			
			List<GoodsItem> tmpList = new ArrayList<GoodsItem>();
			try {
				String json = HttpUtil.postRequest(url,merch);
				if(json != null){
					List<MerchInfo> list = JsonUtil.parse2ListMerchInfo(json);
					int length = list.size();
					
					if(length == 0){
						return tmpList;
					}
					
					for (int i = 0; i < length; i++) {
						MerchInfo merchInfo = list.get(i);
						
						GoodsItem goodsItem = new GoodsItem();
						goodsItem.setId(merchInfo.getMerch_id());
						goodsItem.setDesc(merchInfo.getDesc());
						goodsItem.setPrice(merchInfo.getPrice());
						goodsItem.setSellmount(merchInfo.getSales_volume());//销量
						goodsItem.setStandard(merchInfo.getUnit());
						goodsItem.setStock(merchInfo.getIn_stock());
						goodsItem.setTitle(merchInfo.getName());
						
						goodsItem.setBitmap(FileUtil.getCacheFile(merchInfo.getImage_name()));
						tmpList.add(goodsItem);
					}
				}
			} catch (Exception e) {
				LOGGER.error(">>> 根据分类ID查询商品信息失败",e);
			}
			return tmpList;
		}
		
		@Override
		protected void onPostExecute(List<GoodsItem> result) {
			super.onPostExecute(result);
			
			//默认开始的时候，先清空列表数据
			if(start == PageUtil.START){
				goodsList.clear();
			}
			goodsList.addAll(result);
			
			GoodsActivity.this.start += PageUtil.LIMIT;
			adapter.notifyDataSetChanged();
			goods_listview.stopRefreshData();
		}
	}
	
	//查询商品信息
	private void queryMerchByUser(int start){
		new queryGoodsTask().execute(start);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.CATEGORY_REQUEST_CODE:
			if(data != null){
				queryMerchByUser(PageUtil.START);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
