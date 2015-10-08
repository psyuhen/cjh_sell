package com.cjh.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cjh.adapter.CommonAdapter;
import com.cjh.adapter.ViewHolder;
import com.cjh.bean.AddImage;
import com.cjh.bean.Gallery;
import com.cjh.bean.MerchInfo;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.FileUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 商品预览
 * @author ps
 *
 */
public class GoodsViewActivity extends BaseTwoActivity {
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsViewActivity.class);

	private ListView goodsViewListView;
	private List<AddImage> lists;
	private CommonAdapter<AddImage> commonAdapter;
	
	private int merch_id;
	private TextView goods_view_title;//标题
	private ImageView goods_view_iv01;//图片1
	private TextView goods_view_content;//内容
	private TextView goods_view_price;//价格
//	private TextView goods_view_postage;//包邮
	private TextView goods_view_headtitle_text;//店铺名称
//	private ImageView goods_view_iv01;

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
		goods_view_title = (TextView)findViewById(R.id.goods_view_title);
		goods_view_iv01 = (ImageView)findViewById(R.id.goods_view_iv01);
		goods_view_content = (TextView)findViewById(R.id.goods_view_content);
		goods_view_price = (TextView)findViewById(R.id.goods_view_price);
//		goods_view_postage = (TextView)findViewById(R.id.goods_view_postage);
		goods_view_headtitle_text = (TextView)findViewById(R.id.goods_view_headtitle_text);
		
		goodsViewListView = (ListView) findViewById(R.id.goods_view_listview);
		lists = new ArrayList<AddImage>();
		commonAdapter = showAdapter();
		goodsViewListView.setAdapter(commonAdapter);
	}

	private void initData() {
		title.setText("预览");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setVisibility(View.VISIBLE);
		right_text.setText("刷新");
		
		goods_view_iv01.setImageBitmap(null);
		goods_view_iv01.setBackgroundDrawable(null);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		merch_id = extras.getInt("merch_id");
		
		queryGoods();
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
	
	private CommonAdapter<AddImage> showAdapter() {
		return new CommonAdapter<AddImage>(GoodsViewActivity.this, lists, R.layout.item_goods_view) {
			@Override
			public void convert(ViewHolder helper, AddImage item) {
				helper.setImageBitmap(R.id.item_goods_view_image, item.getBitmap());
			}
		};
	}
	
	//查询商品信息
	private void queryGoods(){
		String url = HttpUtil.BASE_URL + "/merch/querybyid.do?merch_id="+merch_id;
		
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "查询商品列表失败");
				return;
			}
			
			MerchInfo merchInfo = JsonUtil.parse2Object(json, MerchInfo.class);
			goods_view_title.setText(merchInfo.getName());
			goods_view_content.setText(merchInfo.getDesc());
			goods_view_price.setText(merchInfo.getPrice() + "");
			
			goods_view_headtitle_text.setText(sessionManager.getStoreName());
			//查询图片
			url = HttpUtil.BASE_URL + "/gallery/queryByMerchId.do?merch_id="+merch_id;
			json = HttpUtil.getRequest(url);
			if(json != null){
				List<Gallery> galleries = JsonUtil.parse2ListGallery(json);
				if(galleries != null && !galleries.isEmpty()){
					int length = galleries.size();
					for (int i = 0; i < length; i++) {
						Gallery gallery = galleries.get(i);
						String fileName = gallery.getFile_name();
						
						Bitmap photo = FileUtil.getCacheFile(fileName);
						if(i == 0){
							goods_view_iv01.setImageBitmap(photo);
						}else{
							lists.add(new AddImage(gallery.getGallery_id(), fileName, photo));
						}
					}
				}
			}
			commonAdapter.notifyDataSetChanged();
			setListViewHeight(goodsViewListView);
			
		} catch (Exception e) {
			CommonsUtil.showLongToast(getApplicationContext(), "查询商品信息失败");
			LOGGER.error(">>> 查询商品信息失败",e);
		}
	
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_text:
			lists.clear();
			queryGoods();
			break;
		}
	}
}
