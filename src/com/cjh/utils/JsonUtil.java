/**
 * 
 */
package com.cjh.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjh.bean.ClassifyInfo;
import com.cjh.bean.Coupon;
import com.cjh.bean.FavoriteStat;
import com.cjh.bean.Gallery;
import com.cjh.bean.MerchDisacount;
import com.cjh.bean.MerchInfo;
import com.cjh.bean.Order;
import com.cjh.bean.OrderStat;
import com.cjh.bean.VisitStat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * json工具类
 * 
 * @author ps
 * 
 */
public class JsonUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
	/**
	 * 把json字符串转换为一个Map
	 * @param json json字符串
	 * @return
	 */
	public static HashMap<String, String> parse2Map(String json) {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
		HashMap<String, String> map = null;
		try {
			map = mapper.readValue(json, typeRef);
		} catch (JsonParseException e) {
			LOGGER.error("json转换出错", e);
		} catch (JsonMappingException e) {
			LOGGER.error("json转换出错", e);
		} catch (IOException e) {
			LOGGER.error("io出错", e);
		}

		return map;
	}
	
	/**
	 * 把json字符串转换为一个Map
	 * @param json json字符串
	 * @return
	 */
	public static List<Map<String, String>> parse2ListMap(String json) {
		return parse2ListObject(json, new TypeReference<List<Map<String, String>>>() {});
	}
	
	/**
	 * json 转换为list &lt;T&gt;
	 * @param json
	 * @param typeRef
	 * @return
	 */
	public static <T> List<T> parse2ListObject(String json,TypeReference<List<T>> typeRef){
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		List<T> list = null;
		try {
			list = mapper.readValue(json, typeRef);
		} catch (JsonParseException e) {
			LOGGER.error("json转换出错", e);
		} catch (JsonMappingException e) {
			LOGGER.error("json转换出错", e);
		} catch (IOException e) {
			LOGGER.error("io出错", e);
		}


		return list;
	}
	
	/**
	 * 把json转换为对象
	 * @param json
	 * @param valueType
	 * @return
	 */
	public static <T> T  parse2Object(String json,Class<T> valueType){
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		T t = null;
		try {
			t = mapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			LOGGER.error("json转换出错", e);
		} catch (JsonMappingException e) {
			LOGGER.error("json转换出错", e);
		} catch (IOException e) {
			LOGGER.error("io出错", e);
		}


		return t;
	}
	
	/**
	 * 把json字符串转换为一个List&lt;MerchInfo&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<MerchInfo> parse2ListMerchInfo(String json) {
		return parse2ListObject(json, new TypeReference<List<MerchInfo>>() {});
	} 
	/**
	 * 把json字符串转换为一个List&lt;ClassifyInfo&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<ClassifyInfo> parse2ListClassifyInfo(String json) {
		return parse2ListObject(json, new TypeReference<List<ClassifyInfo>>() {});
	} 
	/**
	 * 把json字符串转换为一个List&lt;Order&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<Order> parse2ListOrder(String json) {
		return parse2ListObject(json, new TypeReference<List<Order>>() {});
	} 
	/**
	 * 把json字符串转换为一个List&lt;OrderStat&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<OrderStat> parse2ListOrderStat(String json) {
		return parse2ListObject(json, new TypeReference<List<OrderStat>>() {});
	} 
	/**
	 * 把json字符串转换为一个List&lt;FavoriteStat&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<FavoriteStat> parse2ListFavoriteStat(String json) {
		return parse2ListObject(json, new TypeReference<List<FavoriteStat>>() {});
	} 
	/**
	 * 把json字符串转换为一个List&lt;VisitStat&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<VisitStat> parse2ListVisitStat(String json) {
		return parse2ListObject(json, new TypeReference<List<VisitStat>>() {});
	} 
	/**
	 * 把json字符串转换为一个List&lt;MerchDisacount&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<MerchDisacount> parse2ListMerchDisacount(String json) {
		return parse2ListObject(json, new TypeReference<List<MerchDisacount>>() {});
	} 
	/**
	 * 把json字符串转换为一个List&lt;Coupon&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<Coupon> parse2ListCoupon(String json) {
		return parse2ListObject(json, new TypeReference<List<Coupon>>() {});
	} 
	/**
	 * 把json字符串转换为一个List&lt;Gallery&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<Gallery> parse2ListGallery(String json) {
		return parse2ListObject(json, new TypeReference<List<Gallery>>() {});
	} 
}
