/**
 * 
 */
package com.cjh.utils;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP 工具类
 * @author ps
 *
 */
public class HttpUtil {
//	public static final String IP = "192.168.1.104";
	public static final String IP = "203.195.245.171";
//	public static final String IP = "192.168.43.191";
	public static final String BASE_URL = "http://"+ IP +":8001/sgams";
	
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
				HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
				factory.setConnectTimeout(300);
				RestTemplate restTemplate = new RestTemplate(factory);
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
	 * 获取远程服务器文件的byte[]
	 * @param url 发送的请求URL
	 * @return 服务器响应的字节数
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static byte[] getRequestBype(final String url) throws InterruptedException, ExecutionException{
		FutureTask<byte[]> task = new FutureTask<byte[]>(new Callable<byte[]>() {
			@Override
			public byte[] call() throws Exception {
				HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
				factory.setConnectTimeout(300);
				RestTemplate restTemplate = new RestTemplate(factory);
				restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());    
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
				HttpEntity<String> entity = new HttpEntity<String>(headers);
				
				ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
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
				HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
				factory.setConnectTimeout(300);
				RestTemplate restTemplate = new RestTemplate(factory);
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
				HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
				factory.setConnectTimeout(300);
				RestTemplate restTemplate = new RestTemplate(factory);
				ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);
				return response;
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
}
