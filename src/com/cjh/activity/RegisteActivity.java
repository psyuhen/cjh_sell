package com.cjh.activity;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

import com.cjh.bean.User;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.SecureUtil;
import com.cjh.utils.Validator;
/**
 * 注册
 * @author ps
 *
 */
public class RegisteActivity extends BaseTwoActivity{
	public static final String TAG = "RegisteActivity";
	private EditText mMobileView;
	private EditText mPasswordView;
	private EditText mComfirmPasswordView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		
		mMobileView = (EditText) findViewById(R.id.register_tel_edit);
		mPasswordView = (EditText) findViewById(R.id.register_password);
		mComfirmPasswordView = (EditText) findViewById(R.id.register_password_comfirm);
		
		Button btnRegister = (Button) findViewById(R.id.register_confirm_btn);
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptRegister();
			}
		});
		
		mMobileView.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					String mobile = mMobileView.getText().toString();
					if(TextUtils.isEmpty(mobile)){
						return;
					}
					String url0 = HttpUtil.BASE_URL + "/user/isregister.do?mobile=" + mobile;
					try {
						String request = HttpUtil.getRequest(url0);
						if(request == null){
							mMobileView.setError("手机号码已被注册！");
						}
					} catch (InterruptedException e) {
						Log.e(TAG, "判断手机号码是否被注册失败", e);
					} catch (ExecutionException e) {
						Log.e(TAG, "判断手机号码是否被注册失败", e);
					}
				}
			}
		});
	}
	
	private void initData() {
		title.setText("注册");
		right_imgbtn.setVisibility(View.GONE);
	}
	
	//注册
	private void attemptRegister() {
		// 清空错误
		mMobileView.setError(null);
		mPasswordView.setError(null);
		mComfirmPasswordView.setError(null);
		
		String mobile = mMobileView.getText().toString();
		String password = mPasswordView.getText().toString();
		String comfirmPassword = mComfirmPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		
		// 手机号码为必输入的以及正确输入.
		if (TextUtils.isEmpty(mobile)) {
			mMobileView.setError(getString(R.string.error_field_required));
			focusView = mMobileView;
			cancel = true;
		} else if (!Validator.isMoblie(mobile)) {
			mMobileView.setError(getString(R.string.error_invalid_mobile));
			focusView = mMobileView;
			cancel = true;
		}else if (TextUtils.isEmpty(password)){// 检验密码.
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}else if(!Validator.isPwdLength(password)){
			mPasswordView.setError(getString(R.string.error_length_password));
			focusView = mPasswordView;
			cancel = true;
		}else if(!Validator.isPassword(password)){
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}else if(!password.equals(comfirmPassword)){
			mComfirmPasswordView.setError(getString(R.string.error_comfirm_password));
			focusView = mComfirmPasswordView;
			cancel = true;
		}
		
		if (cancel) {
			// 有错误，不登录，焦点在错误的输入框中，并显示错误
			focusView.requestFocus();
		} else {
			password = SecureUtil.shaEncode(password);
			//显示进度条，并发送登录请求，准备登录
			String url1 = HttpUtil.BASE_URL + "/user/register.do";
//			String url2 = HttpUtil.BASE_URL + "/user/login.do";
			
			User user = new User();
			user.setName(mobile);
			user.setMobile(mobile);
			user.setPassword(password);
			user.setUser_type("0");//卖家
			// 发送登录请求
			try{
				String request = HttpUtil.postRequest(url1, user);
				if(request == null){
					Log.e(TAG, "手机号码注册失败!");
					CommonsUtil.showLongToast(getApplicationContext(), "手机号码注册失败!");
					return;
				}
				//注册成功后，登录
				/*String request2 = HttpUtil.postRequest(url2, user);
				if(request2 == null){
					Log.e(TAG, "手机号码注册成功，但登录失败!");
					CommonsUtil.showLongToast(getApplicationContext(), "手机号码注册成功，但登录失败!");
					return;
				}*/
				
				//user = JsonUtil.parse2Object(request2, User.class);
				CommonsUtil.showLongToast(getApplicationContext(), "注册成功");
				startActivity(new Intent(RegisteActivity.this, LoginActivity.class));
				finish();
			}catch (Exception e) {
				Log.e(TAG, "注册失败", e);
				CommonsUtil.showLongToast(getApplicationContext(), e.getMessage());
			}
		}
	}
}