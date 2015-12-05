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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * 添加商品
 * @author ps
 *
 */
public class GoodsAddActivity extends BaseTwoActivity {
	private Logger LOGGER = LoggerFactory.getLogger(GoodsAddActivity.class);
	// 添加图片对话框
	private AlertDialog imageChooseDialog = null;
	// 添加图片
	private ImageView content_add_image;
	// 获取的图片和路径
	private String picturePath;
	private String ImageName;
	// 图片添加管理
	private GridView gridView;
	// gridview适配器
	private AddImageAdapter adapter;
	private List<AddImage> lists;
	
	private EditText pop_title_edit;//标题
	private EditText pop_content_edit;//正文内容
	private EditText pop_price_edit;//价格
	private EditText pop_stock_edit;//库存
	private EditText pop_type_edit;//单位
	
	// 添加类别对话框
	private RelativeLayout goods_category_ll;
	private Button goods_add_btn;
	private TextView add_goods_category;
	private List<CategoryItem> categoryList;
	private int classify_id;
	private CommonAdapter<CategoryItem> categoryAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_goods);
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
		
		pop_title_edit = (EditText) findViewById(R.id.pop_title_edit);
		pop_content_edit = (EditText) findViewById(R.id.pop_content_edit);
		pop_price_edit = (EditText) findViewById(R.id.pop_price_edit);
		pop_stock_edit = (EditText) findViewById(R.id.pop_stock_edit);
		pop_type_edit = (EditText) findViewById(R.id.pop_type_edit);
		
		goods_category_ll = (RelativeLayout) findViewById(R.id.goods_category_ll);
		goods_category_ll.setOnClickListener(this);
		goods_add_btn = (Button) findViewById(R.id.goods_add_btn);
		goods_add_btn.setOnClickListener(this);
		add_goods_category = (TextView) findViewById(R.id.add_goods_category);
		
		categoryList=new ArrayList<CategoryItem>();
	}

	private void initData() {
		
		title.setText("添加商品");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setVisibility(View.VISIBLE);
		right_text.setText("添加");
		adapter = new AddImageAdapter(GoodsAddActivity.this, lists);
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
		//查询分类数据
		queryclassify();
	}
	private void queryclassify() {
//		String url = HttpUtil.BASE_URL + "/classify/querybytype.do?classify_type="+1;
		int user_id = sessionManager.getUserId();
		String url = HttpUtil.BASE_URL + "/classify/querybyuserid.do?user_id="+user_id;
		try {
			String jsons = HttpUtil.getRequest(url);
			if(jsons == null){
				CommonsUtil.showShortToast(getApplicationContext(), "查询分类信息失败");
				return;
			}
			List<ClassifyInfo> list = JsonUtil.parse2ListClassifyInfo(jsons);
			if(list == null){
				LOGGER.error(">>> 转换分类列表信息失败");
				return;
			}
			categoryList.clear();
			for (ClassifyInfo classifyInfo : list) {
				CategoryItem categoryItem=new CategoryItem();
				categoryItem.setId(classifyInfo.getClassify_id());
				categoryItem.setNum(classifyInfo.getClassify_num());
				categoryItem.setTitle(classifyInfo.getName());
				
				categoryList.add(categoryItem);
			}
		} catch (Exception e) {
			LOGGER.error(">>> 查询分类列表失败",e);
			CommonsUtil.showShortToast(getApplicationContext(), "查询分类列表失败");
		}
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
			CommonsUtil.opengallry(GoodsAddActivity.this);
			break;
		case R.id.dialog_camera:
			imageChooseDialog.dismiss();
			CommonsUtil.openCamera(GoodsAddActivity.this);
			break;
		case R.id.dialog_cancel:
			imageChooseDialog.dismiss();
			break;
		case R.id.right_text:
			addGoods();
			break;
		case R.id.goods_add_btn:
			addGoods();
			break;
		case R.id.goods_category_ll:
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
			queryclassify();
			categoryAdapter.notifyDataSetChanged();
		}
	}

	private void showCategoryList() {
		final AlertDialog categoryChooseDialog = new AlertDialog.Builder(GoodsAddActivity.this).create();
		categoryChooseDialog.show();
		categoryChooseDialog.getWindow().setContentView(R.layout.pop_category);
		ListView category_listview = (ListView) categoryChooseDialog.findViewById(R.id.category_listview);
		Button add_category = (Button) categoryChooseDialog.findViewById(R.id.add_category_btn);
		categoryAdapter = showAdater();
		category_listview.setAdapter(categoryAdapter);
		category_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long len) {
				categoryChooseDialog.dismiss();
				CategoryItem item = (CategoryItem)parent.getItemAtPosition(position);
				add_goods_category.setText(item.getTitle());
				classify_id = item.getId();
			}
		});
		add_category.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(GoodsAddActivity.this, CategoryAddActivity.class));
			}
		});

	}

	public CommonAdapter<CategoryItem> showAdater() {
		return new CommonAdapter<CategoryItem>(GoodsAddActivity.this,
				categoryList, R.layout.item_add_category) {

			@Override
			public void convert(ViewHolder helper, CategoryItem item) {
				helper.setText(R.id.item_add_category_title, item.getTitle());
			}
		};

	}
	
	//添加商品
	private void addGoods(){
		// 清空错误
		pop_title_edit.setError(null);
		pop_content_edit.setError(null);
		
		String title = pop_title_edit.getText().toString();//标题
		String content = pop_content_edit.getText().toString();//正文内容
		String price = pop_price_edit.getText().toString();//价格
		String stock = pop_stock_edit.getText().toString();//价格
		String type = pop_type_edit.getText().toString();//单位
		
		String required = getString(R.string.error_field_required);
		if(TextUtils.isEmpty(title)){
			pop_title_edit.setError("标题" + required);
			CommonsUtil.showShortToast(GoodsAddActivity.this, "标题" + required);
			return;
		}
		
		if(TextUtils.isEmpty(content)){
			pop_content_edit.setError("内容" + required);
			CommonsUtil.showShortToast(GoodsAddActivity.this, "内容" + required);
			return;
		}
		
		int store_id = sessionManager.getInt("store_id");//获取商家ID
		
		MerchInfo merchInfo = new MerchInfo();
		
		if(!TextUtils.isEmpty(price)){
			if(Validator.isNumber(price)){
				merchInfo.setPrice(Float.valueOf(price));
			}else{
				String error_price = getString(R.string.error_invalid_price);
				pop_price_edit.setError(error_price);
				CommonsUtil.showShortToast(GoodsAddActivity.this, error_price);
				return;
			}
		}
		if(!TextUtils.isEmpty(stock)){
			if(Validator.isDigits(stock)){
				merchInfo.setIn_stock(Integer.valueOf(stock));
			}else{
				String error_stock = getString(R.string.error_invalid_stock);
				pop_stock_edit.setError(error_stock);
				CommonsUtil.showShortToast(GoodsAddActivity.this, error_stock);
				return;
			}
		}
		
		if(!TextUtils.isEmpty(type)){
			if(type.length() > 10){
				pop_type_edit.setError("单位输入超过10位");
				CommonsUtil.showShortToast(GoodsAddActivity.this, "单位输入超过10位");
				return;
			}
		}
		
		merchInfo.setName(title);
		merchInfo.setDesc(content);
		merchInfo.setStore_id(store_id);
		merchInfo.setClassify_id(classify_id);
		merchInfo.setUnit(type);
		merchInfo.setOut_published("0");
		
		
		startProgressDialog();
		int user_id = sessionManager.getUserId();
		new addMerchTask(merchInfo, user_id).execute();
	}
	
	private class addMerchTask extends AsyncTask<Void, Void, String>{
		private MerchInfo merchInfo;
		private int user_id;
		public addMerchTask(MerchInfo merchInfo, int user_id) {
			this.merchInfo = merchInfo;
			this.user_id = user_id;
		}
		@Override
		protected String doInBackground(Void... params) {
			String url = HttpUtil.BASE_URL + "/merch/add.do";
			String msg = "";
			try {
				String merchInfoJson = HttpUtil.postRequest(url, merchInfo);
				if(merchInfoJson == null){
					return "添加商品失败";
				}
			
				msg = merchInfoJson;
				//查询Merch_id
				url = HttpUtil.BASE_URL + "/merch/queryMerchByMap.do";
				Map<String,String> map = new HashMap<String, String>();
				map.put("name", merchInfo.getName());
				map.put("desc", merchInfo.getDesc());
				map.put("store_id", merchInfo.getStore_id()+"");
				String json = HttpUtil.postRequest(url, map);
				if(json != null){
					List<MerchInfo> merchList = JsonUtil.parse2ListMerchInfo(json);
					if(merchList != null && !merchList.isEmpty()){
						MerchInfo merch = merchList.get(0);
						final int merchId = merch.getMerch_id();
						//上传图片到7牛吧
						if(lists != null && !lists.isEmpty()){
							for (AddImage addImage : lists) {
								File image = addImage.getFile();
								QiNiuUtil.resumeUploadFile(image.getName(), image, String.valueOf(user_id), new UpCompletionHandler() {
									@Override
									public void complete(String key, ResponseInfo info, JSONObject jsonObj) {
										if(info.statusCode == HttpStatus.OK.value()){
											LOGGER.info("上传图片成功！");
											Gallery gallery = new Gallery();
											gallery.setMerch_id(merchId);
											gallery.setFile_name(key);
											gallery.setName(key);
											addGallery(gallery);
										}else{
											LOGGER.error(">>> 保存图片到服务器失败" + info.error);
										}
									}
								});
							}
						}
					}
				}
				
			} catch (Exception e) {
				LOGGER.error(">>> 添加商品失败",e);
				return "添加商品失败";
			}
			return msg;
		}
	
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			stopProgressDialog();
			CommonsUtil.showShortToast(getApplicationContext(), result);
			
			setResult(RESULT_OK,new Intent());
			GoodsAddActivity.this.finish();
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
			LOGGER.error(">>> 更新图片失败",e);
			CommonsUtil.showShortToast(getApplicationContext(), "更新图片失败");
		}
	}
	

	private void showImageChoose() {
		imageChooseDialog = new AlertDialog.Builder(GoodsAddActivity.this)
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
			if(data != null){
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
				Cursor cursor = GoodsAddActivity.this.getContentResolver().query(
						selectImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				ImageName = picturePath.substring(picturePath.lastIndexOf("."),
						picturePath.length());
				ImageName = "test123" + ImageName;
				cursor.close();
				ImageUtil.startPhotoZoom(data.getData(),GoodsAddActivity.this);
			}
			break;
		case Constants.CAMERA_REQUEST_CODE:
			if (CommonsUtil.hasSdcard()) {
				File tempFile = FileUtil
						.getAppFolderFile(Constants.IMAGE_FILE_NAME);
				ImageUtil.startPhotoZoom(Uri.fromFile(tempFile),GoodsAddActivity.this);
			} else {
				Toast.makeText(GoodsAddActivity.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_LONG).show();
			}

			break;
		case Constants.RESULT_REQUEST_CODE:
			if (data != null) {
				getImageToView(data, GoodsAddActivity.this);
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
				
				File image = ImageUtil.bitmap2file(photo);
				if(image != null){
					addImage.setFile(image);
					addImage.setFileName(image.getName());
				}
			}else{
				CommonsUtil.showShortToast(GoodsAddActivity.this, "最多添加六张图片");
			}
		}
	}
	
}
