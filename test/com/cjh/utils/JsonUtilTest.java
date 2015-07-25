package com.cjh.utils;

import org.junit.Test;

import com.cjh.bean.User;

public class JsonUtilTest {
	
	@Test
	public void testParse2Object(){
		User user = JsonUtil.parse2Object("{'name':'aa'}",User.class);
		junit.framework.Assert.assertNotNull(user);
	}
}
