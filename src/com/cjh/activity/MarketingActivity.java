package com.cjh.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cjh.bean.FavoriteStat;
import com.cjh.bean.OrderStat;
import com.cjh.bean.VisitStat;
import com.cjh.cjh_sell.R;
import com.cjh.common.LineView;
import com.cjh.utils.DateUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 统计中心
 * @author pansen
 *
 */
public class MarketingActivity extends BaseTwoActivity implements
		OnClickListener {
	private Logger LOGGER = LoggerFactory.getLogger(MarketingActivity.class);
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
		title.setText("统计中心");
		
		//TODO 修改为异步了，感觉代码有点冗余。。待优化。
		countOrder();
		countMoney();
		countFavorite();
		countVisit();
	}
	
	private void countOrder(){
		startProgressDialog();
		new CountTask().execute();
	}
	private void countMoney(){
		startProgressDialog();
		new CountTask1().execute();
	}
	private void countFavorite(){
		startProgressDialog();
		new CountTask2().execute();
	}
	private void countVisit(){
		startProgressDialog();
		new CountTask3().execute();
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
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void fillBom(LineView lineView) {
		ArrayList<String> bottomlist = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			bottomlist.add((i < 10 ? "0"+i : i) + ":00");
		}
		lineView.setBottomTextList(bottomlist);
	}
	//成交订单
	public void fillSellData(List<OrderStat> sell){
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			String index = ((i < 10) ? ("0" + i) : i) +":00";
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
		lineView.setDataList(dataslist);
	}
	public void fillMoneyData(List<OrderStat> money){
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			String index = ((i < 10) ? ("0" + i) : i) +":00";
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
		today_mnoney_lineview.setDataList(dataslist);
	}
	public void fillFavoriteData(List<FavoriteStat> favorite){
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			String index = ((i < 10) ? ("0" + i) : i) +":00";
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
			String index = ((i < 10) ? ("0" + i) : i) +":00";
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
				return null;
			}
			
			OrderStat orderStat = JsonUtil.parse2Object(json, OrderStat.class);
			return orderStat;
		} catch (Exception e) {
			LOGGER.error(">>> 统计失败", e);
		}
		return null;
	}
	private List<OrderStat> countListOrder(String url, Map<String,String> map){
		try {
			String json = HttpUtil.postRequest(url,map);
			if(json == null){
				return null;
			}
			
			List<OrderStat> list = JsonUtil.parse2ListOrderStat(json);
			return list;
		} catch (Exception e) {
			LOGGER.error(">>> 统计失败", e);
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
		return (FavoriteStat)countT(url,favoriteStat);
		
	}
	private List<FavoriteStat> countByDayHourAndUser(FavoriteStat favoriteStat){
		String url = HttpUtil.BASE_URL + "/storeuf/countByDayHourAndUser.do";
		return countTList(url,favoriteStat);
	}
	
	private VisitStat countByDayAndUser(VisitStat visitStat){
		String url = HttpUtil.BASE_URL + "/storevisit/countByDayAndUser.do";
		return (VisitStat)countT(url,visitStat);
		
	}
	private List<VisitStat> countByDayHourAndUser(VisitStat visitStat){
		String url = HttpUtil.BASE_URL + "/storevisit/countByDayHourAndUser.do";
		return countTList(url,visitStat);
	}
	
	private Object countT(String url,Object obj){
		try {
			String json = HttpUtil.postRequest(url,obj);
			if(json == null){
				return null;
			}
			
			Object o = JsonUtil.parse2Object(json, obj.getClass());
			return o;
		} catch (Exception e) {
			LOGGER.error(">>> 统计失败", e);
		}
		return null;
	}
	
	private List<FavoriteStat> countTList(String url,FavoriteStat obj){
		try {
			String json = HttpUtil.postRequest(url,obj);
			if(json == null){
				return null;
			}
			
			List<FavoriteStat> list = JsonUtil.parse2ListFavoriteStat(json);
			return list;
		} catch (Exception e) {
			LOGGER.error(">>> 统计失败", e);
		}
		return null;
	}
	private List<VisitStat> countTList(String url,VisitStat obj){
		try {
			String json = HttpUtil.postRequest(url,obj);
			if(json == null){
				return null;
			}
			
			List<VisitStat> list = JsonUtil.parse2ListVisitStat(json);
			return list;
		} catch (Exception e) {
			LOGGER.error(">>> 统计失败", e);
		}
		return null;
	}
	
	private class CountTask extends AsyncTask<Void, Void, CountResult>{
		@Override
		protected CountResult doInBackground(Void... params) {
			String today = DateUtil.today();
			String yesterday = DateUtil.yesterday();
			
			OrderStat todayOrder = countByDay(today);
			OrderStat yesterdayOrder = countByDay(yesterday);
			
			List<OrderStat> sellOrder = countByDayHour(today);
			if(sellOrder == null){
				sellOrder = new ArrayList<OrderStat>();
			}
			
			int today_count = todayOrder == null ? 0 : todayOrder.getOrder_sell();
			int yesterday_count = yesterdayOrder == null ? 0 : yesterdayOrder.getOrder_sell();
			return new CountResult(today_count, yesterday_count, sellOrder);
		}
		@Override
		protected void onPostExecute(CountResult result) {
			super.onPostExecute(result);
			stopProgressDialog();
			
			market_today_count.setText(result.today_count+"");
			market_yesterday_count.setText(result.yesterday_count+"");
			fillSellData((List<OrderStat>)result.list);
		}
	}
	private class CountTask1 extends AsyncTask<Void, Void, CountResult>{
		@Override
		protected CountResult doInBackground(Void... params) {
			String today = DateUtil.today();
			String yesterday = DateUtil.yesterday();
			
			OrderStat todayMoney = countMoneyByDay(today);
			OrderStat yesterdayMoney = countMoneyByDay(yesterday);
			
			
			List<OrderStat> sellMoney = countMoneyByDayHour(today);
			if(sellMoney == null){
				sellMoney = new ArrayList<OrderStat>();
			}
			
			float today_count = todayMoney == null ? 0 : todayMoney.getAmount_money();
			float yesterday_count = yesterdayMoney == null ? 0 : yesterdayMoney.getAmount_money();
			return new CountResult(today_count, yesterday_count, sellMoney);
		}
		@Override
		protected void onPostExecute(CountResult result) {
			super.onPostExecute(result);
			stopProgressDialog();
			
			market_money_today_count.setText(result.today_count+"");
			market_money_yesterday_count.setText(result.yesterday_count+"");
			fillMoneyData((List<OrderStat>)result.list);
		}
	}
	private class CountTask2 extends AsyncTask<Void, Void, CountResult>{
		@Override
		protected CountResult doInBackground(Void... params) {
			String today = DateUtil.today();
			String yesterday = DateUtil.yesterday();
			
			FavoriteStat favoriteStat = new FavoriteStat();
			int store_id = sessionManager.getInt("store_id");
			favoriteStat.setStore_id(store_id);//商家ID
			
			favoriteStat.setStat_date(today);
			FavoriteStat todayFavorite = countByDayAndUser(favoriteStat);
			favoriteStat.setStat_date(yesterday);
			FavoriteStat yesterdayFavorite = countByDayAndUser(favoriteStat);
			
			favoriteStat.setStat_date(today);
			List<FavoriteStat> favorite = countByDayHourAndUser(favoriteStat);
			if(favorite == null){
				favorite = new ArrayList<FavoriteStat>();
			}
			
			int today_count = todayFavorite == null ? 0 : todayFavorite.getFavorite_count();
			int yesterday_count = yesterdayFavorite == null ? 0 : yesterdayFavorite.getFavorite_count();
			return new CountResult(today_count, yesterday_count, favorite);
		}
		@Override
		protected void onPostExecute(CountResult result) {
			super.onPostExecute(result);
			stopProgressDialog();
			
			market_everyday_collect_today_count.setText(result.today_count+"");
			market_everyday_collect_yesterday_count.setText(result.yesterday_count+"");
			fillFavoriteData((List<FavoriteStat>)result.list);
		}
	}
	private class CountTask3 extends AsyncTask<Void, Void, CountResult>{
		@Override
		protected CountResult doInBackground(Void... params) {
			String today = DateUtil.today();
			String yesterday = DateUtil.yesterday();
			
			
			VisitStat visitStat = new VisitStat();
			int store_id = sessionManager.getInt("store_id");
			visitStat.setStore_id(store_id);//商家ID
			
			visitStat.setStat_date(today);
			VisitStat todayVisit = countByDayAndUser(visitStat);
			visitStat.setStat_date(yesterday);
			VisitStat yesterdayVisit = countByDayAndUser(visitStat);
			
			
			visitStat.setStat_date(today);
			List<VisitStat> visit = countByDayHourAndUser(visitStat);
			if(visit == null){
				visit = new ArrayList<VisitStat>();
			}
			
			int today_count = todayVisit == null ? 0 : todayVisit.getVisit_count();
			int yesterday_count = yesterdayVisit == null ? 0 : yesterdayVisit.getVisit_count();
			return new CountResult(today_count, yesterday_count, visit);
		}
		@Override
		protected void onPostExecute(CountResult result) {
			super.onPostExecute(result);
			stopProgressDialog();
			
			market_customer_today_count.setText(result.today_count+"");
			market_customer_yesterday_count.setText(result.yesterday_count+"");
			fillVisitData((List<VisitStat>)result.list);
		}
	}
	
	private class CountResult{
		private float today_count;//今天
		private float yesterday_count;//昨天
		private List<?> list;
		
		public CountResult(float today_count,float yesterday_count,List<?> list) {
			this.today_count = today_count;
			this.yesterday_count = yesterday_count;
			this.list = list;
		}
	}
}
