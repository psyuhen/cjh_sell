/**
 * 
 */
package com.cjh.utils;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * HTTP 工具类
 * @author ps
 *
 */
public class HttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
	
	public static final String IP = "192.168.1.103";
//	public static final String IP = "203.195.245.171";
//	public static final String IP = "192.168.43.67";
//	public static final String IP = "128.128.80.47";
	public static final String BASE_URL = "http://"+ IP +":8001/sgams";
	private static final int CONNECT_TIME_OUT = 300;
	
	public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);
	
	public static RestTemplate getSimpleClient(){
		RestTemplate restTemplate = new RestTemplate();
		SimpleClientHttpRequestFactory requestFactory = (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();
		requestFactory.setConnectTimeout(CONNECT_TIME_OUT);
		return restTemplate;
	}
	
	public static RestTemplate getComponentsClient(){
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(CONNECT_TIME_OUT);
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		return restTemplate;
	}
	
	/**
	 * 
	 * @param url 发送的请求URL
	 * @return 服务器响应的字符串
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static String getRequest(String url) throws InterruptedException, ExecutionException{
		Future<String> task = EXECUTOR_SERVICE.submit(new CJHCallable<String>(url, String.class));
		
		return task.get();
	}
	
	/**
	 * 获取远程服务器文件的byte[]
	 * @param url 发送的请求URL
	 * @return 服务器响应的字节数
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static byte[] getRequestBype(final String url) throws InterruptedException, ExecutionException{
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		Future<byte[]> task = EXECUTOR_SERVICE.submit(new CJHCallable<byte[]>(url, entity, byte[].class, HttpType.EXCHANGE));
		
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
		Future<String> task = EXECUTOR_SERVICE.submit(new CJHCallable<String>(url, params, String.class));
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
		Future<ResponseEntity<String>> task = EXECUTOR_SERVICE.submit(new Callable<ResponseEntity<String>>() {
			@Override
			public ResponseEntity<String> call() throws Exception {
				try{
					return getSimpleClient().postForEntity(url, params, String.class);
				}catch (Exception e) {
					LOGGER.error("post 请求失败", e);
				}
			return null;
			}
		});
		
		return task.get();
	}
}
