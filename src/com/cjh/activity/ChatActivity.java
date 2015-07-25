package com.cjh.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cjh.adapter.ChatMsgViewAdapter;
import com.cjh.bean.ChatMsgItem;
import com.cjh.cjh_sell.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * 聊天窗口
 * @author ps
 *
 */
public class ChatActivity extends BaseTwoActivity{
	private ChatMsgViewAdapter mAdapter;
	private ListView mListView;
	private List<ChatMsgItem> msgList;
	private Button chat_send_btn;
//	private EditText chat_edit_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initView();
		initData();
		mAdapter = new ChatMsgViewAdapter(ChatActivity.this, msgList);
		mListView.setAdapter(mAdapter);
		title.setText("詹姆斯");
	}
	@Override
	public void initView() {
		super.initView();
		mListView = (ListView) findViewById(R.id.chat_msg_listview);
		chat_send_btn = (Button) findViewById(R.id.chat_send_message_btn);
		chat_send_btn.setOnClickListener(this);
	//	chat_edit_text = (EditText) findViewById(R.id.chat_send_edit_msg);
	}
	private void initData() {
		title.setText("聊天");
		msgList = new ArrayList<ChatMsgItem>();
		for (int i = 0; i < 10; i++) {
			ChatMsgItem msgItem = new ChatMsgItem();
			msgItem.setSendDate(new Date());
			if (i % 2 == 0) {
				msgItem.setComing(true);
				msgItem.setSendUser("科比");
				msgItem.setToUser("詹姆斯");
				msgItem.setContent("詹姆斯你好，請問我這週的時間安排是什麼！");
			}
			if (i % 2 == 1) {
				msgItem.setComing(false);
				msgItem.setSendUser("詹姆斯");
				msgItem.setToUser("科比");
				msgItem.setContent("科比你好，你的白班時間是週一，週三，週五，夜班時間是週二和週四，請注意時間的安排，謝謝查詢");
			}
			msgList.add(msgItem);

		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.chat_send_message_btn:
			
			break;

		default:
			break;
		}
	}
}
