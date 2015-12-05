package com.cjh.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjh.adapter.AddImageAdapter;
import com.cjh.adapter.CommonAdapter;
import com.cjh.adapter.ViewHolder;
import com.cjh.bean.AddImage;
import com.cjh.bean.CategoryItem;
import com.cjh.bean.ClassifyInfo;
import com.cjh.bean.Gallery;
import com.cjh.bean.MerchInfo;
import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.FileUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.ImageUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.QiNiuUtil;
import com.cjh.utils.Validator;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
/**
 * 商品明细
 * @author ps
 *
 */
public class GoodsDetailsActivity extends BaseTwoActivity {
	private Logger LOGGER = LoggerFactory.getLogger(GoodsDetailsActivity.class);
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
	
//	private Button goods_details_pubilsh_goods_btn;//发布商品
	private Button goods_details_delete_goods_btn;//删除商品
	private Button goods_details_offshelf_btn;//下架商品
	
	private int merch_id;
	private int classify_id;
	private String out_published;
	
	// 类别选择
	private RelativeLayout goods_detail_category_rl;
	//类别数据
	private List<CategoryItem> categoryList;
	//类别显示
	private TextView goods_detail_category;
	private CommonAdapter<CategoryItem> categoryAdapter;//分类
//	private String from;

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

//		goods_details_pubilsh_goods_btn = (Button)findViewById(R.id.goods_details_pubilsh_goods_btn);
//		goods_details_pubilsh_goods_btn.setOnClickListener(this);
		goods_details_delete_goods_btn = (Button)findViewById(R.id.goods_details_delete_goods_btn);
		goods_details_delete_goods_btn.setOnClickListener(this);
		goods_details_offshelf_btn = (Button)findViewById(R.id.goods_details_offshelf_btn);
		goods_details_offshelf_btn.setOnClickListener(this);
		
