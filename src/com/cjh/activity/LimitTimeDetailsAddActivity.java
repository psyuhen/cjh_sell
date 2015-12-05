package com.cjh.activity;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cjh.bean.MerchDisacount;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.Validator;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 添加限时折扣
 * @author pansen
 *
 */
public class LimitTimeDetailsAddActivity extends BaseTwoActivity{
	private Logger LOGGER = LoggerFactory.getLogger(LimitTimeDetailsAddActivity.class);

	private TextView limit_time_title;
	private TextView limit_time_price;
	
	private EditText limit_time_discount_edit;
	private EditText limit_time_starttime_edit;
	private EditText limit_time_enddate_edit;
	
	private int merch_id = 0;
	private int disacount_id = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_limit_time_details);
		initView();
		initData();
	}
	
	@Override
	public void initView() {
		super.initView();
		right_imgbtn.setVisibility(View.GONE);
		right_text.setText("完成");
		title.setText("限时折扣");
		right_text.setOnClickListener(this);
		limit_time_title=(TextView) findViewById(R.id.limit_time_title);
		limit_time_price=(TextView) findViewById(R.id.limit_time_price);
		
		limit_time_discount_edit=(EditText) findViewById(R.id.limit_time_discount_edit);
		limit_time_starttime_edit=(EditText) findViewById(R.id.limit_time_starttime_edit);
		limit_time_enddate_edit=(EditText) findViewById(R.id.limit_time_enddate_edit);
	}
	
	private void initData() {
//		limit_time_title.setText("新鲜水果甘肃静宁红富士苹果水果10斤24个80mm红富士苹果特价包邮");
//		limit_time_price.setText("￥ 12.0");
		
		Intent intent = getIntent();
		merch_id = intent.getIntExtra("merch_id", 0);
		String name = intent.getStringExtra("name");
		float price = intent.getFloatExtra("price", 0);
		
		limit_time_title.setText(name);
		limit_time_price.setText("￥"+price);
		
		queryDisacount();
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_text:
			if(disacount_id <= 0){
				addDisacount();
			}else {
				updateDisacount();
			}
			break;

		default:
			break;
		}
	}

	//添加优惠信息
	private void addDisacount(){
		if(checkInput()){
			return;
		}
		
		String disacount = limit_time_discount_edit.getText().toString();
		String starttime = limit_time_starttime_edit.getText().toString();
		String enddate = limit_time_enddate_edit.getText().toString();
		
		String url = HttpUtil.BASE_URL + "/disacount/add.do";

		MerchDisacount merchDisacount = new MerchDisacount();
		merchDisacount.setMerch_id(merch_id);
//		merchDisacount.setDisacount(Float.parseFloat(disacount));
		merchDisacount.setDisacount_money(Float.parseFloat(disacount));
		merchDisacount.setDisacount_date(starttime);
		merchDisacount.setEffective_date(enddate);
		
		try {
			String listJson = HttpUtil.postRequest(url,merchDisacount);
			CommonsUtil.showLongToast(getApplicationContext(), listJson);
		} catch (Exception e) {
			LOGGER.error("新增优惠信息失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "新增优惠信息失败");
		}
	}
	//检验
	private boolean checkInput(){
		String disacount = limit_time_discount_edit.getText().toString();
		String starttime = limit_time_starttime_edit.getText().toString();
		String enddate = limit_time_enddate_edit.getText().toString();
		
		boolean isCancel = false;
		if(TextUtils.isEmpty(StringUtils.trimToEmpty(disacount))){
			limit_time_discount_edit.setError(getString(R.string.error_field_required));
			isCancel = true;
		}else if(!Validator.isNumber(StringUtils.trimToEmpty(disacount))){
			limit_time_discount_edit.setError(getString(R.string.error_invalid_number));
			isCancel = true;
		}else if(TextUtils.isEmpty(StringUtils.trimToEmpty(starttime))){
			limit_time_starttime_edit.setError(getString(R.string.error_field_required));
			isCancel = true;
		}else if(TextUtils.isEmpty(StringUtils.trimToEmpty(enddate))){
			limit_time_enddate_edit.setError(getString(R.string.error_field_required));
			isCancel = true;
		}
		
		return isCancel;
	}
	
	//更新优惠信息
	private void updateDisacount(){
		if(checkInput()){
			return;
		}
		
		String disacount = limit_time_discount_edit.getText().toString();
		String starttime = limit_time_starttime_edit.getText().toString();
		String enddate = limit_time_enddate_edit.getText().toString();
		
		String url = HttpUtil.BASE_URL + "/disacount/modify.do";
		
		MerchDisacount merchDisacount = new MerchDisacount();
		merchDisacount.setDisacount_id(disacount_id);
		merchDisacount.setMerch_id(merch_id);
//		merchDisacount.setDisacount(Float.parseFloat(disacount));
		merchDisacount.setDisacount_money(Float.parseFloat(disacount));
		merchDisacount.setDisacount_date(starttime);
		merchDisacount.setEffective_date(enddate);
		
		try {
			String listJson = HttpUtil.postRequest(url,merchDisacount);
			CommonsUtil.showLongToast(getApplicationContext(), listJson);
		} catch (Exception e) {
			LOGGER.error("修改优惠信息失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "修改优惠信息失败");
		}
	}
	//查询优惠信息
	private void queryDisacount(){
		String url = HttpUtil.BASE_URL + "/disacount/queryByMerchId.do?merch_id="+merch_id;
		try {
			String listJson = HttpUtil.getRequest(url);
			if(listJson == null){
				CommonsUtil.showLongToast(getApplicationContext(), "查询优惠信息失败");
				return;
			}
			List<MerchDisacount> lists = JsonUtil.parse2ListMerchDisacount(listJson);
			if(!lists.isEmpty()){
				MerchDisacount merchDisacount = lists.get(0);
				
				limit_time_discount_edit.setText(merchDisacount.getDisacount_money()+"");
				limit_time_starttime_edit.setText(merchDisacount.getDisacount_date());
				limit_time_enddate_edit.setText(merchDisacount.getEffective_date());
				
				disacount_id = merchDisacount.getDisacount_id();
			}
		} catch (Exception e) {
			LOGGER.error("查询优惠信息失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询优惠信息失败");
		}
	}
}
