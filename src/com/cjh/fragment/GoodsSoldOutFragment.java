package com.cjh.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.kymjs.aframe.ui.widget.KJListView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.cjh.activity.GoodActivity;
import com.cjh.activity.GoodsDetailsActivity;
import com.cjh.adapter.GoodsOnofferAdapter;
import com.cjh.auth.SessionManager;
import com.cjh.bean.GoodsItem;
import com.cjh.bean.MerchInfo;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

public class GoodsSoldOutFragment extends Fragment {
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsSoldOutFragment.class);

	
	private KJListView kjListView;
	private List<GoodsItem> goodsList;
	private GoodsOnofferAdapter goodsOnsoldoutAdapter;
	
	private Context context;
	public GoodsSoldOutFragment(Context context) {
		this.context = context;
	}

	/*public static GoodsSoldOutFragment newInstance() {
		GoodsSoldOutFragment fragment = new GoodsSoldOutFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_goods_soldout,
				container, false);
		initData();
		kjListView = (KJListView) contentView
				.findViewById(R.id.soldout_goods_listview);
		goodsOnsoldoutAdapter=new GoodsOnofferAdapter(goodsList, getActivity());
		kjListView.setAdapter(goodsOnsoldoutAdapter);
		kjListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long len) {
				Toast.makeText(getActivity(), "删除商品", Toast.LENGTH_LONG).show();
				return false;
			}
		});
		kjListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long len) {
				GoodsOnofferAdapter adapter = (GoodsOnofferAdapter)kjListView.getAdapter();
				GoodsItem item = adapter.getItem(position);
				Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
				intent.putExtra("merch_id", item.getId());
				startActivity(intent);
			}
		});
		
		return contentView;
	}
	private void initData() {
		goodsList = new ArrayList<GoodsItem>();
		List<MerchInfo> infos = querybyuserid();
		
		for (int i = 0; i < infos.size(); i++) {
			MerchInfo merchInfo = infos.get(i);
			GoodsItem goodsItem = new GoodsItem();
			goodsItem.setId(merchInfo.getMerch_id());
			goodsItem.setImg("image");
			goodsItem.setPrice(merchInfo.getPrice());
			goodsItem.setSellmount(merchInfo.getSales_volume());
			goodsItem.setStandard(merchInfo.getUnit());
			goodsItem.setStock(merchInfo.getIn_stock());
			goodsItem.setTitle(merchInfo.getName());
			goodsList.add(goodsItem);
		}
	}
	
	private List<MerchInfo> querybyuserid(){
		GoodActivity activity = (GoodActivity)context;
		SessionManager sessionManager = activity.sessionManager;
		int user_id = sessionManager.getInt(SessionManager.KEY_USER_ID);
		
		String url = HttpUtil.BASE_URL + "/merch/querybyuserid.do?user_id="+user_id;
		
		try {
			String listJson = HttpUtil.getRequest(url);
			if(listJson == null){
				CommonsUtil.showLongToast(getActivity(), "查询商品列表失败");
				return null;
			}
			
			List<MerchInfo> list = JsonUtil.parse2ListMerchInfo(listJson);
			return list;
		} catch (InterruptedException e) {
			LOGGER.error("查询商品列表失败", e);
			CommonsUtil.showLongToast(getActivity(), "查询商品列表失败");
		} catch (ExecutionException e) {
			LOGGER.error("查询商品列表失败", e);
			CommonsUtil.showLongToast(getActivity(), "查询商品列表失败");
		}
		
		return null;
	}
}
