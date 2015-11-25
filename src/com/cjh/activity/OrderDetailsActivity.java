package com.cjh.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cjh.bean.Order;
import com.cjh.bean.OrderDetail;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 订单详情
 * @author ps
 *
 */
public class OrderDetailsActivity extends BaseTwoActivity {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsActivity.class);
	private TextView order_details_serial;
	private TextView order_details_time;
	private TextView order_details_payway;
	private TextView order_details_status;
	private TextView order_details_person;
	private TextView order_details_tel;
	private TextView order_details_address;
	private TextView order_details_deliverytime;
	private TextView order_details_bill;
	private TextView order_details_allmoney;
	private TextView order_details_freight;
	private ListView order_details_goodslist;
	private TextView order_details_shoulpay;
	private List<Map<String, Object>> maps;
	
	private LinearLayout order_details_ll5;
	private Button order_details_complete;
	private Button order_details_close;

	private String order_id;
	private String from;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderdetails);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		order_details_serial = (TextView) findViewById(R.id.order_details_serial);
		order_details_time = (TextView) findViewById(R.id.order_details_time);
		order_details_payway = (TextView) findViewById(R.id.order_details_payway);
		order_details_status = (TextView) findViewById(R.id.order_details_status);
		order_details_person = (TextView) findViewById(R.id.order_details_person);
		order_details_tel = (TextView) findViewById(R.id.order_details_tel);
		order_details_address = (TextView) findViewById(R.id.order_details_address);
		order_details_deliverytime = (TextView) findViewById(R.id.order_details_deliverytime);
		order_details_bill = (TextView) findViewById(R.id.order_details_bill);
		order_details_allmoney = (TextView) findViewById(R.id.order_details_allmoney);
		order_details_freight = (TextView) findViewById(R.id.order_details_freight);
		order_details_goodslist = (ListView) findViewById(R.id.order_details_goodslist);
		order_details_shoulpay=(TextView) findViewById(R.id.order_details_shoulpay);
		order_details_complete=(Button) findViewById(R.id.order_details_complete);
		order_details_complete.setOnClickListener(this);
		order_details_close=(Button) findViewById(R.id.order_details_close);
		order_details_close.setOnClickListener(this);
		order_details_ll5=(LinearLayout) findViewById(R.id.order_details_ll5);
		
	}

	private void initData() {
		title.setText("订单详情");
		Intent intent = getIntent();
		order_id = intent.getStringExtra("order_id");
		from = intent.getStringExtra("from");
		
		
		if("OrderInFragment".equals(from)){
			order_details_ll5.setVisibility(View.VISIBLE);
		}else{
			order_details_ll5.setVisibility(View.GONE);
		}
		
		new queryOrderByIdTask().execute();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.order_details_complete:
			new updateOrderStatusTask().execute("4", "完成");
			break;
		case R.id.order_details_close:
			new updateOrderStatusTask().execute("6", "关闭");
			break;
		default:
			break;
		}
	}
	//更新订单状态
	private class updateOrderStatusTask extends AsyncTask<String, Void, String>{
		private String msg = "";
		@Override
		protected String doInBackground(String... params) {
			String status = params[0];
			msg = params[1];
			
			return updateBuyerScore(status, msg);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if("更新订单成功!".equals(result)){
				CommonsUtil.showLongToast(getApplicationContext(), "订单" + msg + "成功！");
				OrderDetailsActivity.this.finish();
			}else{
				CommonsUtil.showLongToast(getApplicationContext(), "订单" + msg + "失败！");
			}
			
		}
	}
	
	//更新评分信息
	public String updateBuyerScore(String status, String msg){
		String url = HttpUtil.BASE_URL + "/order/updateOrderStatus.do";
		try {
			
			Order order = new Order();
			order.setOrder_id(order_id);
			order.setStatus(status);
			
			String json = HttpUtil.postRequest(url,order);
			return json;
		} catch (Exception e) {
			LOGGER.error("更新状态失败", e);
		}
		return null;
	}
	
	
	//查询订单
	private Order queryOrderById(){
		String url = HttpUtil.BASE_URL + "/order/getOrderInfoById.do?orderId="+order_id;
		
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "查询订单信息失败");
				return null;
			}
			
			Order order = JsonUtil.parse2Object(json, Order.class);
			return order;
		} catch (Exception e) {
			LOGGER.error(">>> 查询订单信息失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询订单信息失败");
		}
		
		return null;
	}
	
	private void setOrderValue(Order order){
		order_details_serial.setText(order_id);
		order_details_time.setText(order.getTrad_time());
		String pay_type = order.getPay_type();
		if("0".equals(pay_type)){//支付宝
			order_details_payway.setText("支付宝支付");
		}else if("1".equals(pay_type)){//微信支付
			order_details_payway.setText("微信支付");
		}else if("2".equals(pay_type)){//现金交易
			order_details_payway.setText("现金交易");
		}
		String status = order.getStatus();
		if("0".equals(status)){
			order_details_status.setText("买家下单");
		} else if("1".equals(status)){
			order_details_status.setText("卖方确认订单");
		} else if("2".equals(status)){
			order_details_status.setText("买方付款");
		} else if("3".equals(status)){
			order_details_status.setText("卖方发货");
		} else if("4".equals(status)){
			order_details_status.setText("订单完成/买方确认收货");
		} else if("5".equals(status)){
			order_details_status.setText("订单取消");
		} else if("6".equals(status)){
			order_details_status.setText("订单关闭");
		}
		
		order_details_person.setText(order.getBuyer_name());
		order_details_tel.setText(order.getBuyer_phone());
		order_details_address.setText(order.getAddress());
		order_details_deliverytime.setText(order.getSend_time());
		order_details_bill.setText(order.getInvoice_title());
		order_details_allmoney.setText("￥"+order.getAmount_money());
		order_details_freight.setText("￥"+order.getFreight());//运费
		
		//商品列表
		maps = new ArrayList<Map<String, Object>>();
		List<OrderDetail> details = order.getOrderDetails();
		for (int i = 0; i < details.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			OrderDetail detail = details.get(i);
			
			map.put("title", detail.getMerch_name());
			map.put("image", R.drawable.login_head_icon);
			map.put("num", detail.getAmount());
			map.put("money", detail.getPrice());
			maps.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(
				OrderDetailsActivity.this, maps, R.layout.item_order_details,
				new String[] { "image", "title", "num", "money" }, new int[] {
						R.id.item_order_details_image,
						R.id.item_order_details_title, R.id.order_details_num,
						R.id.order_details_money });
		order_details_goodslist.setAdapter(simpleAdapter);
		order_details_shoulpay.setText("￥"+(order.getAmount_money()+order.getFreight()));//应付金额
		right_imgbtn.setVisibility(View.GONE);
	}
	
	//根据ID查询订单信息
	private class queryOrderByIdTask extends AsyncTask<Void, Void, Order>{
		@Override
		protected Order doInBackground(Void... params) {
			return queryOrderById();
		}
		
		@Override
		protected void onPostExecute(Order result) {
			super.onPostExecute(result);
			setOrderValue(result);
		}
	}
}
