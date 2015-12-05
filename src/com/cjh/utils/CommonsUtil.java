package com.cjh.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.cjh.common.Constants;
import com.cjh.common.LineView;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

public class CommonsUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(CommonsUtil.class);

	/**
	 * 判断是否有sdcard
	 * @return
	 */
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
		activity.startActivityForResult(intentFromGallery,Constants.IMAGE_REQUEST_CODE);
	}
	/**
	 *打开照相机
	 * @param activity
	 */
	public static void openCamera(Activity activity) {
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			LOGGER.info(">>> i come here.");
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, 
					Uri.fromFile(FileUtil.getAppFolderFile(Constants.IMAGE_FILE_NAME)));
		}
		activity.startActivityForResult(intentFromCapture, Constants.CAMERA_REQUEST_CODE);
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

	public static void fillData(LineView lineView) {
		ArrayList<String> bottomlist = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			bottomlist.add((i * 2) + ":00");
		}
		lineView.setBottomTextList(bottomlist);
		ArrayList<Integer> dataslist = new ArrayList<Integer>();
		int random = (int) (Math.random() * 100 + 1);
		for (int i = 0; i < 12; i++) {
			dataslist.add((int) (Math.random() * random));
		}
		lineView.setDataList(dataslist);
	}

}
