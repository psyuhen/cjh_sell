package com.cjh.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.cjh.bean.AddImage;
import com.cjh.bean.Store;
import com.cjh.bean.User;
import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.ImageUtil;
import com.cjh.utils.JsonUtil;

/**
 * 
 * 编辑店铺
 * @author ps
 *
 */
public class ShopEditActivity extends BaseTwoActivity {
	public static final String TAG = "ShopEditActivity"; 
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
	
	private Button shop_edit_details_complete_btn;//完成编辑
	
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
				content_add_image.setVisibility(View.GONE);
			}
		});
		
		querybyuserid();
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
		imageChooseDialog = new AlertDialog.Builder(ShopEditActivity.this)
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
		dialog_cancel.setOnClickListener(this);
	}
	
	/**
	 * 获取照片
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.IMAGE_REQUEST_CODE:
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
		case Constants.CAMERA_REQUEST_CODE:
			if (CommonsUtil.hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ Constants.IMAGE_FILE_NAME);
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
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			AddImage addImage = new AddImage();
			addImage.setBitmap(photo);
			lists.add(addImage);
			adapter.notifyDataSetChanged();
			//添加图片控件消失
			content_add_image.setVisibility(View.GONE);
		}
	}
	
	private void querybyuserid(){
		User user = sessionManager.getUserDetails();
		//根据用户查询商家信息
		String url1 = HttpUtil.BASE_URL + "/store/querybyuser.do?user_id="+user.getUser_id();
		try {
			String json = HttpUtil.getRequest(url1);
			if(json != null){
				Store store = JsonUtil.parse2Object(json, Store.class);
				shop_edit_detail_title.setText(store.getName());
				shop_edit_detail_content.setText(store.getDesc());
				shop_edit_detail_address.setText(store.getAddress());
				shop_edit_detail_tel.setText(store.getPhone());
			}
		} catch (InterruptedException e) {
			Log.e(TAG, "根据用户获取商家信息失败", e);
		} catch (ExecutionException e) {
			Log.e(TAG, "根据用户获取商家信息失败", e);
		}
		
	}
	
	//完成编辑
	private void finishEdit(){
		String shopName = shop_edit_detail_title.getText().toString();
		String shopDesc = shop_edit_detail_content.getText().toString();
		String address = shop_edit_detail_address.getText().toString();
		String phone = shop_edit_detail_tel.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		if (TextUtils.isEmpty(shopName)) {
			shop_edit_detail_title.setError(getString(R.string.error_field_required));
			focusView = shop_edit_detail_title;
			cancel = true;
		}else if (TextUtils.isEmpty(shopDesc)) {
			shop_edit_detail_content.setError(getString(R.string.error_field_required));
			focusView = shop_edit_detail_content;
			cancel = true;
		}else if (TextUtils.isEmpty(address)) {
			shop_edit_detail_address.setError(getString(R.string.error_field_required));
			focusView = shop_edit_detail_address;
			cancel = true;
		}else if (TextUtils.isEmpty(phone)) {
			shop_edit_detail_tel.setError(getString(R.string.error_field_required));
			focusView = shop_edit_detail_tel;
			cancel = true;
		}
		
		if (cancel) {
			// 有错误，不提交
			focusView.requestFocus();
		}else {
			User user = sessionManager.getUserDetails();
			Store storeInfo = new Store();
			storeInfo.setUser_id(user.getUser_id());
//			storeInfo.setClassify_id(Integer.parseInt(classify_id));
			storeInfo.setDesc(shopDesc);
			storeInfo.setAddress(address);
			storeInfo.setPhone(phone);
			storeInfo.setName(shopName);
			storeInfo.setStore_id(Integer.parseInt(sessionManager.get("store_id")));

			//更新商家信息
			String url = HttpUtil.BASE_URL + "/store/modify.do";
			String json = null;
			try {
				json = HttpUtil.postRequest(url, storeInfo);
			} catch (InterruptedException e) {
				Log.e(TAG, "更新商家信息失败", e);
			} catch (ExecutionException e) {
				Log.e(TAG, "更新商家信息失败", e);
			}
			if(json != null){
				CommonsUtil.showShortToast(getApplicationContext(), json);
			}
		}
	}
}
