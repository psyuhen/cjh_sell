package com.cjh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cjh.auth.SessionManager;
import com.cjh.bean.User;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.SecureUtil;
import com.cjh.utils.SwitchButton;
import com.cjh.utils.SwitchButton.OnCheckChangeListener;
import com.cjh.utils.Validator;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 登录
 * @author ps
 *
 */
public class LoginActivity extends BaseTwoActivity implements OnClickListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginActivity.class);
	private TextView login_register;
	private TextView login_retrieve;
	private Button login_btn;
	
	//手机号和密码
	private EditText mMobileView;
	private EditText mPasswordView;
	
	private SwitchButton login_show_password;//显示密码的按钮
	
	private String tmpMobile;
	private String tmpPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initData();
	}

	private void initData() {
		right_imgbtn.setVisibility(View.GONE);
		title.setText("登录");
		
		//从注册跳转过来的，自动登录
		Intent intent = getIntent();
		tmpMobile = intent.getStringExtra("mobile");
		tmpPwd = intent.getStringExtra("password");
		if(!TextUtils.isEmpty(tmpMobile) && !TextUtils.isEmpty(tmpPwd)){
			mMobileView.setText(tmpMobile);
			mPasswordView.setText(tmpPwd);
			attemptLogin();
		}
	}

	@Override
	public void initView() {
		super.initView();
		login_register = (TextView) findViewById(R.id.login_registe);
		login_register.setOnClickListener(this);
		login_retrieve = (TextView) findViewById(R.id.login_retrieve);
		login_retrieve.setOnClickListener(this);
		login_btn = (Button) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(this);
		
		back.setVisibility(View.GONE);
		
		// 初始化登录页面
		mMobileView = (EditText) findViewById(R.id.login_username_edit);
		mPasswordView = (EditText) findViewById(R.id.login_password_edit); 
		
		login_show_password = (SwitchButton)findViewById(R.id.login_show_password);
		login_show_password.setOnCheckChangeListener(new OnCheckChangeListener() {
			@Override
			public void OnCheck(SwitchButton switchButton, boolean isChecked) {
				if(!isChecked){
					mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.login_registe:
			startActivity(new Intent(LoginActivity.this, RegisteActivity.class));
			break;
		case R.id.login_btn:
			attemptLogin();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 登录事件
	 */
	private void attemptLogin() {
		// 清空错误
		mMobileView.setError(null);
		mPasswordView.setError(null);
		
		// 保存输入的手机号码和密码
		String mobile = mMobileView.getText().toString();
		String password = mPasswordView.getText().toString();
		
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
		}

		// 检验密码.
		if (TextUtils.isEmpty(password)){
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}else if(!Validator.isPwdLength(password)){
			mPasswordView.setError(getString(R.string.error_length_password));
			focusView = mPasswordView;
			cancel = true;
		}
		/*else if(!Validator.isPassword(password)){
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}*/
		

		if (cancel) {
			// 有错误，不登录，焦点在错误的输入框中，并显示错误
			focusView.requestFocus();
		}else{
			//显示进度条，并发送登录请求，准备登录
			password = SecureUtil.shaEncode(password);

			User user = new User();
			user.setMobile(mobile);
			user.setPassword(password);
			user.setUser_type("0");//卖家
			// 发送登录请求
			try{
				//先检查手机号是否存在
				String url = HttpUtil.BASE_URL + "/user/isregister.do?mobile="+mobile;
				String json = HttpUtil.getRequest(url);
				if(json == null){
					CommonsUtil.showLongToast(getApplicationContext(), "网络或者服务器异常");
					sessionManager.putBoolean(SessionManager.IS_LOGIN, false);
					return ;
				}
				if("false".equals(json)){
					CommonsUtil.showLongToast(getApplicationContext(), "手机号码不存在");
					sessionManager.putBoolean(SessionManager.IS_LOGIN, false);
					return ;
				}
				
				url = HttpUtil.BASE_URL + "/user/login.do";
				json = HttpUtil.postRequest(url, user);
				if(json == null){
					CommonsUtil.showLongToast(getApplicationContext(), "登录失败,用户密码错误或者网络异常");
					sessionManager.putBoolean(SessionManager.IS_LOGIN, false);
					return ;
				}
				
				user = JsonUtil.parse2Object(json, User.class);
				sessionManager.createLoginSession(user);
				
				int store_id = user.getStore_id();
				if(store_id > 0){
					sessionManager.putInt("store_id", store_id);
					sessionManager.put("store_name", user.getName());
					
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
				}else{
					startActivity(new Intent(LoginActivity.this, ShopEditActivity.class));
				}
				
				finish();
			}catch (Exception e) {
				LOGGER.error(">>> 用户登录失败", e);
				CommonsUtil.showLongToast(getApplicationContext(), "用户登录失败");
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			
		}
	}
}
