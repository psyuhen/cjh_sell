package com.cjh.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cjh.adapter.AddImageAdapter;
import com.cjh.bean.AddImage;
import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.ImageUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class CategoryAddActivity extends BaseTwoActivity{
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_category);
		initView();
		initData();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		content_add_image=(ImageView) findViewById(R.id.content_add_image);
		content_add_image.setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.content_add_gridview);
		lists = new ArrayList<AddImage>();
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		title.setText("添加类别");
		right_imgbtn.setVisibility(View.GONE);
		right_text.setText("添加");
		adapter = new AddImageAdapter(CategoryAddActivity.this, lists);
		gridView.setAdapter(adapter);
		right_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CommonsUtil.showShortToast(CategoryAddActivity.this, "添加完成！");
				startActivity(new Intent(CategoryAddActivity.this, CategoryActivity.class));
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.content_add_image:
			showImageChoose();
			break;
		case R.id.dialog_album:
			imageChooseDialog.dismiss();
			CommonsUtil.opengallry(CategoryAddActivity.this);
			break;
		case R.id.dialog_camera:
			CommonsUtil.openCamera(CategoryAddActivity.this);
			break;
		case R.id.dialog_cancel:
			imageChooseDialog.dismiss();
			break;
		default:
			break;
		}
	}
	
	
	private void showImageChoose() {
		// TODO Auto-generated method stub
		imageChooseDialog = new AlertDialog.Builder(CategoryAddActivity.this)
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
		// TODO Auto-generated method stub
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
			Cursor cursor = CategoryAddActivity.this.getContentResolver().query(
					selectImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			ImageName = picturePath.substring(picturePath.lastIndexOf("."),
					picturePath.length());
			ImageName = "test123" + ImageName;
			cursor.close();
			ImageUtil.startPhotoZoom(data.getData(),CategoryAddActivity.this);
			break;
		case Constants.CAMERA_REQUEST_CODE:
			if (CommonsUtil.hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ Constants.IMAGE_FILE_NAME);
				ImageUtil.startPhotoZoom(Uri.fromFile(tempFile),CategoryAddActivity.this);
			} else {
				Toast.makeText(CategoryAddActivity.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_LONG).show();
			}

			break;
		case Constants.RESULT_REQUEST_CODE:
			if (data != null) {
				getImageToView(data, CategoryAddActivity.this);
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
			content_add_image.setVisibility(View.GONE);
		}
	}
}
