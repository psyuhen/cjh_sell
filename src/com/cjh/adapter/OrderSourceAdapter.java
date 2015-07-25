package com.cjh.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cjh.bean.OrderSourceItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.DateUtil;


public class OrderSourceAdapter extends BaseAdapter{
	private List<OrderSourceItem> list;
	private Context context;

	public OrderSourceAdapter(Context context, List<OrderSourceItem> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
					R.layout.item_order_source, null);
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.item_order_source_date);
			viewHolder.pocketcount = (TextView) convertView
					.findViewById(R.id.item_order_source_pocket);
			viewHolder.othercount = (TextView) convertView
					.findViewById(R.id.item_order_source_other);
			viewHolder.totalcount = (TextView) convertView
					.findViewById(R.id.item_order_source_total);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		OrderSourceItem orderSourceItem = list.get(position);
		viewHolder.date.setText(DateUtil.format(orderSourceItem.getDate()));
		viewHolder.pocketcount.setText(orderSourceItem.getPocketcount() + "");
		viewHolder.othercount.setText(orderSourceItem.getOthercount() + "");
		viewHolder.totalcount.setText(orderSourceItem.getAllcount() + "单");
		return convertView;
	}

	class ViewHolder {
		private TextView date;
		private TextView pocketcount;
		private TextView othercount;
		private TextView totalcount;
	}
	
}