		goods_detail_category_rl = (RelativeLayout) findViewById(R.id.goods_detail_category_rl);
		goods_detail_category_rl.setOnClickListener(this);
		goods_detail_category = (TextView) findViewById(R.id.goods_detail_category);
		categoryList = new ArrayList<CategoryItem>();
	}

	private void initData() {
		title.setText("商品编辑");
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
		out_published = extras.getString("out_published");
//		from = extras.getString("from");
		
		setPublishedText();
		
		queryclassify();
	}
	
	private void setPublishedText(){
		if("0".equals(out_published)){//未下架
			goods_details_offshelf_btn.setText(getString(R.string.goods_details_offshelft_btn));
//			goods_details_pubilsh_goods_btn.setVisibility(View.VISIBLE);
		}else if("1".equals(out_published)){//已下架
//			goods_details_pubilsh_goods_btn.setVisibility(View.GONE);
			goods_details_offshelf_btn.setText(getString(R.string.goods_details_onshelft_btn));
		}
	}
	
	//查询分类
	private List<ClassifyInfo> queryclassify2() {
		int user_id = sessionManager.getUserId();
		String url = HttpUtil.BASE_URL + "/classify/querybyuserid.do?user_id="+user_id;
		try {
			String jsons = HttpUtil.getRequest(url);
			if(jsons == null){
				CommonsUtil.showShortToast(getApplicationContext(), "查询分类信息失败");
				return null;
			}
			List<ClassifyInfo> list = JsonUtil.parse2ListClassifyInfo(jsons);
			if(list == null){
				LOGGER.error(">>> 转换分类列表信息失败");
				return null;
			}
			categoryList.clear();
			for (ClassifyInfo classifyInfo : list) {
				CategoryItem categoryItem=new CategoryItem();
				categoryItem.setId(classifyInfo.getClassify_id());
				categoryItem.setNum(classifyInfo.getClassify_num());
				categoryItem.setTitle(classifyInfo.getName());
				
				categoryList.add(categoryItem);
			}
			
			return list;
		} catch (Exception e) {
			LOGGER.error(">>> 查询分类列表失败",e);
			CommonsUtil.showShortToast(getApplicationContext(), "查询分类列表失败");
		}
		
		return null;
	}
	
	//查询分类信息
	private void queryclassify() {
		queryclassify2();
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
		case R.id.goods_details_delete_goods_btn://删除
			deleteGoods();
			break;
		case R.id.goods_details_offshelf_btn://下架或者上架
			offshelfGoods();
			break;
		case R.id.goods_details_pubilsh_goods_btn://发布
//			publishGoods();
			break;
		case R.id.goods_detail_category_rl:
			showCategoryList();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(categoryAdapter != null){
			queryclassify2();
			categoryAdapter.notifyDataSetChanged();
		}
	}
	
	//分类类别
	private void showCategoryList() {
		final AlertDialog categoryChooseDialog = new AlertDialog.Builder(GoodsDetailsActivity.this).create();
		categoryChooseDialog.show();
		categoryChooseDialog.getWindow().setContentView(R.layout.pop_category);
		ListView category_listview = (ListView) categoryChooseDialog.findViewById(R.id.category_listview);
		Button add_category = (Button) categoryChooseDialog.findViewById(R.id.add_category_btn);//新增类型
		categoryAdapter = showAdater();
		category_listview.setAdapter(categoryAdapter);
		category_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long len) {
				categoryChooseDialog.dismiss();
				CategoryItem item = (CategoryItem)parent.getItemAtPosition(position);
				goods_detail_category.setText(item.getTitle());
				classify_id = item.getId();
			}
		});
		add_category.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(GoodsDetailsActivity.this, CategoryAddActivity.class));
			}
		});
	}

	public CommonAdapter<CategoryItem> showAdater() {
		return new CommonAdapter<CategoryItem>(GoodsDetailsActivity.this,
				categoryList, R.layout.item_add_category) {

			@Override
			public void convert(ViewHolder helper, CategoryItem item) {
				helper.setText(R.id.item_add_category_title, item.getTitle());
			}
		};

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
		String type = goods_detail_type.getText().toString();//分类
		
		String required = getString(R.string.error_field_required);
		if(TextUtils.isEmpty(title)){
			goods_detail_title.setError("标题" + required);
			CommonsUtil.showShortToast(getApplicationContext(), "标题" + required);
			return;
		}
		
		if(TextUtils.isEmpty(content)){
			goods_detail_content.setError("内容" + required);
			CommonsUtil.showShortToast(getApplicationContext(), "内容" + required);
			return;
		}
		MerchInfo merchInfo = new MerchInfo();
		
		if(!TextUtils.isEmpty(price)){
			if(Validator.isNumber(price)){
				merchInfo.setPrice(Float.valueOf(price));
			}else{
				String error_price = getString(R.string.error_invalid_price);
				goods_detail_price.setError(error_price);
				CommonsUtil.showShortToast(getApplicationContext(), error_price);
				return;
			}
		}
		if(!TextUtils.isEmpty(stock)){
			if(Validator.isDigits(stock)){
				merchInfo.setIn_stock(Integer.valueOf(stock));
			}else{
				String error_stock = getString(R.string.error_invalid_stock);
				goods_detail_stock.setError(error_stock);
				CommonsUtil.showShortToast(getApplicationContext(), error_stock);
				return;
			}
		}
		
		if(!TextUtils.isEmpty(type)){
			if(type.length() > 10){
				goods_detail_type.setError("单位输入超过10位");
				CommonsUtil.showShortToast(getApplicationContext(), "单位输入超过10位");
				return;
			}
		}
		
		merchInfo.setMerch_id(merch_id);
		merchInfo.setName(title);
		merchInfo.setDesc(content);
		merchInfo.setClassify_id(classify_id);
		merchInfo.setUnit(type);
		
		
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
					int user_id = sessionManager.getUserId();
					for (AddImage addImage: lists) {
						File image = addImage.getFile();
						if(image == null){
							continue;
						}
						QiNiuUtil.resumeUploadFile(image.getName(), image, String.valueOf(user_id), new UpCompletionHandler() {
							@Override
							public void complete(String key, ResponseInfo info, JSONObject jsonObj) {
								if(info.statusCode == HttpStatus.OK.value()){
									LOGGER.info("上传图片成功！");
									//CommonsUtil.showShortToast(getApplicationContext(), "更新图片成功");
									Gallery gallery = new Gallery();
									gallery.setMerch_id(merch_id);
									gallery.setFile_name(key);
									gallery.setName(key);
									addGallery(gallery);
								}else{
									//CommonsUtil.showShortToast(getApplicationContext(), "保存图片到服务器失败");
									LOGGER.error(">>> 保存图片到服务器失败"+ info.error);
								}
							}
						});
					}
				}
				
				setResult(RESULT_OK,new Intent());
				finish();
			}else{
				CommonsUtil.showShortToast(GoodsDetailsActivity.this, "更新失败");
			}
		} catch (Exception e) {
			CommonsUtil.showLongToast(getApplicationContext(), "更新商品失败");
			LOGGER.error(">>> 更新商品失败",e);
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
			
		} catch (Exception e) {
			CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
			LOGGER.error(">>> 更新图片失败",e);
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
				//CommonsUtil.showShortToast(getApplicationContext(), "删除图片失败");
				return;
			}
			//CommonsUtil.showShortToast(getApplicationContext(), json);
			
			QiNiuUtil.deleteFile(fileName);
		} catch (Exception e) {
			CommonsUtil.showShortToast(getApplicationContext(), "删除图片失败");
			LOGGER.error(">>> 删除图片失败",e);
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
			
			MerchInfo merchInfo = JsonUtil.parse2Object(json, MerchInfo.class);
			goods_detail_title.setText(merchInfo.getName());
			goods_detail_content.setText(merchInfo.getDesc());
			goods_detail_price.setText(merchInfo.getPrice() + "");
			goods_detail_stock.setText(merchInfo.getIn_stock() + "");
			goods_detail_type.setText(merchInfo.getUnit());
			out_published = merchInfo.getOut_published();
			setPublishedText();
			
			int classifyId = merchInfo.getClassify_id();
			for (int i = 0; i < categoryList.size(); i++) {
				CategoryItem categoryItem = categoryList.get(i);
				if(categoryItem.getId() == classifyId && classifyId != 0){
					goods_detail_category.setText(categoryItem.getTitle());
					classify_id = classifyId;
					break;
				}
			}
			
			
			//查询图片
			lists.clear();
			adapter.notifyDataSetChanged();
			
			//查询图片
			url = HttpUtil.BASE_URL + "/gallery/queryByMerchId.do?merch_id="+merch_id;
			json = HttpUtil.getRequest(url);
			if(json != null){
				List<Gallery> galleries = JsonUtil.parse2ListGallery(json);
				if(galleries != null && !galleries.isEmpty()){
					int length = galleries.size();
					for (int i = 0; i < length; i++) {
						Gallery gallery = galleries.get(i);
						getImageToView(gallery.getGallery_id(),gallery.getFile_name());
					}
				}
			}
			
		} catch (Exception e) {
			CommonsUtil.showLongToast(getApplicationContext(), "查询商品信息失败");
			LOGGER.error(">>> 查询商品信息失败",e);
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
			setResult(RESULT_OK,new Intent());
			finish();
		} catch (Exception e) {
			CommonsUtil.showLongToast(getApplicationContext(), "删除商品失败");
			LOGGER.error(">>> 删除商品失败",e);
		}
	}
	
	//下架或者上架商品
	private void offshelfGoods(){
		MerchInfo merchInfo = new MerchInfo();
		
		String msg = "下架";
		if("0".equals(out_published)){
			merchInfo.setOut_published("1");
			msg = "下架";
		}else if("1".equals(out_published)){
			merchInfo.setOut_published("0");
			msg = "上架";
		}
		
		merchInfo.setMerch_id(merch_id);
		
		String url = HttpUtil.BASE_URL + "/merch/modify.do";
		try {
			String json = HttpUtil.postRequest(url, merchInfo);
			
			if(json == null || "更新商品失败!".equals(json)){
				CommonsUtil.showShortToast(GoodsDetailsActivity.this, msg + "商品失败");
				return ;
			}
			CommonsUtil.showShortToast(GoodsDetailsActivity.this, msg + "商品成功");
			setResult(RESULT_OK,new Intent());
			finish();
		} catch (Exception e) {
			CommonsUtil.showLongToast(getApplicationContext(), msg + "商品失败");
			LOGGER.error(">>> 下架商品失败",e);
		}
		
	}
	//发布商品
	/*private void publishGoods(){
		MerchInfo merchInfo = new MerchInfo();
		merchInfo.setPublished_date(DateUtil.currentTime());
		merchInfo.setOut_published("0");
		merchInfo.setMerch_id(merch_id);
		
		String url = HttpUtil.BASE_URL + "/merch/modify.do";
		try {
			String json = HttpUtil.postRequest(url, merchInfo);
			
			if(json == null || "更新商品失败!".equals(json)){
				CommonsUtil.showShortToast(GoodsDetailsActivity.this, "发布商品失败");
				return ;
			}
			CommonsUtil.showShortToast(GoodsDetailsActivity.this, "发布商品成功");
			finish();
		} catch (Exception e) {
			CommonsUtil.showLongToast(getApplicationContext(), "发布商品失败");
			LOGGER.error(">>> 发布商品失败",e);
		}
		
	}*/
	private void showImageChoose() {
		/*imageChooseDialog = new AlertDialog.Builder(GoodsDetailsActivity.this).create();
		imageChooseDialog.show();
		imageChooseDialog.getWindow().setContentView(R.layout.image_choose_dialog);
		Button dialog_album = (Button) imageChooseDialog.findViewById(R.id.dialog_album);
		dialog_album.setOnClickListener(this);
		Button dialog_camera = (Button) imageChooseDialog.findViewById(R.id.dialog_camera);
		dialog_camera.setOnClickListener(this);
		Button dialog_cancel = (Button) imageChooseDialog.findViewById(R.id.dialog_cancel);
		dialog_cancel.setOnClickListener(this);*/
		
		if(imageChooseDialog == null){
			imageChooseDialog = ImageUtil.showImageChoose(GoodsDetailsActivity.this);
		}else {
			imageChooseDialog.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.IMAGE_REQUEST_CODE://相册 
			if(data == null){
				break;
			}
			Uri selectImage = data.getData();
			if (selectImage != null) {
				String uriStr = selectImage.toString();
				String path = uriStr.substring(10, uriStr.length());
				if (path.startsWith("com.sec.android.gallery3d")) {
					CommonsUtil.showLongToast(getApplicationContext(), "It's auto backup pic path:" + selectImage.toString());
				}
			}
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = GoodsDetailsActivity.this.getContentResolver()
					.query(selectImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			ImageName = picturePath.substring(picturePath.lastIndexOf("."), picturePath.length());
			ImageName = "test123" + ImageName;
			cursor.close();
			ImageUtil.startPhotoZoom(data.getData(), GoodsDetailsActivity.this);
			break;
		case Constants.CAMERA_REQUEST_CODE://相机
			if (CommonsUtil.hasSdcard()) {
				File tempFile = FileUtil.getAppFolderFile(Constants.IMAGE_FILE_NAME);
				ImageUtil.startPhotoZoom(Uri.fromFile(tempFile), GoodsDetailsActivity.this);
			} else {
				CommonsUtil.showLongToast(getApplicationContext(), "未找到存储卡，无法存储照片！");
			}

			break;
		case Constants.RESULT_REQUEST_CODE:
			if (data != null) {
				getImageToView(data, GoodsDetailsActivity.this);
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
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			if (lists.size()<6) {
				AddImage addImage = new AddImage();
				addImage.setBitmap(photo);
				
				File image = ImageUtil.bitmap2file(photo);
				if(image != null){
					addImage.setFileName(image.getName());
					addImage.setFile(image);
				}
				
				lists.add(addImage);
				adapter.notifyDataSetChanged();
				
				
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
	private void getImageToView(int gallery_id,String fileName) {
		Bitmap photo = FileUtil.getCacheFile(fileName);
		lists.add(new AddImage(gallery_id, fileName, photo));
		old_lists.clear();
		old_lists.add(new AddImage(gallery_id, fileName, photo));
		adapter.notifyDataSetChanged();
	}
}
