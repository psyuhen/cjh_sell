/**
 * 
 */
package com.cjh.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP 工具类
 * @author ps
 *
 */
public class HttpUtil {
	public static final String BASE_URL = "http://192.168.1.104:8001/sgams";
//	public static final String BASE_URL = "http://192.168.43.191:8001/sgams";
//	public static final String BASE_URL = "http://localhost:8001/sgams";
	
	public static final RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * 
	 * @param url 发送的请求URL
	 * @return 服务器响应的字符串
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static String getRequest(final String url) throws InterruptedException, ExecutionException{
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			@Override
			public String call() throws Exception {
				 ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
				 if(HttpStatus.OK.equals(response.getStatusCode())){
					 return response.getBody();
				 }
				return null;
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 使用postForEntity
	 * @param url 发送的请求URL
	 * @param params 发送的请求参数
	 * @return 服务器响应的字符串
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static String postRequest(final String url,final Object params) throws InterruptedException, ExecutionException{
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			@Override
			public String call() throws Exception {
				ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);
				if(HttpStatus.OK.equals(response.getStatusCode())){
					return response.getBody();
				}
				return null;
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
	/**
	 * 使用postForEntity
	 * @param url 发送的请求URL
	 * @param params 发送的请求参数
	 * @return 服务器响应的字符串
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static ResponseEntity<String> postRequest2(final String url,final Object params) throws InterruptedException, ExecutionException{
		FutureTask<ResponseEntity<String>> task = new FutureTask<ResponseEntity<String>>(new Callable<ResponseEntity<String>>() {
			@Override
			public ResponseEntity<String> call() throws Exception {
				ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);
				return response;
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
}
