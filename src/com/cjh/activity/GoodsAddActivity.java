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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjh.adapter.AddImageAdapter;
import com.cjh.bean.AddImage;
import com.cjh.bean.MerchInfo;
import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.ImageUtil;
/**
 * 添加商品
 * @author ps
 *
 */
public class GoodsAddActivity extends BaseTwoActivity {
	public static final String TAG = "GoodsAddActivity";
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
	//private EditText pop_tel_edit;//联系电话
	

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
		//pop_tel_edit = (EditText) findViewById(R.id.pop_tel_edit);
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
			CommonsUtil.openCamera(GoodsAddActivity.this);
			break;
		case R.id.dialog_cancel:
			imageChooseDialog.dismiss();
			break;
		case R.id.right_text:
			addGoods();
			break;
		default:
			break;
		}
	}
	
	//添加商品
	private void addGoods(){
		// 清空错误
		pop_title_edit.setError(null);
		pop_content_edit.setError(null);
		
		String title = pop_title_edit.getText().toString();//标题
		String content = pop_content_edit.getText().toString();//正文内容
		//String tel = pop_tel_edit.getText().toString();;//联系电话
		
		if(TextUtils.isEmpty(title)){
			pop_title_edit.setError(getString(R.string.error_field_required));
			return;
		}
		
		if(TextUtils.isEmpty(content)){
			pop_content_edit.setError(getString(R.string.error_field_required));
			return;
		}
		
		MerchInfo merchInfo = new MerchInfo();
		merchInfo.setName(title);
		merchInfo.setDesc(content);
		
		String url = HttpUtil.BASE_URL + "/merch/add.do";
		try {
			String json = HttpUtil.postRequest(url, merchInfo);
			if(json != null){
				CommonsUtil.showShortToast(GoodsAddActivity.this, "添加成功");
				startActivity(new Intent(GoodsAddActivity.this, GoodsActivity.class));
			}
		} catch (InterruptedException e) {
			Log.i(TAG, "添加商品失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "添加商品失败");
		} catch (ExecutionException e) {
			Log.i(TAG, "添加商品失败", e);
			CommonsUtil.showLongToast(getApplicationContext(), "添加商品失败");
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
			break;
		case Constants.CAMERA_REQUEST_CODE:
			if (CommonsUtil.hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ Constants.IMAGE_FILE_NAME);
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
			AddImage addImage = new AddImage();
			addImage.setBitmap(photo);
			lists.add(addImage);
			adapter.notifyDataSetChanged();
		}
	}
	
}
