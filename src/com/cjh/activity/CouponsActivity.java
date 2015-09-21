package com.cjh.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.kymjs.aframe.ui.widget.KJListView;
import org.kymjs.aframe.ui.widget.KJListView.KJListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cjh.adapter.CommonAdapter;
import com.cjh.adapter.ViewHolder;
import com.cjh.bean.Coupon;
import com.cjh.bean.CouponsItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.DateUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.PageUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 优惠券
 * @author pansen
 *
 */
public class CouponsActivity extends BaseTwoActivity {
	private static final Logger LOGGER = LoggerFactory.getLogger(CouponsActivity.class);

	private List<CouponsItem> couponsList;
	private KJListView kjListView;
	private Button add_coupons_btn;
	private CommonAdapter<CouponsItem> commonAdapter;
	
	private int start = PageUtil.START;//分页的开始
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupons);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		title.setText("店铺优惠券");
		right_imgbtn.setVisibility(View.GONE);
		kjListView = (KJListView) findViewById(R.id.coupons_listview);
		add_coupons_btn = (Button) findViewById(R.id.add_coupons_btn);
		add_coupons_btn.setOnClickListener(this);
		couponsList = new ArrayList<CouponsItem>();
		commonAdapter = showAdapter();
		kjListView.setAdapter(commonAdapter);
		//上下拉刷新
		kjListView.setPullLoadEnable(true);
		kjListView.setKJListViewListener(new KJListViewListener() {
			@Override
			public void onRefresh() {
				queryCoupon(PageUtil.START);
			}

			@Override
			public void onLoadMore() {
				queryCoupon(CouponsActivity.this.start);
			}
		});
	}

	private void initData() {
		queryCoupon(PageUtil.START);
	}
	
	private void queryCoupon(int start){
		int user_id = sessionManager.getUserId();
		String url = HttpUtil.BASE_URL + "/coupon/queryByUserIdPage.do?user_id="+user_id+"&start="+start+"&limit="+PageUtil.LIMIT;
		
		try {
			
			String listJson = HttpUtil.getRequest(url);
			if(listJson == null){
				CommonsUtil.showLongToast(getApplicationContext(), "查询优惠券失败");
				kjListView.stopRefreshData();
				return;
			}
			
			List<Coupon> list = JsonUtil.parse2ListCoupon(listJson);
			if(list == null){
				LOGGER.warn("转换优惠券信息失败");
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
				couponsList.clear();
			}
			
			for (int i = 0; i < length; i++) {
				Coupon coupon = list.get(i);
				
				CouponsItem couponsItem = new CouponsItem();
				couponsItem.setTitle(coupon.getDesc());
				String start_time = coupon.getStart_time();
				Date startDate = DateUtil.parseDate(start_time, new String[]{"yyyyMMddHHmmss"});
				couponsItem.setStartDate(startDate);
				Date endDate = DateUtil.parseDate(coupon.getEnd_time(), new String[]{"yyyyMMddHHmmss"});
				couponsItem.setEndDate(endDate);
				couponsItem.setMoney(coupon.getCoupon_money());
				couponsItem.setEnoughmoney(coupon.getMin_order_money());
				
				int surplusnum = coupon.getCoupon_total() - coupon.getHas_coupon_num();
				couponsItem.setSurplusnum(surplusnum);//剩下几个券
				
				couponsItem.setStatus('1');//可领用
				
				if(startDate.compareTo(new Date()) > 0){//开始时间大于当前时间
					couponsItem.setStatus('2');//未开始
				}
				
				if(surplusnum == 0){
					couponsItem.setStatus('3');//已领完
				}
				
				if(endDate.compareTo(new Date()) < 0){
					couponsItem.setStatus('4');//已过期
				}
				
				couponsList.add(couponsItem);
			}
			
			this.start += PageUtil.LIMIT;//每次改变start的值 
			commonAdapter.notifyDataSetChanged();
			kjListView.stopRefreshData();
		} catch (InterruptedException e) {
			LOGGER.error("查询优惠券失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询优惠券失败");
		} catch (ExecutionException e) {
			LOGGER.error("查询优惠券失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询优惠券失败");
		}
	}

	public CommonAdapter<CouponsItem> showAdapter() {
		return new CommonAdapter<CouponsItem>(CouponsActivity.this,
				couponsList, R.layout.item_coupons) {
			@Override
			public void convert(ViewHolder helper, CouponsItem item) {
				
				helper.setText(R.id.item_coupons_money, "￥" + item.getMoney());
				helper.setText(R.id.item_coupons_enoughmoney, "满" + item.getEnoughmoney() + "元可用");
				helper.setText(R.id.item_coupons_startdate, DateUtil.format(item.getStartDate()));
				helper.setText(R.id.item_coupons_enddate, DateUtil.format(item.getEndDate()));
				helper.setText(R.id.item_coupons_surplusnum, "剩余" + item.getSurplusnum() + "张");
				helper.setText(R.id.item_coupons_status, CommonsUtil.getCouponsStatus(item.getStatus()));

			}
		};
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.add_coupons_btn://添加优惠券
			startActivity(new Intent(CouponsActivity.this, CouponAddActivity.class));
			break;

		default:
			break;
		}
	}

}
