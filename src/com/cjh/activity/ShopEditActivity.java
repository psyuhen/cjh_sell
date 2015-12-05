package com.cjh.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjh.adapter.AddImageAdapter;
import com.cjh.auth.SessionManager;
import com.cjh.bean.AddImage;
import com.cjh.bean.Store;
import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.FileUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.ImageUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.QiNiuUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

/**
 * 
 * 编辑店铺
 * @author ps
 *
 */
public class ShopEditActivity extends BaseTwoActivity {
	private Logger LOGGER = LoggerFactory.getLogger(ShopEditActivity.class);
	// 添加图片对话框
	private AlertDialog imageChooseDialog = null;
	private ImageView content_add_image;
	// 获取的图片和路径
	private String picturePath;
	private String ImageName;
	// 图片添加管理
	private GridView gridView;
	// gridview适配器
	private AddImageAdapter adapter;
	private List<AddImage> lists;

	
	private EditText shop_edit_detail_title;//店铺名称
	private EditText shop_edit_detail_content;//店铺内容
	private EditText shop_edit_detail_address;//地址
	private EditText shop_edit_detail_tel;//电话
	private EditText shop_edit_detail_person;//负责人
	
	private Button shop_edit_details_complete_btn;//完成编辑
	
	private boolean isBack2Last = false;//是否跳转到上一个页面
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_edit);
		content_add_image = (ImageView) findViewById(R.id.content_add_image);
		content_add_image.setOnClickListener(this);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		content_add_image = (ImageView) findViewById(R.id.content_add_image);
		content_add_image.setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.content_add_gridview);
		lists = new ArrayList<AddImage>();
		
		shop_edit_detail_title = (EditText)findViewById(R.id.shop_edit_detail_title);
		shop_edit_detail_content = (EditText)findViewById(R.id.shop_edit_detail_content);
		shop_edit_detail_address = (EditText)findViewById(R.id.shop_edit_detail_address);
		shop_edit_detail_tel = (EditText)findViewById(R.id.shop_edit_detail_tel);
		shop_edit_detail_person = (EditText)findViewById(R.id.shop_edit_detail_person);
		
		shop_edit_details_complete_btn = (Button)findViewById(R.id.shop_edit_details_complete_btn);
		shop_edit_details_complete_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishEdit();
			}
		});
	}

	private void initData() {
		title.setText("店铺名片管理");
		right_imgbtn.setVisibility(View.GONE);
		adapter = new AddImageAdapter(ShopEditActivity.this, lists);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long len) {
				AddImage addImage = lists.get(position);
				lists.remove(addImage);
				adapter.notifyDataSetChanged();
				//添加图片控件出现
				content_add_image.setVisibility(View.VISIBLE);
				
				String fileName = addImage.getFileName();
				QiNiuUtil.deleteFile(fileName);
				//更新数据库的文件名
				Store storeInfo = new Store();
				int store_id = sessionManager.getInt("store_id");
				if(store_id > 0){//
					storeInfo.setStore_id(store_id);
					storeInfo.setLogo("");

					isBack2Last = false;
					updateStore(storeInfo);
				}
			}
		});
		
		int user_id = sessionManager.getUserId();
		querybyuserid(user_id);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.content_add_image:
			showImageChoose();
			break;
		case R.id.dialog_album:
			imageChooseDialog.dismiss();
			CommonsUtil.opengallry(ShopEditActivity.this);
			break;
		case R.id.dialog_camera:
			CommonsUtil.openCamera(ShopEditActivity.this);
			break;
		case R.id.dialog_cancel:
			imageChooseDialog.dismiss();
			break;
		case R.id.right_text:
			CommonsUtil.showShortToast(ShopEditActivity.this, "添加成功");
			startActivity(new Intent(ShopEditActivity.this, GoodsActivity.class));
			break;
		default:
			break;
		}
	}

	private void showImageChoose() {
		/*imageChooseDialog = new AlertDialog.Builder(ShopEditActivity.this)
				.create();
		imageChooseDialog.show();
		imageChooseDialog.getWindow().setContentView(
				R.layout.image_choose_dialog);
		Button dialog_album = (Button) imageChooseDialog
				.findViewById(R.id.dialog_album);
		dialog_album.setOnClickListener(this);
		Button dialog_camera = (Button) imageChooseDialog
				.findViewById(R.id.dialog_camera);
		dialog_camera.setOnClickListener(this);
		Button dialog_cancel = (Button) imageChooseDialog
				.findViewById(R.id.dialog_cancel);
		dialog_cancel.setOnClickListener(this);*/
		if(imageChooseDialog == null){
			imageChooseDialog = ImageUtil.showImageChoose(ShopEditActivity.this);
		}else{
			imageChooseDialog.show();
		}
	}
	
	/**
	 * 获取照片
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.IMAGE_REQUEST_CODE://打开相册后返回的数据
			if(data == null){
				break;
			}
			Uri selectImage = data.getData();
			if (selectImage != null) {
				String uriStr = selectImage.toString();
				String path = uriStr.substring(10, uriStr.length());
				if (path.startsWith("com.sec.android.gallery3d")) {
					Toast.makeText(
							this,
							"It's auto backup pic path:"
									+ selectImage.toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = ShopEditActivity.this.getContentResolver().query(
					selectImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			ImageName = picturePath.substring(picturePath.lastIndexOf("."),
					picturePath.length());
			ImageName = "test123" + ImageName;
			cursor.close();
			ImageUtil.startPhotoZoom(data.getData(), ShopEditActivity.this);
			break;
		case Constants.CAMERA_REQUEST_CODE://打开相机后返回来数据 
			if (CommonsUtil.hasSdcard()) {
				File tempFile = FileUtil.getAppFolderFile(Constants.IMAGE_FILE_NAME);
				ImageUtil.startPhotoZoom(Uri.fromFile(tempFile),
						ShopEditActivity.this);
			} else {
				Toast.makeText(ShopEditActivity.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_LONG).show();
			}

			break;
		case Constants.RESULT_REQUEST_CODE:
			if (data != null) {
				getImageToView(data, ShopEditActivity.this);
				imageChooseDialog.hide();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data, Context context) {
		Bundle extras = data.getExtras();
		if(extras == null){
			return;
		}
		
		//显示加载图片
		startProgressDialog();

		Bitmap photo = extras.getParcelable("data");
		AddImage addImage = new AddImage();
		addImage.setBitmap(photo);
		lists.add(addImage);
		adapter.notifyDataSetChanged();
		//添加图片控件消失
		content_add_image.setVisibility(View.GONE);
		
		//上传图片到7牛
		File image = ImageUtil.bitmap2file(photo);
		if(image == null){
			stopProgressDialog();
			CommonsUtil.showShortToast(getApplicationContext(), "生成图片文件失败");
			return;
		}
		addImage.setFile(image);
		addImage.setFileName(image.getName());
		int user_id = sessionManager.getUserId();
		QiNiuUtil.resumeUploadFile(image.getName(), image, String.valueOf(user_id), new UpCompletionHandler() {
			@Override
			public void complete(String key, ResponseInfo info, JSONObject jsonObj) {
				stopProgressDialog();
				if(info.statusCode == HttpStatus.OK.value()){
//					CommonsUtil.showShortToast(getApplicationContext(), "更新图片成功");
					LOGGER.info("上传图片成功！");
					//更新数据库的文件名
					Store storeInfo = new Store();
					int store_id = sessionManager.getInt("store_id");
					if(store_id > 0){
						storeInfo.setStore_id(store_id);
						storeInfo.setLogo(key);

						isBack2Last = false;
						updateStore(storeInfo);
					}
					
				}else{
//					CommonsUtil.showShortToast(getApplicationContext(), "保存图片到服务器失败");
					LOGGER.error("保存图片到服务器失败" + info.error);
				}
			}
		});
	}
	
	/**
	 * 先从本地中获取，如果获取不到再获取网络图片
	 * @param fileName
	 */
	private void getImageToView(String fileName){
		Bitmap bitmap = null;
		bitmap = FileUtil.getCacheFile(fileName);
		
		if(bitmap != null){//防止没有图片的时候，报错
			AddImage addImage = new AddImage();
			addImage.setBitmap(bitmap);
			//添加图片控件消失
			content_add_image.setVisibility(View.GONE);//添加图标隐藏
			File image = ImageUtil.bitmap2file(bitmap);
			if(image != null){
				addImage.setFile(image);
				addImage.setFileName(image.getName());
			}
			
			lists.add(addImage);
			adapter.notifyDataSetChanged();
		}
	}
	
	private void querybyuserid(int user_id){
		new querybyuseridTask(user_id).execute();
	}
	
	private class querybyuseridTask extends AsyncTask<Void, Void, String>{
		private int user_id;
		public querybyuseridTask(int user_id) {
			this.user_id = user_id;
		}
		@Override
		protected String doInBackground(Void... params) {
			//根据用户查询商家信息
			String url1 = HttpUtil.BASE_URL + "/store/querybyuser.do?user_id="+user_id;
			try {
				String json = HttpUtil.getRequest(url1);
				
				return json;
			} catch (Exception e) {
				LOGGER.error(">>> 根据用户获取商家信息失败",e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			stopProgressDialog();
			if(result != null){
				Store store = JsonUtil.parse2Object(result, Store.class);
				shop_edit_detail_title.setText(store.getName());
				shop_edit_detail_content.setText(store.getDesc());
				shop_edit_detail_address.setText(store.getAddress());
				shop_edit_detail_tel.setText(store.getPhone());
				shop_edit_detail_person.setText(store.getPerson());
				
				String fileName = store.getLogo();
				getImageToView(fileName);
			}
		}
	}
	
	//完成编辑
	private void finishEdit(){
		String shopName = shop_edit_detail_title.getText().toString();
		String shopDesc = shop_edit_detail_content.getText().toString();
		String address = shop_edit_detail_address.getText().toString();
		String phone = shop_edit_detail_tel.getText().toString();
		String person = shop_edit_detail_person.getText().toString();
		
		boolean cancel = false;
		
		if (TextUtils.isEmpty(shopName)) {
			shop_edit_detail_title.setError(getString(R.string.error_field_required));
			cancel = true;
		}else if (TextUtils.isEmpty(shopDesc)) {
			shop_edit_detail_content.setError(getString(R.string.error_field_required));
			cancel = true;
		}else if (TextUtils.isEmpty(address)) {
			shop_edit_detail_address.setError(getString(R.string.error_field_required));
			cancel = true;
		}else if (TextUtils.isEmpty(phone)) {
			shop_edit_detail_tel.setError(getString(R.string.error_field_required));
			cancel = true;
		}else if (TextUtils.isEmpty(person)) {
			shop_edit_detail_tel.setError(getString(R.string.error_field_required));
			cancel = true;
		}
		
		if (cancel) {
			// 有错误，不提交
			return;
		}

		int user_id = sessionManager.getInt(SessionManager.KEY_USER_ID);
		int store_id = sessionManager.getInt("store_id");
		
		Store storeInfo = new Store();
		storeInfo.setUser_id(user_id);
//		storeInfo.setClassify_id(Integer.parseInt(classify_id));
		storeInfo.setPerson(person);
		storeInfo.setDesc(shopDesc);
		storeInfo.setAddress(address);
		storeInfo.setPhone(phone);
		storeInfo.setName(shopName);
		storeInfo.setStore_id(store_id);

		isBack2Last = true;
		if(store_id > 0){
			updateStore(storeInfo);
		}else{
			addStore(storeInfo);
		}
	}
	//增加商家
	private class addStoreTask extends AsyncTask<Void, Void, Store>{
		private int user_id;
		private Store storeInfo;
		public addStoreTask(int user_id, Store storeInfo) {
			this.user_id = user_id;
			this.storeInfo = storeInfo;
		}
		@Override
		protected Store doInBackground(Void... params) {
			//注册商家信息
			String url = HttpUtil.BASE_URL + "/store/register.do";
			String json = null;
			try {
				json = HttpUtil.postRequest(url, storeInfo);
				if(json == null){
					return null;
				}
				CommonsUtil.showShortToast(getApplicationContext(), json);
				
				if("注册商店成功!".equals(json)){
					url =  HttpUtil.BASE_URL + "/store/querybyuser.do?user_id="+user_id;
					json = HttpUtil.getRequest(url);
					if(json != null){
						Store store = JsonUtil.parse2Object(json, Store.class);
						return store;
					}
				}
				
			} catch (Exception e) {
				LOGGER.error(">>> 注册商家信息失败", e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Store result) {
			super.onPostExecute(result);
			stopProgressDialog();
			if(result == null){
				CommonsUtil.showShortToast(getApplicationContext(), "注册商家信息失败");
			}else{
				sessionManager.putInt("store_id", result.getStore_id());
				sessionManager.put("store_name", result.getName());
			}
			
			if(isBack2Last){
				ShopEditActivity.this.setResult(RESULT_OK);
				ShopEditActivity.this.finish();
			}
		}
	}
	
	private void addStore(Store storeInfo){
		startProgressDialog();
		int user_id = sessionManager.getUserId();
		new addStoreTask(user_id, storeInfo).execute();
	}
	
	//增加商家
	private class updateStoreTask extends AsyncTask<Void, Void, String>{
		private Store storeInfo;
		public updateStoreTask(Store storeInfo) {
			this.storeInfo = storeInfo;
		}
		@Override
		protected String doInBackground(Void... params) {
			//更新商家信息
			String url = HttpUtil.BASE_URL + "/store/modify.do";
			String json = null;
			try {
				json = HttpUtil.postRequest(url, storeInfo);
				return json;
			} catch (Exception e) {
				LOGGER.error(">>> 更新商家信息失败", e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			stopProgressDialog();
			if(result == null){
				CommonsUtil.showShortToast(getApplicationContext(), "更新商家信息失败");
			}else{
				CommonsUtil.showShortToast(getApplicationContext(), result);
			}
			
			if(isBack2Last){
				ShopEditActivity.this.setResult(RESULT_OK);
				ShopEditActivity.this.finish();
			}
		}
	}
	
	
	private void updateStore(Store storeInfo){
		//更新商家信息
		startProgressDialog();
		new updateStoreTask(storeInfo).execute();
	}
}
