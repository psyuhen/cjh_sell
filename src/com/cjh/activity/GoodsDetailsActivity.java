package com.cjh.activity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjh.adapter.AddImageAdapter;
import com.cjh.bean.AddImage;
import com.cjh.bean.Gallery;
import com.cjh.bean.MerchInfo;
import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.ImageUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.QiNiuUtil;
import com.cjh.utils.Validator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qiniu.api.io.PutRet;
/**
 * 商品明细
 * @author ps
 *
 */
public class GoodsDetailsActivity extends BaseTwoActivity {
	public static final String TAG = "GoodsDetailsActivity";
	// 添加图片对话框
	private AlertDialog imageChooseDialog = null;
	// 添加图片
	private ImageView content_add_image;
	// 获取的图片和路径
	private String picturePath;
	private String ImageName;
	private GridView gridView;
	// gridview适配器
	private AddImageAdapter adapter;
	private List<AddImage> lists;
	private List<AddImage> old_lists;
	
	private EditText goods_detail_title;//标题
	private EditText goods_detail_content;//内容
	private EditText goods_detail_price;//价格
	private EditText goods_detail_stock;//库存
	private EditText goods_detail_type;//规格
	
	private Button goods_details_delete_goods_btn;//删除商品
	private Button goods_details_offshelf_btn;//下架商品
	
