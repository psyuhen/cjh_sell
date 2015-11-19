/**
 * 
 */
package com.cjh.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 文件操作
 * 
 * @author ps
 * 
 */
public class FileUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	private final static String FOLDER = "cjh_seller";
	private final static String AUDIO_FOLDER = "audio";

	/**
	 * 创建目录，在父目录下，创建一个新目录
	 * @param parentDir 父目录的路径
	 * @param folderName 目录
	 */
	public static void createDir(String parentDir, String folderName) {
		createDir(parentDir + "/" + folderName);
	}
	/**
	 * 创建目录，SD card下创建目录
	 * 
	 * @param folderPath 目录路径
	 */
	public static void createDir(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}
	/**
	 * 创建APP目录
	 */
	public static void createAppFolder() {
		createDir(getAppFolder());
		createAudioFolder();
	}
	
	/**
	 * 创建APP目录下的audio目录
	 */
	public static void createAudioFolder() {
		createDir(getAudioFolder());
	}

	/**
	 * 获取APP目录路径
	 * 
	 * @return
	 */
	public static String getAppFolder() {
		return Environment.getExternalStorageDirectory() + "/" + FOLDER;
	}
	
	/**
	 * 获取APP目录下的audio路径
	 * 
	 * @return
	 */
	public static String getAudioFolder() {
		return getAppFolder() + "/" + AUDIO_FOLDER;
	}
	/**
	 * 获取APP目录
	 * 
	 * @return
	 */
	public static File getAppFolderFile() {
		return new File(getAppFolder());
	}
	/**
	 * 获取APP目录的audio路径
	 * 
	 * @return
	 */
	public static File getAudioFolderFile() {
		return getAppFolderFile(AUDIO_FOLDER);
	}
	/**
	 * 获取APP目录
	 * @param fileName
	 * @return
	 */
	public static File getAppFolderFile(String fileName) {
		return new File(getAppFolder() + "/" + fileName);
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isExists(String fileName){
		File file = getAppFolderFile(fileName);
		return file.exists();
	}

	/**
	 * 获取文件的Bitmap
	 * @param fileName
	 * @return
	 */
	public static Bitmap fromFile(String fileName){
		return BitmapFactory.decodeFile(getAppFolder() + "/" + fileName);
	}
	
	/**
	 * 从文件服务器中获取文件，如果文件名为null，返回null,
	 * 否则判断文件是否在文件中存在，否，从文件服务器中获取成功后，存在到手机中
	 * 是，直接从手机获取
	 * @param fileName 输入的文件名
	 * @return
	 */
	public static Bitmap getCacheFile(String fileName){
		//如果文件名不存在，即为还没保存文件，直接返回Null
		if(fileName == null || "".equals(fileName)){
			return null;
		}
		//判断文件是否存在
		boolean exists = FileUtil.isExists(fileName);
		if(!exists){
			String imageUrl = QiNiuUtil.getImageUrl(fileName);
			LOGGER.info("获取"+fileName+"的url:"+imageUrl);
			if(!"".equals(imageUrl)){
				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				try {
					Bitmap bitmap = QiNiuUtil.getQiNiu(imageUrl);
					
					if(bitmap == null){
						LOGGER.error("生成bitmap失败:"+fileName);
						return null;
					}
					
					//保存文件到本地
					File local = getAppFolderFile(fileName);
					local.createNewFile();
					
					fos = new FileOutputStream(local);
					bos = new BufferedOutputStream(fos);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
					bos.flush();
					
					return bitmap;
				} catch (Exception e) {
					LOGGER.error("保存文件到本地出错",e);
				}finally{
					if(bos != null){
						try {
							bos.close();
						} catch (IOException e) {
							LOGGER.error("关闭流出错",e);
						}
					}
					if(fos != null){
						try {
							fos.close();
						} catch (IOException e) {
							LOGGER.error("关闭流出错",e);
						}
					}
				}
			}
		}else if(exists){
			return fromFile(fileName);
		}
		
		return null;
	}
	
	/**
	 * 获取远程的语音文件,若文件在本地存在，就不再取了。
	 * @param url
	 * @param fileName 
	 */
	public static void getRemoteFile(String url, String fileName){
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			File file = new File(getAudioFolder() + "/" + fileName);
			if(file.exists()){
				return;
			}
			
			byte[] fileBytes = HttpUtil.getRequestBype(url);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			
			bos.write(fileBytes);
		} catch (Exception e) {
			LOGGER.error("获取语音文件失败", e);
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					LOGGER.error("关闭流失败", e);
				}
			}
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					LOGGER.error("关闭流失败", e);
				}
			}
		}
	}
}
