package com.cjh.activity;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cjh.bean.Coupon;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.DateTimePickDialogUtil;
import com.cjh.utils.DateUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.Validator;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

public class CouponAddActivity extends BaseTwoActivity {
	private static final Logger LOGGER = LoggerFactory.getLogger(CouponAddActivity.class);

	private EditText coupons_startdate_edit;
	private EditText coupons_enddate_edit;
	private Button add_coupons_btn;
	// 预设时间对话框
	// 时间控件常量
	private String initStartDateTime = DateUtil.today("yyyy年MM月dd日 mm:ss"); // 初始化开始时间
	
	private EditText coupons_money_edit;
	private EditText coupons_enoughmoney_edit;
	private EditText coupons_surplusnum_edit;
	private EditText coupons_limitsnum_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_coupons);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		coupons_startdate_edit = (EditText) findViewById(R.id.coupons_startdate_edit);
		coupons_enddate_edit = (EditText) findViewById(R.id.coupons_enddate_edit);
		coupons_money_edit = (EditText) findViewById(R.id.coupons_money_edit);
		coupons_enoughmoney_edit = (EditText) findViewById(R.id.coupons_enoughmoney_edit);
		coupons_surplusnum_edit = (EditText) findViewById(R.id.coupons_surplusnum_edit);
		coupons_limitsnum_edit = (EditText) findViewById(R.id.coupons_limitsnum_edit);
		
		add_coupons_btn = (Button) findViewById(R.id.add_coupons_btn);
		add_coupons_btn.setOnClickListener(this);
		coupons_startdate_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(CouponAddActivity.this, initStartDateTime);
				dateTimePicKDialog.dateTimePicKDialog(coupons_startdate_edit);
			}
		});
		coupons_enddate_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(CouponAddActivity.this, initStartDateTime);
				dateTimePicKDialog.dateTimePicKDialog(coupons_enddate_edit);
			}
		});
		title.setText("添加优惠券");
		right_imgbtn.setVisibility(View.GONE);
	}

	private void initData() {
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.add_coupons_btn:
			addCoupon();
			break;
		default:
			break;
		}
	}

	private void addCoupon(){
		String url = HttpUtil.BASE_URL + "/coupon/addCoupon.do";
		
		try {
			
			String coupon_money = coupons_money_edit.getText().toString();
			String min_order_money = coupons_enoughmoney_edit.getText().toString();
			String coupon_total = coupons_surplusnum_edit.getText().toString();
			String coupon_limit = coupons_limitsnum_edit.getText().toString();
			String start_time = coupons_startdate_edit.getText().toString();
			String end_time = coupons_enddate_edit.getText().toString();
			
			coupon_money = StringUtils.trimToEmpty(coupon_money);
			if("".equals(coupon_money)){
				coupons_money_edit.setError(getString(R.string.error_field_required));
				return;
			}
			
			if(!Validator.isNumber(coupon_money)){
				coupons_money_edit.setError(getString(R.string.error_invalid_number));
				return;
			}
			
			min_order_money = StringUtils.trimToEmpty(min_order_money);
			if("".equals(min_order_money)){
				coupons_enoughmoney_edit.setError(getString(R.string.error_field_required));
				return;
			}
			
			if(!Validator.isNumber(min_order_money)){
				coupons_enoughmoney_edit.setError(getString(R.string.error_invalid_number));
				return;
			}
			
			coupon_total = StringUtils.trimToEmpty(coupon_total);
			if("".equals(coupon_total)){
				coupons_surplusnum_edit.setError(getString(R.string.error_field_required));
				return;
			}
			
			if(!Validator.isDigits(coupon_total)){
				coupons_surplusnum_edit.setError(getString(R.string.error_invalid_number));
				return;
			}
			
			coupon_limit = StringUtils.trimToEmpty(coupon_limit);
			if("".equals(coupon_limit)){
				coupons_limitsnum_edit.setError(getString(R.string.error_field_required));
				return;
			}
			if(!Validator.isDigits(coupon_limit)){
				coupons_limitsnum_edit.setError(getString(R.string.error_invalid_number));
				return;
			}
			
			if("".equals(StringUtils.trimToEmpty(start_time))){
				coupons_startdate_edit.setError(getString(R.string.error_field_required));
				return;
			}
			
			if("".equals(StringUtils.trimToEmpty(end_time))){
				coupons_enddate_edit.setError(getString(R.string.error_field_required));
				return;
			}
			
			Coupon coupon = new Coupon();
			coupon.setStore_id(sessionManager.getStoreId());
			
			coupon.setCoupon_money(Float.parseFloat(coupon_money));
			coupon.setMin_order_money(Float.parseFloat(min_order_money));
			coupon.setCoupon_total(Integer.parseInt(coupon_total));
			coupon.setCoupon_limit(Integer.parseInt(coupon_limit));
			
			start_time = DateUtil.format(DateUtil.parseDate(start_time, new String[]{"yyyy/MM/dd HH:mm"}), "yyyyMMddHHmmss");
			end_time = DateUtil.format(DateUtil.parseDate(end_time, new String[]{"yyyy/MM/dd HH:mm"}), "yyyyMMddHHmmss");
			coupon.setStart_time(start_time);
			coupon.setEnd_time(end_time);
			
			
			String listJson = HttpUtil.postRequest(url,coupon);
			if(listJson == null){
				CommonsUtil.showLongToast(getApplicationContext(), "新增优惠券失败");
				return;
			}
			
			CommonsUtil.showLongToast(getApplicationContext(), listJson);
			
			if("新增优惠券信息成功!".equals(listJson)){
				startActivity(new Intent(CouponAddActivity.this, CouponsActivity.class));
				CouponAddActivity.this.finish();
			}
		} catch (Exception e) {
			LOGGER.error("查询优惠券失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询优惠券失败");
		}
	
	}
}
