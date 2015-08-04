/**
 * 
 */
package com.cjh.utils;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cjh.qiniu.UploadFile2QiNiu;
import com.qiniu.api.io.PutRet;

/**
 * 7牛图片上传
 * @author ps
 *
 */
public class QiNiuUtil {
	
	/**
	 * 获取7牛上的图片并转换为bitmap
	 * @param url
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Bitmap getQiNiu(final String url) throws InterruptedException, ExecutionException{
		FutureTask<Bitmap> task = new FutureTask<Bitmap>(new Callable<Bitmap>() {
			@Override
			public Bitmap call() throws Exception {
				URL picUrl = new URL(url);
				Bitmap bitmap = BitmapFactory.decodeStream(picUrl.openStream()); 
				return bitmap;
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 断点续传
	 * @param file
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static PutRet resumeUploadFile(final File file) throws InterruptedException, ExecutionException{
		FutureTask<PutRet> task = new FutureTask<PutRet>(new Callable<PutRet>() {
			@Override
			public PutRet call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.resumeUploadFile(file);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 断点续传
	 * @param fileName 在7牛空间上的新名称
	 * @param file
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static PutRet resumeUploadFile(final String fileName, final File file) throws InterruptedException, ExecutionException{
		FutureTask<PutRet> task = new FutureTask<PutRet>(new Callable<PutRet>() {
			@Override
			public PutRet call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.resumeUploadFile(fileName, file);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 断点续传
	 * @param file
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static PutRet resumeUploadStream(final File file) throws InterruptedException, ExecutionException{
		FutureTask<PutRet> task = new FutureTask<PutRet>(new Callable<PutRet>() {
			@Override
			public PutRet call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.resumeUploadStream(file);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 断点续传
	 * @param fileName 在7牛空间上的新名称
	 * @param file
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static PutRet resumeUploadStream(final String fileName, final File file) throws InterruptedException, ExecutionException{
		FutureTask<PutRet> task = new FutureTask<PutRet>(new Callable<PutRet>() {
			@Override
			public PutRet call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.resumeUploadStream(fileName, file);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 批量上传，不支持断点续传
	 * @param file
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static List<PutRet> uploadFile(final List<File> files) throws InterruptedException, ExecutionException{
		FutureTask<List<PutRet>> task = new FutureTask<List<PutRet>>(new Callable<List<PutRet>>() {
			@Override
			public List<PutRet> call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.uploadFile(files);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 不支持断点续传
	 * @param file
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static PutRet uploadFile(final File file) throws InterruptedException, ExecutionException{
		FutureTask<PutRet> task = new FutureTask<PutRet>(new Callable<PutRet>() {
			@Override
			public PutRet call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.uploadFile(file);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 不支持断点续传
	 * @param fileName 在7牛空间上的新名称
	 * @param file
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static PutRet uploadFile(final String fileName, final File file) throws InterruptedException, ExecutionException{
		FutureTask<PutRet> task = new FutureTask<PutRet>(new Callable<PutRet>() {
			@Override
			public PutRet call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.uploadFile(fileName, file);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	
	/**
	 * 获取7牛空间里面的图片名称
	 * @param imageName 文件名也是key
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static String getImageUrl(final String imageName) throws InterruptedException, ExecutionException{
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			@Override
			public String call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.getImageUrl(imageName);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	
	/**
	 * 删除7牛上的文件
	 * @param imageName
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static boolean deleteFile(final String imageName) throws InterruptedException, ExecutionException{
		FutureTask<Boolean> task = new FutureTask<Boolean>(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				UploadFile2QiNiu uploadFile2QiNiu = UploadFile2QiNiu.getInstance();
				return uploadFile2QiNiu.deleteFile(imageName);
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
}
