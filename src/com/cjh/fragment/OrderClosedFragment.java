package com.cjh.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kymjs.aframe.ui.widget.KJListView;
import org.kymjs.aframe.ui.widget.KJListView.KJListViewListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjh.activity.MainActivity;
import com.cjh.activity.OrderActivity;
import com.cjh.activity.OrderDetailsActivity;
import com.cjh.activity.OrdersActivity;
import com.cjh.activity.SettingActivity;
import com.cjh.adapter.OrderItemAdapter;
import com.cjh.bean.MerchGallery;
import com.cjh.bean.Order;
import com.cjh.bean.OrderDetail;
import com.cjh.bean.OrderItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.FileUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.PageUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

public class OrderClosedFragment extends Fragment implements OnClickListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderClosedFragment.class);

	private KJListView kjListView;
	private OrderItemAdapter orderItemAdapter;
	private List<OrderItem> orderlist;
	private View view;
	private RelativeLayout order_top_rl;
	private RelativeLayout order_left_rl;
	private RelativeLayout order_right_rl;

	private View order_left_line;
	private View order_right_line;
	
	private int start = PageUtil.START;//分页的开始
	
	private Context context;
	public OrderClosedFragment(Context context) {
		this.context = context;
	}
	
	public OrderClosedFragment() {
	}

/*	public static OrderClosedFragment newInstance() {
		OrderClosedFragment fragment = new OrderClosedFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_completed_order,container, false);
		initView();
		kjListView = (KJListView) contentView.findViewById(R.id.completed_order_listview);
		orderlist = new ArrayList<OrderItem>();
		orderItemAdapter = new OrderItemAdapter(getActivity(), orderlist);
		kjListView.setAdapter(orderItemAdapter);
		kjListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				OrderItem item = (OrderItem)parent.getItemAtPosition(position);
				Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
				intent.putExtra("order_id", item.getSerialnum());
				startActivity(intent);
			}
		});
		kjListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(getActivity(), "删除", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
		
		//上下拉刷新
		kjListView.setPullLoadEnable(true);
		kjListView.setKJListViewListener(new KJListViewListener() {
			@Override
			public void onRefresh() {
				getClosedOrderInfo(PageUtil.START);
			}

			@Override
			public void onLoadMore() {
				getClosedOrderInfo(OrderClosedFragment.this.start);
			}
		});
		
		initData();
		return contentView;

	}

	private void initView() {
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.item_order_headview, null);
		order_top_rl = (RelativeLayout) view.findViewById(R.id.order_top_rl);
		order_top_rl.setOnClickListener(this);
		order_left_rl = (RelativeLayout) view.findViewById(R.id.order_left_rl);
		order_left_rl.setOnClickListener(this);
		order_right_rl = (RelativeLayout) view
				.findViewById(R.id.order_right_rl);
		order_right_rl.setOnClickListener(this);
		order_left_line = view.findViewById(R.id.order_left_line);
		order_right_line = view.findViewById(R.id.order_right_line);
	}

	public void initData() {
		getClosedOrderInfo(PageUtil.START);
	}
	
	//查询已完成的订单
	private void getClosedOrderInfo(int start){
		int user_id = 0;
		if(context instanceof OrderActivity){
			OrderActivity activity = (OrderActivity)context;
			user_id = activity.sessionManager.getUserId();
		}else if(context instanceof MainActivity){
			MainActivity activity = (MainActivity)context;
			user_id = activity.sessionManager.getUserId();
		}else if(context instanceof OrdersActivity){
			OrdersActivity activity = (OrdersActivity)context;
			user_id = activity.sessionManager.getUserId();
		}
		
		String url = HttpUtil.BASE_URL + "/order/getClosedOrderInfoPage.do?start="+start+"&limit="+PageUtil.LIMIT;
		Order order1 = new Order();
		order1.setSeller_user_id(user_id);
		
		try {
			String listJson = HttpUtil.postRequest(url, order1);
			if(listJson == null){
				CommonsUtil.showLongToast(getActivity(), "查询订单列表失败");
				kjListView.stopRefreshData();
				return;
			}
			
			List<Order> list = JsonUtil.parse2ListOrder(listJson);
			
			if(list == null){
				LOGGER.warn("转换订单列表信息失败");
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
				orderlist.clear();
			}
			
			for (int i = 0; i < length; i++) {
				Order order = list.get(i);
				List<OrderDetail> orderDetails = order.getOrderDetails();
				
				OrderItem orderItem = new OrderItem();
				orderItem.setId(i);
				orderItem.setBuyer_user_id(order.getBuyer_user_id()+"");
				orderItem.setSeller_user_id(order.getSeller_user_id()+"");
				orderItem.setOrdertime(new Date());
				orderItem.setSerialnum(order.getOrder_id());
//					orderItem.setAddress("aaaa");
				orderItem.setBuyer(order.getBuyer_user_name());
				orderItem.setOrdertime(new Date());
				orderItem.setPrice(order.getAmount_money());
				orderItem.setBuyer_user_mobile(order.getBuyer_phone());
				
				String status = order.getStatus();
				if(status != null && !"".equals(status)){
					char[] statusChars = status.toCharArray();
					orderItem.setStatus(statusChars[0]);
				}
				
				if(orderDetails != null && !orderDetails.isEmpty()){
					OrderDetail orderDetail = orderDetails.get(0);
					
					orderItem.setNum(orderDetail.getAmount());//默认显示第一个商品的数量
					orderItem.setGoodtitle(orderDetail.getMerch_name());//默认为第一个商品的名称
					//默认显示第一个商品的图片
					List<MerchGallery> merchGallerys = orderDetail.getMerchGallerys();
					if(merchGallerys != null && !merchGallerys.isEmpty()){
						int gallerySize = merchGallerys.size();
						List<Bitmap> bitmapList = new ArrayList<Bitmap>();
						for (int j = 0; j < gallerySize; j++) {
							Bitmap cacheFile = FileUtil.getCacheFile(merchGallerys.get(j).getName());
							if(cacheFile == null){
								continue;
							}
							bitmapList.add(cacheFile);
						}
						orderItem.setBitmapList(bitmapList);
					}
				}
				
				orderlist.add(orderItem);
			}
			
			this.start += PageUtil.LIMIT;//每次改变start的值 
			orderItemAdapter.notifyDataSetChanged();
			kjListView.stopRefreshData();
		} catch (Exception e) {
			LOGGER.error("查询订单列表失败", e);
			CommonsUtil.showLongToast(getActivity(), "查询订单列表失败");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_left_rl:
			order_left_line.setVisibility(View.VISIBLE);
			order_right_line.setVisibility(View.INVISIBLE);
			break;
		case R.id.order_right_rl:
			order_left_line.setVisibility(View.INVISIBLE);
			order_right_line.setVisibility(View.VISIBLE);
			break;
		case R.id.order_top_rl:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
		}
	}
}
