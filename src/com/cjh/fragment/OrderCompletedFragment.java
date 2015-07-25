package com.cjh.fragment;

import java.util.ArrayList;
import java.util.Date;
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

import com.cjh.activity.OrderActivity;
import com.cjh.activity.OrderDetailsActivity;
import com.cjh.adapter.OrderItemAdapter;
import com.cjh.bean.Order;
import com.cjh.bean.OrderItem;
import com.cjh.bean.User;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.auth.SessionManager;
/**
 * 已完成订单
 * @author ps
 *
 */
public class OrderCompletedFragment extends Fragment {
	public static final String TAG = "OrderCompletedFragment";
	private KJListView kjListView;
	private OrderItemAdapter orderItemAdapter;
	private List<OrderItem> orderlist;
	
	private Context context;
	public OrderCompletedFragment(Context context) {
		this.context = context;
	}

	/*public static OrderCompletedFragment newInstance() {
		OrderCompletedFragment fragment = new OrderCompletedFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_completed_order,
				container, false);
		initData();
		kjListView = (KJListView) contentView
				.findViewById(R.id.completed_order_listview);
		orderItemAdapter = new OrderItemAdapter(getActivity(), orderlist);
		kjListView.setAdapter(orderItemAdapter);
		kjListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
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
		return contentView;
	}

	private void initData() {
		orderlist = new ArrayList<OrderItem>();
		List<Order> orderList = getCompletedOrderInfo();
		for (int i = 0; i < orderList.size(); i++) {
			Order order = orderList.get(i);
			
			OrderItem orderItem = new OrderItem();
			orderItem.setId(i);
			orderItem.setBuyer_user_id(order.getBuyer_user_id()+"");
			orderItem.setSeller_user_id(order.getSeller_user_id()+"");
			orderItem.setOrdertime(new Date());
			orderItem.setSerialnum(order.getOrder_id());
//			orderItem.setAddress("aaaa");
			orderItem.setGoodtitle(order.getOrderDetails().get(0).getMerch_name());
			orderItem.setBuyer(order.getBuyer_user_name());
			orderItem.setNum(order.getOrderDetails().get(0).getAmount());
			orderItem.setOrdertime(new Date());
			orderItem.setPrice(order.getAmount_money());
			orderlist.add(orderItem);
		}
	}
	
	private List<Order> getCompletedOrderInfo(){
		OrderActivity activity = (OrderActivity)context;
		SessionManager sessionManager = activity.sessionManager;
		User user = sessionManager.getUserDetails();
		
		
		String url = HttpUtil.BASE_URL + "/order/getCompletedOrderInfo.do";
		Order order = new Order();
		order.setSeller_user_id(user.getUser_id());
		
		try {
			String listJson = HttpUtil.postRequest(url, order);
			if(listJson == null){
				CommonsUtil.showLongToast(getActivity(), "查询订单列表失败");
				return null;
			}
			
			List<Order> list = JsonUtil.parse2ListOrder(listJson);
			return list;
		} catch (InterruptedException e) {
			Log.e(TAG, "查询订单列表失败", e);
			CommonsUtil.showLongToast(getActivity(), "查询订单列表失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "查询订单列表失败", e);
			CommonsUtil.showLongToast(getActivity(), "查询订单列表失败");
		}
		
		return null;
	}
}
