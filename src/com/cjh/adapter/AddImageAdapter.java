package com.cjh.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cjh.bean.AddImage;
import com.cjh.cjh_sell.R;

public class AddImageAdapter extends BaseAdapter {

	private Context context;
	private List<AddImage> lists;

	public AddImageAdapter(Context context, List<AddImage> lists) {
		this.context = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_add_image, null);
			viewHolder.item_add_imageview = (ImageView) convertView
					.findViewById(R.id.item_add_imageview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.item_add_imageview.setImageBitmap(lists.get(position)
				.getBitmap());
		return convertView;
	}

	private class ViewHolder {
		private ImageView item_add_imageview;
	}

}
