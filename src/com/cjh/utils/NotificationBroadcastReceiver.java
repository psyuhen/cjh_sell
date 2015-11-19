package com.cjh.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cjh.activity.CommunicationActivity;
import com.cjh.common.Constants;

/**
 * COPY BY leancloud
 * 因为 notification 点击时，控制权不在 app，此时如果 app 被 kill
 * 或者上下文改变后， 有可能对 notification 的响应会做相应的变化，所以此处将所有 notification 都发送至此类， 然后由此类做分发。
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AVImClientManager instance = AVImClientManager.getInstance();
		if (instance.getClient() == null) {
			String clientId = intent.getStringExtra(Constants.CLIENT_ID);
			if(clientId == null){
				return;
			}
			instance.open(clientId, null);
		} 
		
		String conversationId = intent.getStringExtra(Constants.CONVERSATION_ID);
		if (!TextUtils.isEmpty(conversationId)) {
			gotoSingleChatActivity(context, intent);
		}
	}

	/**
	 * 如果 app 上下文已经缺失，则跳转到登陆页面，走重新登陆的流程
	 * 
	 * @param context
	 */
	/*private void gotoLoginActivity(Context context) {
		Intent startActivityIntent = new Intent(context, AVLoginActivity.class);
		startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startActivityIntent);
	}*/


	/**
	 * 跳转至单聊页面
	 * 
	 * @param context
	 * @param intent
	 */
	private void gotoSingleChatActivity(Context context, Intent intent) {
		Intent startActivityIntent = new Intent(context, CommunicationActivity.class);
		startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivityIntent.putExtra(Constants.MEMBER_ID, intent.getStringExtra(Constants.MEMBER_ID));//买家手机号
		startActivityIntent.putExtra("buyer_user_id", intent.getStringExtra("buyer_user_id"));//买家ID
		context.startActivity(startActivityIntent);
	}
}
