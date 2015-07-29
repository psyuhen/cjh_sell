package com.cjh.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjh.bean.ClassifyInfo;
import com.cjh.bean.Gallery;
import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.ImageUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.QiNiuUtil;
import com.qiniu.api.io.PutRet;
/**
 * 分类明细
 * @author ps
 *
 */
public class CategoryDetailsActivity extends BaseTwoActivity {
	public static final String TAG = "CategoryDetailsActivity";
	// 添加图片对话框
	private AlertDialog imageChooseDialog = null;
	// 添加图片图标
	private ImageView category_detail_image;
	// 已成功添加的图标
	private ImageView category_detail_image1;
	private ImageView category_detail_deleteimage1;
	// 获取的图片和路径
	private String picturePath;
	private String ImageName;
	
	private int classify_id;
	private EditText category_detail_title;//标题
	private EditText category_detail_content;//详情
	private Button category_details_delete_category_btn;//删除按钮
	private Gallery gallery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_details);
		initView();
		initData();
	}

	@Override
	public void initView() {
		super.initView();
		category_detail_image = (ImageView) findViewById(R.id.category_detail_image);
		category_detail_image.setOnClickListener(this);
		category_detail_image1 = (ImageView) findViewById(R.id.category_detail_image1);
		category_detail_image1.setOnClickListener(this);
		category_detail_deleteimage1 = (ImageView) findViewById(R.id.category_detail_deleteimage1);
		category_detail_deleteimage1.setOnClickListener(this);
		
		category_detail_title = (EditText)findViewById(R.id.category_detail_title);
		category_detail_content = (EditText)findViewById(R.id.category_detail_content);
		category_details_delete_category_btn = (Button)findViewById(R.id.category_details_delete_category_btn);
		category_details_delete_category_btn.setOnClickListener(this);
		//取到注册成功后的用户信息
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		classify_id = data.getInt("classify_id");
	}

	private void initData() {
		title.setText("果蔬生鲜");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setVisibility(View.VISIBLE);
		right_text.setText("完成");
		category_detail_image.setVisibility(View.GONE);
		
		querybyid();
	}
	
	//根据id查询分类信息
	private void querybyid(){
		String url = HttpUtil.BASE_URL + "/classify/querybyid.do?classify_id="+String.valueOf(classify_id);
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				return;
			}
			ClassifyInfo classifyInfo = JsonUtil.parse2Object(json, ClassifyInfo.class);
			if(classifyInfo == null){
				CommonsUtil.showShortToast(getApplicationContext(), "没有查询到该分类信息");
				finish();
				return;
			}
			title.setText(classifyInfo.getName());
			category_detail_title.setText(classifyInfo.getName());
			category_detail_content.setText(classifyInfo.getDesc());
			
			//查询图片信息
			gallery = queryByClassifyId();
			if(gallery != null){
				//再从7牛上获取图片
				String imageUrl = QiNiuUtil.getImageUrl(gallery.getFile_name());
				getImageToView(imageUrl);
			}
		} catch (InterruptedException e) {
			Log.e(TAG, "查询分类信息失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "查询分类信息失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "查询分类信息失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "查询分类信息失败");
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.category_detail_image://添加图片事件
			showImageChoose();
			break;
		case R.id.category_detail_image1:
			if(gallery != null){
				deleteGallery(gallery.getGallery_id()+"");
			}
			
			category_detail_image.setVisibility(View.VISIBLE);
			category_detail_deleteimage1.setVisibility(View.GONE);
			category_detail_image1.setVisibility(View.GONE);
			break;
		case R.id.dialog_album:
			imageChooseDialog.dismiss();
			CommonsUtil.opengallry(CategoryDetailsActivity.this);
			break;
		case R.id.dialog_camera:
			CommonsUtil.openCamera(CategoryDetailsActivity.this);
			break;
		case R.id.dialog_cancel:
			imageChooseDialog.dismiss();
			break;
		case R.id.right_text:
			attempFinish();
			break;
		case R.id.category_details_delete_category_btn:
			deleteClassify();
			break;
		default:
			break;
		}
	}
	//编辑完成
	private void attempFinish(){
		String url = HttpUtil.BASE_URL + "/classify/update.do";
		try {
			ClassifyInfo classifyInfo = new ClassifyInfo();
			classifyInfo.setClassify_id(classify_id);
			classifyInfo.setName(category_detail_title.getText().toString());
			classifyInfo.setDesc(category_detail_content.getText().toString());
			classifyInfo.setClassify_type("1");
			
			String json = HttpUtil.postRequest(url,classifyInfo);
			if(json == null){
				CommonsUtil.showShortToast(getApplicationContext(), "更新分类信息失败");
				return;
			}
			CommonsUtil.showShortToast(getApplicationContext(), json);
//			startActivity(new Intent(CategoryDetailsActivity.this, CategoryActivity.class));
			finish();
		} catch (InterruptedException e) {
			Log.e(TAG, "更新分类信息失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "更新分类信息失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "更新分类信息失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "更新分类信息失败");
		}
	}
	//删除类别
	private void deleteClassify(){
		String url = HttpUtil.BASE_URL + "/classify/delete.do?classify_id="+String.valueOf(classify_id);
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showShortToast(getApplicationContext(), "删除分类信息失败");
				return;
			}
			CommonsUtil.showShortToast(getApplicationContext(), json);
//			startActivity(new Intent(CategoryDetailsActivity.this, CategoryActivity.class));
			finish();
		} catch (InterruptedException e) {
			Log.e(TAG, "删除分类信息失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "删除分类信息失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "删除分类信息失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "删除分类信息失败");
		}
	}

	private void showImageChoose() {
		imageChooseDialog = new AlertDialog.Builder(
				CategoryDetailsActivity.this).create();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.IMAGE_REQUEST_CODE://打开相册后的回调
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
			Cursor cursor = CategoryDetailsActivity.this.getContentResolver()
					.query(selectImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			ImageName = picturePath.substring(picturePath.lastIndexOf("."),
					picturePath.length());
			ImageName = "test123" + ImageName;
			cursor.close();
			ImageUtil.startPhotoZoom(data.getData(),
					CategoryDetailsActivity.this);
			break;
		case Constants.CAMERA_REQUEST_CODE://打开拍照后的回调
			if (CommonsUtil.hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ Constants.IMAGE_FILE_NAME);
				ImageUtil.startPhotoZoom(Uri.fromFile(tempFile),
						CategoryDetailsActivity.this);
			} else {
				Toast.makeText(CategoryDetailsActivity.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_LONG).show();
			}

			break;
		case Constants.RESULT_REQUEST_CODE:
			if (data != null) {
				getImageToView(data, CategoryDetailsActivity.this);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 获取网络图片
	 * @param url
	 */
	private void getImageToView(String url){
		try {
			URL picUrl = new URL(url);
			Bitmap bitmap = BitmapFactory.decodeStream(picUrl.openStream()); 
			category_detail_image1.setImageBitmap(bitmap);//已成功添加的图标
		} catch (FileNotFoundException e) {
			Log.e(TAG,"文件找不到", e);
		} catch (IOException e) {
			Log.e(TAG,"IO异常", e);
		}
		category_detail_image.setVisibility(View.GONE);//添加图标隐藏
		category_detail_deleteimage1.setVisibility(View.VISIBLE);//删除图标显示
		category_detail_image1.setVisibility(View.VISIBLE);//显示已添加的图标
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
			category_detail_image1.setImageBitmap(photo);//已成功添加的图标
			category_detail_image.setVisibility(View.GONE);//添加图标隐藏
			category_detail_deleteimage1.setVisibility(View.VISIBLE);//删除图标显示
			category_detail_image1.setVisibility(View.VISIBLE);//显示已添加的图标
			
			if(classify_id <= 0){//如果没有分类ID，就不保存图片了。
				return;
			}
			//上传图片到7牛
			File image = ImageUtil.bitmap2file(CategoryDetailsActivity.this, photo);
			if(image == null){
				CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
				return;
			}
			PutRet putRet = QiNiuUtil.resumeUploadFile(image.getName(), image);
			if(!putRet.ok()){
				CommonsUtil.showShortToast(getApplicationContext(), "保存图片到服务器失败");
//				Log.e("", putRet.get);
				return;
			}
			
			CommonsUtil.showShortToast(getApplicationContext(), "更新图片成功");
			
			//获取7牛上的文件名和路径保存到数据库中
			boolean isAdd = false;
			if(gallery == null){//新增图片数据
				gallery = new Gallery();
				isAdd = true;
			}
			gallery.setClassify_id(classify_id);
			gallery.setFile_name(image.getName());
			gallery.setName(image.getName());
			
			if(isAdd){
				addGallery(gallery);
			}else{
				updateGallery(gallery);
			}
		}
	}
	
	//根据分类Id查询图片信息
	private Gallery queryByClassifyId(){
		String url = HttpUtil.BASE_URL + "/gallery/queryByClassifyId.do?classify_id="+String.valueOf(classify_id);
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showShortToast(getApplicationContext(), "查询分类图片失败");
				return null;
			}
			
			Gallery gallery = JsonUtil.parse2Object(json, Gallery.class);
			if(gallery == null){
				CommonsUtil.showShortToast(getApplicationContext(), "转换图片Json失败");
				return null;
			}
			
			return gallery;
		} catch (InterruptedException e) {
			Log.e(TAG, "根据分类Id查询图片信息失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "根据分类Id查询图片信息失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "根据分类Id查询图片信息失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "根据分类Id查询图片信息失败");
		}
		
		return null;
	}
	//新增图片信息
	private void addGallery(Gallery gallery){
		String url = HttpUtil.BASE_URL + "/gallery/add.do";
		try {
			String json = HttpUtil.postRequest(url, gallery);
			if(json == null){
				CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
				return;
			}
			CommonsUtil.showShortToast(getApplicationContext(), json);
			
		} catch (InterruptedException e) {
			Log.e(TAG, "更新图片失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "更新图片失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
		}
	}
	//更新图片信息
	private void updateGallery(Gallery gallery){
		String url = HttpUtil.BASE_URL + "/gallery/update.do";
		try {
			String json = HttpUtil.postRequest(url, gallery);
			if(json == null){
				CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
				return;
			}
			CommonsUtil.showShortToast(getApplicationContext(), json);
			
		} catch (InterruptedException e) {
			Log.e(TAG, "更新图片失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "更新图片失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
		}
	}
	//删除图片信息
	private void deleteGallery(String gallery_id){
		String url = HttpUtil.BASE_URL + "/gallery/delete.do?gallery_id="+gallery_id;
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showShortToast(getApplicationContext(), "删除图片失败");
				return;
			}
			CommonsUtil.showShortToast(getApplicationContext(), json);
			
			QiNiuUtil.deleteFile(gallery.getFile_name());
		} catch (InterruptedException e) {
			Log.e(TAG, "删除图片失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "删除图片失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "删除图片失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "删除图片失败");
		}
	}
	
	
}
