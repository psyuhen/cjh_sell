/**
 * 
 */
package com.cjh.utils;

import java.io.File;
import java.util.List;

import com.cjh.qiniu.UploadFile2QiNiu;
import com.qiniu.api.io.PutRet;

/**
 * 7牛图片上传
 * @author ps
 *
 */
public class QiNiuUtil {
	
	/**
	 * 断点续传
	 * @param file
	 * @return
	 */
	public static PutRet resumeUploadFile(File file){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.resumeUploadFile(file);
	}
	/**
	 * 断点续传
	 * @param fileName 在7牛空间上的新名称
	 * @param file
	 * @return
	 */
	public static PutRet resumeUploadFile(String fileName, File file){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.resumeUploadFile(fileName, file);
	}
	/**
	 * 断点续传
	 * @param file
	 * @return
	 */
	public static PutRet resumeUploadStream(File file){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.resumeUploadStream(file);
	}
	/**
	 * 断点续传
	 * @param fileName 在7牛空间上的新名称
	 * @param file
	 * @return
	 */
	public static PutRet resumeUploadStream(String fileName, File file){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.resumeUploadStream(fileName, file);
	}
	/**
	 * 批量上传，不支持断点续传
	 * @param file
	 * @return
	 */
	public static List<PutRet> uploadFile(List<File> files){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.uploadFile(files);
	}
	/**
	 * 不支持断点续传
	 * @param file
	 * @return
	 */
	public static PutRet uploadFile(File file){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.uploadFile(file);
	}
	/**
	 * 不支持断点续传
	 * @param fileName 在7牛空间上的新名称
	 * @param file
	 * @return
	 */
	public static PutRet uploadFile(String fileName, File file){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.uploadFile(fileName, file);
	}
	
	/**
	 * 获取7牛空间里面的图片名称
	 * @param imageName 文件名也是key
	 * @return
	 */
	public static String getImageUrl(String imageName){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.getImageUrl(imageName);
	}
	
	/**
	 * 删除7牛上的文件
	 * @param imageName
	 * @return
	 */
	public static boolean deleteFile(String imageName){
		UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
		return uploadFile2QiNiu.deleteFile(imageName);
	}
}
