package com.cjh.adapter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cjh.bean.ChatMsgItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.DateUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 聊天窗口适配器
 * @author ps
 *
 */
public class ChatMsgViewAdapter extends BaseAdapter {
	private Logger LOGGER = LoggerFactory.getLogger(ChatMsgViewAdapter.class);

	private List<ChatMsgItem> data;
	private Context context;
	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context, List<ChatMsgItem> data) {
		this.context = context;
		this.data = data;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMsgItem entity = data.get(position);
		boolean isComing = entity.isComing();
		ViewHolder viewHolder = null;

		if (convertView == null) {
			if (isComing) {
				convertView = mInflater.inflate(R.layout.chatting_item_msg_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.chatting_item_msg_right, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.msg_sendtime = (TextView) convertView.findViewById(R.id.msg_sendtime);
			viewHolder.msg_text = (TextView) convertView.findViewById(R.id.msg_text);
			viewHolder.msg_username = (TextView) convertView.findViewById(R.id.msg_username);
			viewHolder.isComing = isComing;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String regular = "appkefu_f0[0-9]{2}";
		String str = entity.getContent();
		SpannableString spannableString = getExpressionString(context, str, regular);

		viewHolder.msg_text.setText(spannableString);
		viewHolder.msg_sendtime.setText(DateUtil.format(entity.getSendDate()));
		// viewHolder.msg_text.setText(entity.getText());
		viewHolder.msg_username.setText(entity.getSendUser());
		return convertView;
	}

	public SpannableString getExpressionString(Context context,
			String str, String regular) {
		SpannableString spannableString = new SpannableString(str);
		Pattern pattern = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
		dealExpression(context, spannableString, pattern, 0);
		return spannableString;
	}

	public void dealExpression(Context context,
			SpannableString spannableString, Pattern pattern, int start) {
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			if (matcher.start() < start) {
				continue;
			}
			try {
				Field field = R.drawable.class.getDeclaredField(key);
				int resId = Integer.parseInt(field.get(null).toString());
				if (resId != 0) {
					Bitmap bitmap = BitmapFactory.decodeResource(
							context.getResources(), resId);
					@SuppressWarnings("deprecation")
					ImageSpan imageSpan = new ImageSpan(bitmap);
					int end = matcher.start() + key.length();
					spannableString.setSpan(imageSpan, matcher.start(), end,
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					if (end < spannableString.length()) {
						dealExpression(context, spannableString, pattern, end);
					}
					break;
				}
			} catch (Exception e) {
				LOGGER.error("显示表情出错", e);
			}

		}
	}

	class ViewHolder {
		public boolean isComing = true;
		public TextView msg_sendtime;
		public TextView msg_username;
		public TextView msg_text;
	}
}
