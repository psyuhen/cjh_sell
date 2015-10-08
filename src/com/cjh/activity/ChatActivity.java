package com.cjh.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cjh.adapter.ChatMsgViewAdapter;
import com.cjh.bean.ChatMsgItem;
import com.cjh.bean.User;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.SocketUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 聊天窗口
 * @author ps
 *
 */
public class ChatActivity extends BaseTwoActivity{
	private static final Logger LOGGER = LoggerFactory.getLogger(ChatActivity.class);

	private ChatMsgViewAdapter mAdapter;
	private ListView mListView;
	private List<ChatMsgItem> msgList;
	private Button chat_send_btn;
	private EditText chat_edit_text;
	private String buyer_user_id;
	private String buyer_user_name;
	private String buyer_user_mobile;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initView();
		initData();
		title.setText(buyer_user_name);
	}
	@Override
	public void initView() {
		super.initView();
		right_imgbtn.setVisibility(View.GONE);
		mListView = (ListView) findViewById(R.id.chat_msg_listview);
		chat_send_btn = (Button) findViewById(R.id.chat_send_message_btn);
		chat_send_btn.setOnClickListener(this);
		chat_edit_text = (EditText) findViewById(R.id.chat_send_edit_msg);
		
		Intent intent = getIntent();
		buyer_user_id = intent.getStringExtra("buyer_user_id");
		buyer_user_name = intent.getStringExtra("buyer_user_name");
		buyer_user_mobile = intent.getStringExtra("buyer_user_name");
	}
	private void initData() {
		title.setText("聊天");
		user = sessionManager.getUserDetails();
		msgList = new ArrayList<ChatMsgItem>();
		mAdapter = new ChatMsgViewAdapter(getApplicationContext(), msgList);
		mListView.setAdapter(mAdapter);
		
		getOffLineMsg();
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.chat_send_message_btn:
			send();
			break;

		default:
			break;
		}
	}
	//每次进来先获取离线消息
	private void getOffLineMsg(){
		//发送到后端
		try {
			List<ChatMsgItem> list = SocketUtil.receive(user.getUser_id()+"", buyer_user_id);
			if(list != null && !list.isEmpty()){
				msgList.addAll(list);
				Collections.sort(list, new Comparator<ChatMsgItem>() {
					@Override
					public int compare(ChatMsgItem item1, ChatMsgItem item2) {
						Date d1 = item1.getSendDate();
						Date d2 = item2.getSendDate();
						
						if(d1 == null){
							return -1;
						}
						
						if(d2 == null){
							return 1;
						}
						
						return d1.compareTo(d2);
					}
				});
				mAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			LOGGER.error("接收信息失败",e);
		}
	}
	//发送消息
	private void send(){
		String chatContent = chat_edit_text.getText().toString();
		if(TextUtils.isEmpty(chatContent.trim())){
			CommonsUtil.showLongToast(getApplicationContext(), "内容不能为空");
			return;
		}
		
		ChatMsgItem msgItem = new ChatMsgItem();
		msgItem.setSendDate(new Date());
		msgItem.setComing(false);
		msgItem.setSendUser(user.getName());
		msgItem.setToUser(buyer_user_name);
		msgItem.setContent(chatContent);
		msgList.add(msgItem);
		mAdapter.notifyDataSetChanged();
		
		User toUser = new User();
		toUser.setUser_id(Integer.parseInt(buyer_user_id));
		toUser.setMobile(buyer_user_mobile);
		toUser.setName(buyer_user_name);
		//发送到后端
		SocketUtil.send(chatContent, toUser, user);
		
		chat_edit_text.setText("");
	}
}
