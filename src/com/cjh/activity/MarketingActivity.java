package com.cjh.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cjh.bean.FavoriteStat;
import com.cjh.bean.OrderStat;
import com.cjh.bean.Store;
import com.cjh.bean.VisitStat;
import com.cjh.cjh_sell.R;
import com.cjh.common.LineView;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.DateUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;

public class MarketingActivity extends BaseTwoActivity implements
		OnClickListener {
	public static final String TAG = "MarketingActivity";
	private Button order_completed_source_btn;
	
	//成交订单的今日与昨日
	private TextView market_today_count;
	private TextView market_yesterday_count;
	//今日金额的今日与昨日
	private TextView market_money_today_count;
	private TextView market_money_yesterday_count;
	//今日顾客的今日与昨日
	private TextView market_customer_today_count;
	private TextView market_customer_yesterday_count;
	//今日收藏的今日与昨日
	private TextView market_everyday_collect_today_count;
	private TextView market_everyday_collect_yesterday_count;
	
	//
	private LineView lineView;
	private LineView today_mnoney_lineview;
	private LineView today_customer_lineview;
	private LineView everyday_collect_lineview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marketing);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		order_completed_source_btn = (Button) findViewById(R.id.order_completed_source_btn);
		order_completed_source_btn.setOnClickListener(this);
		right_imgbtn.setVisibility(View.GONE);
		
		market_today_count = (TextView)findViewById(R.id.market_today_count);
		market_yesterday_count = (TextView)findViewById(R.id.market_yesterday_count);
		market_money_today_count = (TextView)findViewById(R.id.market_money_today_count);
		market_money_yesterday_count = (TextView)findViewById(R.id.market_money_yesterday_count);
		market_customer_today_count = (TextView)findViewById(R.id.market_customer_today_count);
		market_customer_yesterday_count = (TextView)findViewById(R.id.market_customer_yesterday_count);
		market_everyday_collect_today_count = (TextView)findViewById(R.id.market_everyday_collect_today_count);
		market_everyday_collect_yesterday_count = (TextView)findViewById(R.id.market_everyday_collect_yesterday_count);
	
		zouni();
	}

	private void initData() {
		title.setText("营销中心");

		//成交订单
		String today = DateUtil.today();
		String yesterday = DateUtil.yesterday();
		
		OrderStat todayOrder = countByDay(today);
		OrderStat yesterdayOrder = countByDay(yesterday);
		market_today_count.setText((todayOrder != null ? todayOrder.getOrder_sell() : 0)+"");
		market_yesterday_count.setText((yesterdayOrder != null ? yesterdayOrder.getOrder_sell() : 0)+"");
		
		List<OrderStat> sellOrder = countByDayHour(today);
		if(sellOrder == null){
			sellOrder = new ArrayList<OrderStat>();
		}
		fillSellData(sellOrder);
		
		//成交金额
		OrderStat todayMoney = countMoneyByDay(today);
		OrderStat yesterdayMoney = countMoneyByDay(yesterday);
		
		market_money_today_count.setText((todayMoney != null ? todayMoney.getAmount_money() : 0)+"");
		market_money_yesterday_count.setText((yesterdayMoney != null ? yesterdayMoney.getAmount_money() : 0)+"");
		
		List<OrderStat> sellMoney = countMoneyByDayHour(today);
		if(sellMoney == null){
			sellMoney = new ArrayList<OrderStat>();
		}
		fillMoneyData(sellMoney);
		
		//每日收藏
		FavoriteStat favoriteStat = new FavoriteStat();
		String store_id = sessionManager.get("store_id");
		if(store_id == null || "".equals(store_id)){
			String user_id = sessionManager.get("user_id");
			Store store = queryByUserId(user_id);
			store_id = String.valueOf(store.getStore_id());
			sessionManager.put("store_id", store_id);
		}
		favoriteStat.setStore_id(Integer.parseInt(store_id));//商家ID
		
		favoriteStat.setStat_date(today);
		FavoriteStat todayFavorite = countByDayAndUser(favoriteStat);
		favoriteStat.setStat_date(yesterday);
		FavoriteStat yesterdayFavorite = countByDayAndUser(favoriteStat);
		
		market_everyday_collect_today_count.setText((todayFavorite != null ? todayFavorite.getFavorite_count() : 0)+"");
		market_everyday_collect_yesterday_count.setText((yesterdayFavorite != null ? yesterdayFavorite.getFavorite_count() : 0)+"");
		
		favoriteStat.setStat_date(today);
		List<FavoriteStat> favorite = countByDayHourAndUser(favoriteStat);
		if(favorite == null){
			favorite = new ArrayList<FavoriteStat>();
		}
		fillFavoriteData(favorite);
		
		//每日顾客
		VisitStat visitStat = new VisitStat();
		visitStat.setStore_id(Integer.parseInt(store_id));//商家ID
		
		visitStat.setStat_date(today);
		VisitStat todayVisit = countByDayAndUser(visitStat);
		visitStat.setStat_date(yesterday);
		VisitStat yesterdayVisit = countByDayAndUser(visitStat);
		
		market_customer_today_count.setText((todayVisit != null ? todayVisit.getVisit_count() : 0)+"");
		market_customer_yesterday_count.setText((yesterdayVisit != null ? yesterdayVisit.getVisit_count() : 0)+"");
		
		visitStat.setStat_date(today);
		List<VisitStat> visit = countByDayHourAndUser(visitStat);
		if(visit == null){
			visit = new ArrayList<VisitStat>();
		}
		fillVisitData(visit);
		
	}

	@SuppressLint("ResourceAsColor")
	public void zouni() {
		lineView = (LineView) findViewById(R.id.line_view);
		today_mnoney_lineview = (LineView) findViewById(R.id.today_mnoney_lineview);
		today_customer_lineview = (LineView) findViewById(R.id.today_customer_lineview);
		everyday_collect_lineview = (LineView) findViewById(R.id.everyday_collect_lineview);
		
		fillBom(lineView);
		fillBom(today_mnoney_lineview);
		fillBom(today_customer_lineview);
		fillBom(everyday_collect_lineview);
	}

	public void fillBom(LineView lineView) {
		ArrayList<String> bottomlist = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			bottomlist.add((i < 10 ? "0"+i : i) + ":00");
		}
		lineView.setBottomTextList(bottomlist);
	}
	
	public void fillSellData(List<OrderStat> sell){
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			String index = (i < 10) ? "0"+i : i+"";
			boolean isFound = false;
			for (OrderStat v : sell) {
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
		everyday_collect_lineview.setDataList(dataslist);
	}
	public void fillMoneyData(List<OrderStat> money){
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			String index = (i < 10) ? "0"+i : i+"";
			boolean isFound = false;
			for (OrderStat v : money) {
				String hour = v.getStat_date();
				if(index.equals(hour)){
					isFound = true;
					dataslist.add((int)v.getAmount_money());
					break;
				}
			}
			if(!isFound){
				dataslist.add(0);
			}
		}
		everyday_collect_lineview.setDataList(dataslist);
	}
	public void fillFavoriteData(List<FavoriteStat> favorite){
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			String index = (i < 10) ? "0"+i : i+"";
			boolean isFound = false;
			for (FavoriteStat v : favorite) {
				String hour = v.getStat_date();
				if(index.equals(hour)){
					isFound = true;
					dataslist.add(v.getFavorite_count());
					break;
				}
			}
			if(!isFound){
				dataslist.add(0);
			}
		}
		everyday_collect_lineview.setDataList(dataslist);
	}
	
	public void fillVisitData(List<VisitStat> visit){
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			String index = (i < 10) ? "0"+i : i+"";
			boolean isFound = false;
			for (VisitStat v : visit) {
				String hour = v.getStat_date();
				if(index.equals(hour)){
					isFound = true;
					dataslist.add(v.getVisit_count());
					break;
				}
			}
			if(!isFound){
				dataslist.add(0);
			}
		}
		today_customer_lineview.setDataList(dataslist);
	}
	

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.order_completed_source_btn:
			startActivity(new Intent(MarketingActivity.this,
					OrderSourceActivity.class));
			break;

		default:
			break;
		}
	}

	private OrderStat countOrder(String url,Map<String,String> map){
		try {
			String json = HttpUtil.postRequest(url,map);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
				return null;
			}
			
			OrderStat orderStat = JsonUtil.parse2Object(json, OrderStat.class);
			return orderStat;
		} catch (InterruptedException e) {
			Log.e(TAG, "统计失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "统计失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
		}
		return null;
	}
	private List<OrderStat> countListOrder(String url, Map<String,String> map){
		try {
			String json = HttpUtil.postRequest(url,map);
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
	
	private OrderStat countByDay(String statDate){
		String url = HttpUtil.BASE_URL + "/order/countByDay.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("stat_date", statDate);
		map.put("user_id", sessionManager.getInt("user_id")+"");
		return countOrder(url,map);
		
	}
	private List<OrderStat> countByDayHour(String statDate){
		String url = HttpUtil.BASE_URL + "/order/countByDayHour.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("stat_date", statDate);
		map.put("user_id", sessionManager.getInt("user_id")+"");
		return countListOrder(url,map);
	}
	private OrderStat countMoneyByDay(String statDate){
		String url = HttpUtil.BASE_URL + "/order/countMoneyByDay.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("stat_date", statDate);
		map.put("user_id", sessionManager.getInt("user_id")+"");
		return countOrder(url,map);
		
	}
	private List<OrderStat> countMoneyByDayHour(String statDate){
		String url = HttpUtil.BASE_URL + "/order/countMoneyByDayHour.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("stat_date", statDate);
		map.put("user_id", sessionManager.getInt("user_id")+"");
		return countListOrder(url,map);
	}
	
	private FavoriteStat countByDayAndUser(FavoriteStat favoriteStat){
		String url = HttpUtil.BASE_URL + "/storeuf/countByDayAndUser.do";
		return countOrder(url,favoriteStat);
		
	}
	private List<FavoriteStat> countByDayHourAndUser(FavoriteStat favoriteStat){
		String url = HttpUtil.BASE_URL + "/storeuf/countByDayHourAndUser.do";
		return countList(url,favoriteStat);
	}
	
	private List<FavoriteStat> countList(String url,FavoriteStat favoriteStat){
		try {
			String json = HttpUtil.postRequest(url,favoriteStat);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
				return null;
			}
			
			List<FavoriteStat> list = JsonUtil.parse2ListFavoriteStat(json);
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
	
	private FavoriteStat countOrder(String url, FavoriteStat favoriteStat){
		try {
			String json = HttpUtil.postRequest(url,favoriteStat);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
				return null;
			}
			
			FavoriteStat fStat = JsonUtil.parse2Object(json, FavoriteStat.class);
			return fStat;
		} catch (InterruptedException e) {
			Log.e(TAG, "统计失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "统计失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
		}
		return null;
	}
	
	private Store queryByUserId(String user_id){
		String url = HttpUtil.BASE_URL + "/store/querybyuser.do?user_id="+user_id;
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "查询商家信息失败");
				return null;
			}
			
			Store store = JsonUtil.parse2Object(json, Store.class);
			return store;
		} catch (InterruptedException e) {
			Log.e(TAG, "查询商家信息失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询商家信息失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "查询商家信息失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询商家信息失败");
		}
		return null;
	}
	
	private VisitStat countByDayAndUser(VisitStat visitStat){
		String url = HttpUtil.BASE_URL + "/storevisit/countByDayAndUser.do";
		return countOrder(url,visitStat);
		
	}
	private List<VisitStat> countByDayHourAndUser(VisitStat visitStat){
		String url = HttpUtil.BASE_URL + "/storevisit/countByDayHourAndUser.do";
		return countList(url,visitStat);
	}
	
	private VisitStat countOrder(String url, VisitStat visitStat){
		try {
			String json = HttpUtil.postRequest(url,visitStat);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
				return null;
			}
			
			VisitStat vStat = JsonUtil.parse2Object(json, VisitStat.class);
			return vStat;
		} catch (InterruptedException e) {
			Log.e(TAG, "统计失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "统计失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
		}
		return null;
	}
	private List<VisitStat> countList(String url,VisitStat visitStat){
		try {
			String json = HttpUtil.postRequest(url,visitStat);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "统计失败");
				return null;
			}
			
			List<VisitStat> list = JsonUtil.parse2ListVisitStat(json);
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
