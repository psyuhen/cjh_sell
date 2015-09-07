package com.cjh.utils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.cjh.common.Constants;

public class CommonsUtil {

	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 打开相册
	 * @param activity
	 */
	public static void opengallry(Activity activity) {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intentFromGallery,
				Constants.IMAGE_REQUEST_CODE);
	}
	/**
	 *打开照相机
	 * @param activity
	 */
	public static void openCamera(Activity activity) {
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(),
							Constants.IMAGE_FILE_NAME)));
		}
		activity.startActivityForResult(intentFromCapture,
				Constants.CAMERA_REQUEST_CODE);
	}
	
	public static void showLongToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static String getOrderStatus(char type) {
		switch (type) {
		case '1':
			return "卖方确认订单";
		case '2':
			return "等待买方付款";
		case '3':
			return "卖方发货";
		case '4':
			return "交易完成";
		case '5':
			return "交易取消";
		case '6':
			return "交易关闭";
		default:
			return "";
		}
	}
	public static String getCouponsStatus(char type) {
		switch (type) {
		case '1':
			return "可领用";
		case '2':
			return "未开始";
		case '3':
			return "已领完";
		case '4':
			return "已过期";
		default:
			return "";
		}
	}
}
