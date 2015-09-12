package com.cjh.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.kymjs.aframe.ui.widget.KJListView;
import org.kymjs.aframe.ui.widget.KJListView.KJListViewListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.cjh.activity.GoodActivity;
import com.cjh.activity.GoodsAddActivity;
import com.cjh.activity.GoodsDetailsActivity;
import com.cjh.adapter.GoodsOnofferAdapter;
import com.cjh.auth.SessionManager;
import com.cjh.bean.GoodsItem;
import com.cjh.bean.MerchInfo;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.PageUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 
 * 商品列表 
 * @author ps
 *
 */
public class GoodsOnOfferFragment extends Fragment {
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsOnOfferFragment.class);

	
	private KJListView kjListView;
	private List<GoodsItem> goodsList;
	private GoodsOnofferAdapter goodsOnofferAdapter;
	private int start = PageUtil.START;//分页的开始
	
	private Context context;
	
	public GoodsOnOfferFragment(Context context) {
		this.context = context;
	}
	
	
	/*public static GoodsOnOfferFragment newInstance() {
		GoodsOnOfferFragment fragment = new GoodsOnOfferFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.view_goods_onoffer,container, false);
		
		//三个排序
		CheckBox goods_add_time = (CheckBox)contentView.findViewById(R.id.goods_add_time);
		goods_add_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sort(R.id.goods_add_time, isChecked);
			}
		});
		CheckBox goods_sale_num = (CheckBox)contentView.findViewById(R.id.goods_sale_num);
		goods_sale_num.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sort(R.id.goods_sale_num, isChecked);
			}
		});
		CheckBox goods_stock = (CheckBox)contentView.findViewById(R.id.goods_stock);
		goods_stock.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sort(R.id.goods_stock, isChecked);
			}
		});
		
		//添加商品
		RelativeLayout goods_add_rl = (RelativeLayout)contentView.findViewById(R.id.goods_add_rl);
		goods_add_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, GoodsAddActivity.class));
			}
		});
		
		kjListView = (KJListView) contentView.findViewById(R.id.onoffer_goods_listview);
		
		goodsList = new ArrayList<GoodsItem>();
		goodsOnofferAdapter=new GoodsOnofferAdapter(goodsList, getActivity());
		kjListView.setAdapter(goodsOnofferAdapter);
		kjListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long len) {
				
				GoodsItem item = (GoodsItem)parent.getItemAtPosition(position);
				Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
				intent.putExtra("merch_id", item.getId());
				startActivity(intent);
			}
		});
		
		//上下拉刷新
		kjListView.setPullLoadEnable(true);
		kjListView.setKJListViewListener(new KJListViewListener() {
			@Override
			public void onRefresh() {
				querybyuserid(PageUtil.START);
			}

			@Override
			public void onLoadMore() {
				querybyuserid(GoodsOnOfferFragment.this.start);
			}
		});
		
		initData();
		return contentView;
	}

	private void initData() {
		querybyuserid(PageUtil.START);
	}
	//排序
	private void sort(int type,final boolean isChecked){
		switch(type){
		case R.id.goods_add_time://添加时间
			Collections.sort(goodsList, new Comparator<GoodsItem>() {
				@Override
				public int compare(GoodsItem gi1, GoodsItem gi2) {
					String ct1 = gi1.getCreate_time();
					String ct2 = gi2.getCreate_time();
					
					return isChecked ? ct2.compareTo(ct1) : ct1.compareTo(ct2);
				}
			});
			goodsOnofferAdapter.notifyDataSetChanged();
			break;
		case R.id.goods_sale_num://销量
			Collections.sort(goodsList, new Comparator<GoodsItem>() {
				@Override
				public int compare(GoodsItem gi1, GoodsItem gi2) {
					int sm1 = gi1.getSellmount();
					int sm2 = gi2.getSellmount();
					
					if(sm2 > sm1){
						return isChecked ? 1 : -1;
					}else if(sm2 < sm1){
						return isChecked ? -1 : 1;
					}
					
					return 0;
				}
			});
			goodsOnofferAdapter.notifyDataSetChanged();
			break;
		case R.id.goods_stock://库存
			Collections.sort(goodsList, new Comparator<GoodsItem>() {
				@Override
				public int compare(GoodsItem gi1, GoodsItem gi2) {
					int st1 = gi1.getStock();
					int st2 = gi2.getStock();
					

					if(st2 > st1){
						return isChecked ? 1 : -1;
					}else if(st2 < st1){
						return isChecked ? -1 : 1;
					}
					
					return 0;
				}
			});
			goodsOnofferAdapter.notifyDataSetChanged();
			break;
		}
	}
	
	//根据用户查询商品信息
	private void querybyuserid(int start){
		GoodActivity activity = (GoodActivity)context;
		SessionManager sessionManager = activity.sessionManager;
		int user_id = sessionManager.getInt(SessionManager.KEY_USER_ID);
		
		MerchInfo info = new MerchInfo();
		info.setUser_id(user_id);
		info.setOut_published("0");
		
		String url = HttpUtil.BASE_URL + "/merch/querybypage.do?start="+start+"&limit="+PageUtil.LIMIT;
		
		try {
			String listJson = HttpUtil.postRequest(url,info);
			if(listJson == null){
				CommonsUtil.showLongToast(getActivity(), "查询商品列表失败");
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
				goodsItem.setImg("image");
				goodsItem.setPrice(merchInfo.getPrice());
				goodsItem.setSellmount(merchInfo.getSales_volume());
				goodsItem.setStandard(merchInfo.getUnit());
				goodsItem.setStock(merchInfo.getIn_stock());
				goodsItem.setTitle(merchInfo.getName());
				goodsItem.setCreate_time(merchInfo.getCreate_time());
				goodsList.add(goodsItem);
			}
			
			this.start += PageUtil.LIMIT;//每次改变start的值 
			goodsOnofferAdapter.notifyDataSetChanged();
			kjListView.stopRefreshData();
		} catch (InterruptedException e) {
			LOGGER.error("查询商品列表失败", e);
			CommonsUtil.showLongToast(getActivity(), "查询商品列表失败");
		} catch (ExecutionException e) {
			LOGGER.error("查询商品列表失败", e);
			CommonsUtil.showLongToast(getActivity(), "查询商品列表失败");
		}
		
	}
}
