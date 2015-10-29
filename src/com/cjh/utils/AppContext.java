package com.cjh.utils;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 
 * @author pansen
 *
 */
public class AppContext extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppContext.class);

	@Override
	public void onCreate() {
		super.onCreate();
		
		try{
			AVOSCloud.initialize(this, "OMnLPjX7ykL6B82b7TeKNvcT", "TF17FlFxgKD9KFaFuPgRi9Xr");
			// 必须在启动的时候注册 MessageHandler
		    // 应用一启动就会重连，服务器会推送离线消息过来，需要 MessageHandler 来处理
		    AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(this));
		} catch (Exception e) {
			LOGGER.error("AVOSCloud初始化失败",e);
		}
	}
}
