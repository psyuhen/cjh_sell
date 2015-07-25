package com.cjh.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.kymjs.aframe.ui.widget.KJListView;

import com.cjh.adapter.OrderSourceAdapter;
import com.cjh.bean.OrderSourceItem;
import com.cjh.bean.OrderStat;
import com.cjh.cjh_sell.R;
import com.cjh.common.LineView;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.DateUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
/**
 * 订单成交
 * @author ps
 *
 */
public class OrderSourceActivity extends BaseTwoActivity {
	public static final String TAG = "OrderSourceActivity";
	private KJListView kjListView;
	private OrderSourceAdapter adpater;
	private List<OrderSourceItem> list;
	private LineView order_completed_lineview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_source);
		initView();
		initData();
		adpater = new OrderSourceAdapter(OrderSourceActivity.this, list);
		kjListView.setAdapter(adpater);
		fillData(order_completed_lineview);
	}

	@Override
	public void initView() {
		super.initView();
		kjListView = (KJListView) findViewById(R.id.market_source_listview);
		list = new ArrayList<OrderSourceItem>();
		order_completed_lineview = (LineView) findViewById(R.id.order_completed_lineview);
		kjListView.setPullRefreshEnable(true);
		kjListView.setPullLoadEnable(true);
		kjListView.setLongClickable(true);
		kjListView.setSelected(true);
		right_imgbtn.setVisibility(View.GONE);
	}

	private void initData() {
		title.setText("成交订单");
		for (int i = 0; i < 10; i++) {
			OrderSourceItem orderSourceItem = new OrderSourceItem();
			orderSourceItem.setDate(new Date());
			orderSourceItem.setPocketcount(i);
			orderSourceItem.setOthercount(i * 3);
			orderSourceItem.setAllcount(i + i * 3);
			list.add(orderSourceItem);
		}
	}

	public void fillData(LineView lineView) {
		ArrayList<String> bottomlist = new ArrayList<String>();
		String yearMonth = DateUtil.today("yyyyMM");
		int days = DateUtil.getDayOfMonth(yearMonth);
		String month = yearMonth.substring(4, 6);
		for (int i = 1; i <= days; i++) {
			bottomlist.add(month +"-"+ ((i < 10) ? ("0"+i) : (i+"")));
		}
		lineView.setBottomTextList(bottomlist);
		
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		List<OrderStat> list = countByMonth(yearMonth);
		if(list == null){
			list = new ArrayList<OrderStat>();
		}
		
		for (int i = 1; i <= days; i++) {
			String index = (i < 10) ? "0"+i : i+"";
			boolean isFound = false;
			for (OrderStat v : list) {
				String hour = v.getStat_date();
				if(index.equals(hour)){
					isFound = true;
					dataslist.add(v.getOrder_sell());
					break;
				}
			}
			if(!isFound){
				dataslist.add(0);
			}
		}
		lineView.setDataList(dataslist);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	
	private List<OrderStat> countByMonth(String statDate){
		String url = HttpUtil.BASE_URL + "/order/countByMonth.do?stat_date="+statDate;
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
				return null;
			}
			
			List<OrderStat> list = JsonUtil.parse2ListOrderStat(json);
			return list;
		} catch (InterruptedException e) {
			Log.e(TAG, "统计失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "统计失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
		}
		return null;
	}

}