	private int merch_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_details);
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
		old_lists = new ArrayList<AddImage>();
		
		goods_detail_title = (EditText) findViewById(R.id.goods_detail_title);
		goods_detail_content = (EditText) findViewById(R.id.goods_detail_content);
		goods_detail_price = (EditText) findViewById(R.id.goods_detail_price);
		goods_detail_stock = (EditText) findViewById(R.id.goods_detail_stock);
		goods_detail_type = (EditText) findViewById(R.id.goods_detail_type);

		goods_details_delete_goods_btn = (Button)findViewById(R.id.goods_details_delete_goods_btn);
		goods_details_delete_goods_btn.setOnClickListener(this);
		goods_details_offshelf_btn = (Button)findViewById(R.id.goods_details_offshelf_btn);
		goods_details_offshelf_btn.setOnClickListener(this);
	}

	private void initData() {
		title.setText("编辑物品");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setVisibility(View.VISIBLE);
		right_text.setText("完成");
		lists.add(new AddImage(1, BitmapFactory.decodeResource(getResources(),
				R.drawable.pg1)));
		lists.add(new AddImage(2, BitmapFactory.decodeResource(getResources(),
				R.drawable.pg2)));
		lists.add(new AddImage(3, BitmapFactory.decodeResource(getResources(),
				R.drawable.pg3)));
		lists.add(new AddImage(4, BitmapFactory.decodeResource(getResources(),
				R.drawable.pg4)));
		adapter = new AddImageAdapter(GoodsDetailsActivity.this, lists);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long len) {
				
				AddImage addImage = lists.get(position);
				lists.remove(addImage);
				adapter.notifyDataSetChanged();
			}
		});
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		merch_id = extras.getInt("merch_id");
		
		queryGoods();
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
			CommonsUtil.opengallry(GoodsDetailsActivity.this);
			break;
		case R.id.dialog_camera:
			CommonsUtil.openCamera(GoodsDetailsActivity.this);
			break;
		case R.id.dialog_cancel:
			imageChooseDialog.dismiss();
			break;
		case R.id.right_text:
			updateGoods();
			break;
		case R.id.goods_details_delete_goods_btn:
			deleteGoods();
			break;
		case R.id.goods_details_offshelf_btn:
			offshelfGoods();
			break;
		default:
			break;
		}
	}
	
	//更新商品
	private void updateGoods(){
		// 清空错误
		goods_detail_title.setError(null);
		goods_detail_content.setError(null);
		goods_detail_price.setError(null);
		goods_detail_stock.setError(null);
		goods_detail_type.setError(null);
		
		String title = goods_detail_title.getText().toString();//标题
		String content = goods_detail_content.getText().toString();//正文内容
		String price = goods_detail_price.getText().toString();//价格
		String stock = goods_detail_stock.getText().toString();//库存
//		String type = goods_detail_type.getText().toString();//分类
		
		if(TextUtils.isEmpty(title)){
			goods_detail_title.setError(getString(R.string.error_field_required));
			return;
		}
		
		if(TextUtils.isEmpty(content)){
			goods_detail_content.setError(getString(R.string.error_field_required));
			return;
		}
		
		if(!TextUtils.isEmpty(price) && !Validator.isNumber(price)){
			goods_detail_price.setError(getString(R.string.error_invalid_number));
			return;
		}
		
		if(!TextUtils.isEmpty(stock) && !Validator.isDigits(stock)){
			goods_detail_stock.setError(getString(R.string.error_invalid_number));
			return;
		}
		
		MerchInfo merchInfo = new MerchInfo();
		merchInfo.setMerch_id(merch_id);
		merchInfo.setName(title);
		merchInfo.setDesc(content);
		merchInfo.setIn_stock(Integer.valueOf(stock));
		merchInfo.setPrice(Float.valueOf(price));
		//merchInfo.setClassify_id(Integer.valueOf(type));
		
		
		String url = HttpUtil.BASE_URL + "/merch/modify.do";
		try {
			String json = HttpUtil.postRequest(url, merchInfo);
			if(json != null){
				CommonsUtil.showShortToast(GoodsDetailsActivity.this, "更新成功");
				
				//先删除图片，再重新保存吧
				if(lists != null && !lists.isEmpty()){
					for (AddImage addImage: old_lists) {
						deleteGallery(addImage.getFileName(), addImage.getId()+"");
					}
					for (AddImage addImage: lists) {
						File image = addImage.getFile();
						PutRet putRet = QiNiuUtil.resumeUploadFile(image.getName(), image);
						if(!putRet.ok()){
							Log.e(TAG, putRet.getResponse(), putRet.getException());
						}else{
							Gallery gallery = new Gallery();
							gallery.setMerch_id(merch_id);
							gallery.setFile_name(image.getName());
							gallery.setName(image.getName());
							addGallery(gallery);
						}
					}
				}
				
				
				finish();
			}else{
				CommonsUtil.showShortToast(GoodsDetailsActivity.this, "更新失败");
			}
		} catch (InterruptedException e) {
			Log.e(TAG, "更新商品失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "更新商品失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "更新商品失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "更新商品失败");
		}
		
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
	//删除图片信息
	private void deleteGallery(String fileName,String gallery_id){
		String url = HttpUtil.BASE_URL + "/gallery/delete.do";
		try {
			Map<String,String> map = new HashMap<String, String>();
			map.put("gallery_id", gallery_id);
			String json = HttpUtil.postRequest(url,map);
			if(json == null){
				CommonsUtil.showShortToast(getApplicationContext(), "删除图片失败");
				return;
			}
			CommonsUtil.showShortToast(getApplicationContext(), json);
			
			QiNiuUtil.deleteFile(fileName);
		} catch (InterruptedException e) {
			Log.e(TAG, "删除图片失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "删除图片失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "删除图片失败", e);
			CommonsUtil.showShortToast(getApplicationContext(), "删除图片失败");
		}
	}

	//根据商品ID查询商品信息
	private void queryGoods(){
		String url = HttpUtil.BASE_URL + "/merch/querybyid.do?merch_id="+merch_id;
		
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "查询商品列表失败");
				return;
			}
			
			Log.e(TAG, json);
			
			MerchInfo merchInfo = JsonUtil.parse2Object(json, MerchInfo.class);
			goods_detail_title.setText(merchInfo.getName());
			goods_detail_content.setText(merchInfo.getDesc());
			goods_detail_price.setText(merchInfo.getPrice() + "");
			goods_detail_stock.setText(merchInfo.getIn_stock() + "");
			goods_detail_type.setText(merchInfo.getClassify_id()+"");
			
			//查询图片
			lists.clear();
			adapter.notifyDataSetChanged();
			
			//查询图片
			url = HttpUtil.BASE_URL + "/gallery/queryByMerchId.do?merch_id="+merch_id;
			json = HttpUtil.getRequest(url);
			if(json != null){
				List<Gallery> galleries = JsonUtil.parse2ListObject(json, new TypeReference<List<Gallery>>() {});
				if(galleries != null && !galleries.isEmpty()){
					for (Gallery gallery : galleries) {
						String imageUrl = QiNiuUtil.getImageUrl(gallery.getFile_name());
						getImageToView(gallery.getGallery_id(),imageUrl,gallery.getFile_name());
					}
				}
			}
			
		} catch (InterruptedException e) {
			Log.e(TAG, "查询merch_id为["+merch_id+"]商品信息失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询商品信息失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "查询merch_id为["+merch_id+"]商品信息失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "查询商品信息失败");
		}
	}
	
	//删除商品
	private void deleteGoods(){
		String url = HttpUtil.BASE_URL + "/merch/deletebyid.do?merch_id="+merch_id;
		
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showLongToast(getApplicationContext(), "删除商品失败");
				return;
			}
			
			CommonsUtil.showLongToast(getApplicationContext(), json);
			startActivity(new Intent(GoodsDetailsActivity.this, GoodsActivity.class));
		} catch (InterruptedException e) {
			Log.e(TAG, "删除商品失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "删除商品失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "删除商品失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "删除商品失败");
		}
	}
	
	//下架商品
	private void offshelfGoods(){
		MerchInfo merchInfo = new MerchInfo();
		merchInfo.setOut_published("1");
		merchInfo.setMerch_id(merch_id);
		
		String url = HttpUtil.BASE_URL + "/merch/modify.do";
		try {
			String json = HttpUtil.postRequest(url, merchInfo);
			if(json != null){
				CommonsUtil.showShortToast(GoodsDetailsActivity.this, "下架商品成功");
				startActivity(new Intent(GoodsDetailsActivity.this, GoodsActivity.class));
			}
		} catch (InterruptedException e) {
			Log.e(TAG, "下架商品失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "下架商品失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "下架商品失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "下架商品失败");
		}
		
	}
	private void showImageChoose() {
		
		imageChooseDialog = new AlertDialog.Builder(GoodsDetailsActivity.this)
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
			Cursor cursor = GoodsDetailsActivity.this.getContentResolver()
					.query(selectImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			ImageName = picturePath.substring(picturePath.lastIndexOf("."),
					picturePath.length());
			ImageName = "test123" + ImageName;
			cursor.close();
			ImageUtil.startPhotoZoom(data.getData(), GoodsDetailsActivity.this);
			break;
		case Constants.CAMERA_REQUEST_CODE:
			if (CommonsUtil.hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ Constants.IMAGE_FILE_NAME);
				ImageUtil.startPhotoZoom(Uri.fromFile(tempFile),
						GoodsDetailsActivity.this);
			} else {
				Toast.makeText(GoodsDetailsActivity.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_LONG).show();
			}

			break;
		case Constants.RESULT_REQUEST_CODE:
			if (data != null) {
				getImageToView(data, GoodsDetailsActivity.this);
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
			if (lists.size()<6) {
				AddImage addImage = new AddImage();
				addImage.setBitmap(photo);
				
				lists.add(addImage);
				adapter.notifyDataSetChanged();
				
				File image = ImageUtil.bitmap2file(GoodsDetailsActivity.this, photo);
				if(image != null){
					addImage.setFileName(image.getName());
				}
			} else{
				CommonsUtil.showShortToast(GoodsDetailsActivity.this, "最多添加六张图片");
			}
		
		}
	}
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(int gallery_id,String url,String fileName) {
		URL picUrl;
		Bitmap photo;
		try {
			picUrl = new URL(url);
			photo = BitmapFactory.decodeStream(picUrl.openStream());
			AddImage addImage = new AddImage();
			addImage.setId(gallery_id);
			addImage.setBitmap(photo);
			addImage.setFileName(fileName);
			lists.add(addImage);
			old_lists.add(addImage);
			adapter.notifyDataSetChanged();
			
		} catch (MalformedURLException e) {
			Log.e(TAG,"url不正确", e);
		} catch (IOException e) {
			Log.e(TAG,"IO异常", e);
		} 
	}
}
