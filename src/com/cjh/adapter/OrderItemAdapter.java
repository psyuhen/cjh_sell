package com.cjh.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cjh.activity.ChatActivity;
import com.cjh.bean.OrderItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.DateUtil;

public class OrderItemAdapter extends BaseAdapter {
	private List<OrderItem> orderlist;
	private Context context;

	public OrderItemAdapter(Context context, List<OrderItem> orderlist) {
		this.context = context;
		this.orderlist = orderlist;
	}

	@Override
	public int getCount() {
		return orderlist.size();
	}

	@Override
	public OrderItem getItem(int position) {
		return orderlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_in_order, null);
			viewHolder.ordertime_text = (TextView) convertView
					.findViewById(R.id.order_time);
			viewHolder.serialnumber_text = (TextView) convertView
					.findViewById(R.id.order_serial);
//			viewHolder.img_image = (ImageView) convertView
//					.findViewById(R.id.order_item_image);
			viewHolder.buyer_text = (TextView) convertView
					.findViewById(R.id.order_buyer);
			viewHolder.number_text = (TextView) convertView
					.findViewById(R.id.order_number);
			viewHolder.title_text = (TextView) convertView
					.findViewById(R.id.order_title);
			viewHolder.price_text = (TextView) convertView
					.findViewById(R.id.order_price);
			viewHolder.order_complete_btn = (Button) convertView
					.findViewById(R.id.order_complete_btn);
			viewHolder.order_communication_btn = (Button) convertView
					.findViewById(R.id.order_communication_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final OrderItem orderItem = orderlist.get(position);
		viewHolder.ordertime_text.setText(DateUtil.format(orderItem.getOrdertime()));
		viewHolder.serialnumber_text.setText(orderItem.getSerialnum());
		viewHolder.buyer_text.setText(orderItem.getBuyer());
		viewHolder.price_text.setText(orderItem.getPrice() + "");
		viewHolder.number_text.setText(orderItem.getNum() + "");
		viewHolder.title_text.setText(orderItem.getGoodtitle());

		viewHolder.order_complete_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "订单已完成", Toast.LENGTH_SHORT).show();
			}
		});
		viewHolder.order_communication_btn
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, ChatActivity.class);
						intent.putExtra("buyer_user_id", orderItem.getBuyer_user_id());
						intent.putExtra("buyer_user_name", orderItem.getBuyer());
						intent.putExtra("buyer_user_mobile", orderItem.getBuyer_user_mobile());
						context.startActivity(intent);
					}
				});
		return convertView;
	}

	private class ViewHolder {
		private TextView buyer_text;
		private TextView ordertime_text;
		private TextView serialnumber_text;
	//	private ImageView img_image;
		private TextView price_text;
		private TextView title_text;
		private TextView number_text;
		private Button order_complete_btn;
		private Button order_communication_btn;
	}

}
