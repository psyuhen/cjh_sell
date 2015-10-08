package com.cjh.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cjh.cjh_sell.R;
import com.cjh.common.Constants;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

public class ImageUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static void startPhotoZoom(Uri uri,Activity activity) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, Constants.RESULT_REQUEST_CODE);
	}

	/**
	 * 把bitmap转换为inputstream
	 * @param bitmap
	 * @return
	 */
	public static InputStream bitmap2stream(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
		
		return bs;
	}
	
	/**
	 * 把bitmap转换为file
	 * @param activity
	 * @param bitmap
	 * @return
	 */
	public static File bitmap2file(Activity activity,Bitmap bitmap){
		File filesDir = activity.getFilesDir();
		String currentTime = DateUtil.currentTime();
		File imageFile = new File(filesDir, currentTime + ".png");
		OutputStream os = null;
		try {
			os = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			os.flush();
		} catch (Exception e) {
			LOGGER.error("Error writing bitmap", e);
			return null;
		} finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					LOGGER.error( "Error close outputstream", e);
				}
			}
		}
		
		return imageFile;
	}
	/**
	 * 把bitmap转换为file
	 * @param bitmap
	 * @return
	 */
	public static File bitmap2file(Bitmap bitmap){
		File filesDir = FileUtil.getAppFolderFile();
		String currentTime = DateUtil.currentTime();
		File imageFile = new File(filesDir, currentTime + ".png");
		OutputStream os = null;
		try {
			os = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			os.flush();
		} catch (Exception e) {
			LOGGER.error("Error writing bitmap", e);
			return null;
		} finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					LOGGER.error( "Error close outputstream", e);
				}
			}
		}
		
		return imageFile;
	}
	
	/**
	 * 显示各种
	 * @param activity
	 * @return
	 */
	public static AlertDialog showImageChoose(Activity activity) {
		AlertDialog imageChooseDialog = new AlertDialog.Builder(activity).create();
		imageChooseDialog.show();
		imageChooseDialog.getWindow().setContentView(R.layout.image_choose_dialog);
		Button dialog_album = (Button) imageChooseDialog.findViewById(R.id.dialog_album);
		dialog_album.setOnClickListener((OnClickListener)activity);
		Button dialog_camera = (Button) imageChooseDialog.findViewById(R.id.dialog_camera);
		dialog_camera.setOnClickListener((OnClickListener)activity);
		Button dialog_cancel = (Button) imageChooseDialog.findViewById(R.id.dialog_cancel);
		dialog_cancel.setOnClickListener((OnClickListener)activity);
		
		return imageChooseDialog;
	}
}
