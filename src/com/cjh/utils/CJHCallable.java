/**
 * 
 */
package com.cjh.utils;

import java.util.concurrent.Callable;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * @author pansen
 *
 */
public class CJHCallable <V> implements Callable<V>{
	private Logger LOGGER = LoggerFactory.getLogger(CJHCallable.class);

	private String url = "";
	private Class<V> cls = null;
	private HttpType httpType;
	private Object params = null;
	
	/**
	 * params 为空，httpType 为 {@link HttpType#POST}
	 * @param url 请求
	 * @param cls 返回对象
	 */
	public CJHCallable(String url, Object params, Class<V> cls) {
		this(url, params, cls, HttpType.POST);
	}
	/**
	 * params 为空，httpType 为 {@link HttpType#GET}
	 * @param url 请求
	 * @param cls 返回对象
	 */
	public CJHCallable(String url, Class<V> cls) {
		this(url, null, cls, HttpType.GET);
	}
	
	/**
	 * 
	 * @param url 请求
	 * @param cls 返回对象
	 * @param httpType 请求类型
	 * @param params 请求参数
	 */
	public CJHCallable(String url, Object params, Class<V> cls, HttpType httpType) {
		this.url = url;
		this.params = params;
		this.cls = cls;
		this.httpType = httpType;
	}
	
	@Override
	public V call() throws Exception {
		ResponseEntity<V> response =  null;
		try{
			switch (httpType) {
			case GET:
				response = HttpUtil.getSimpleClient().getForEntity(url, cls);
				break;
			case POST:
				response = HttpUtil.getSimpleClient().postForEntity(url, params, cls);
				break;
			case EXCHANGE:
				response = HttpUtil.getComponentsClient().exchange(url, HttpMethod.GET, (HttpEntity<?>)params, cls);
				break;
			default:
				response = null;
				return null;
			}
			
			
			if(HttpStatus.OK.equals(response.getStatusCode())){
				return response.getBody();
			}
		}catch (Exception e) {
			LOGGER.error(url+"\t请求失败", e);
		}
		return null;
	}
	
}
