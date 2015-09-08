package com.cjh.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjh.activity.GoodsViewActivity;
import com.cjh.activity.LimitTimeActivity;
import com.cjh.activity.LimitTimeAddActivity;
import com.cjh.bean.GoodsItem;
import com.cjh.cjh_sell.R;

public class GoodsOnofferAdapter extends BaseAdapter {
	private List<GoodsItem> goodsList;
	private Context context;

	public GoodsOnofferAdapter(List<GoodsItem> goodsList, Context context) {
		this.goodsList = goodsList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return goodsList.size();
	}

	@Override
	public GoodsItem getItem(int position) {
		return goodsList.get(position);
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
					R.layout.item_onoffer_goods, null);
			viewHolder.title_text = (TextView) convertView
					.findViewById(R.id.goods_title);
			viewHolder.price_text = (TextView) convertView
					.findViewById(R.id.goods_price);
			viewHolder.sellmount_text = (TextView) convertView
					.findViewById(R.id.goods_sell_mount);
			viewHolder.standard_text = (TextView) convertView
					.findViewById(R.id.goods_standard);
			viewHolder.stock_text = (TextView) convertView
					.findViewById(R.id.goods_stock);
			viewHolder.img_image = (ImageView) convertView
					.findViewById(R.id.goods_image);
			viewHolder.goods_see = (ImageButton) convertView
					.findViewById(R.id.good_see_imagebtn);
			viewHolder.goods_bind = (ImageButton) convertView
					.findViewById(R.id.good_bind_imagebtn);
			viewHolder.goods_share = (ImageButton) convertView
					.findViewById(R.id.good_share_imagebtn);
			viewHolder.bottm_rl = (LinearLayout) convertView
					.findViewById(R.id.bottm_rl);
			viewHolder.goods_discount = (TextView) convertView
					.findViewById(R.id.goods_discount);
			viewHolder.original_price = (TextView) convertView
					.findViewById(R.id.goods_orignal_price);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		/**
		 * 如果为选择商品，则隐藏下方选择按钮
		 */
		if (context instanceof LimitTimeAddActivity) {
			viewHolder.bottm_rl.setVisibility(View.GONE);
		}
		/**
		 * 限时折扣则显示折扣和原始价格
		 */
		if (context instanceof LimitTimeActivity) {
			viewHolder.goods_discount.setVisibility(View.VISIBLE);
			viewHolder.goods_discount.setText("8.0折");
			viewHolder.original_price.setText("￥"
					+ goodsList.get(position).getPrice());
			viewHolder.original_price.setVisibility(View.VISIBLE);
			viewHolder.original_price.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.price_text.setText("￥"
					+ (goodsList.get(position).getPrice() - 10));
		} else {
			viewHolder.price_text.setText("￥"
					+ goodsList.get(position).getPrice());
		}

		viewHolder.title_text.setText(goodsList.get(position).getTitle());

		viewHolder.sellmount_text.setText("总销量"
				+ goodsList.get(position).getSellmount() + "件");
		viewHolder.stock_text.setText("库存" + goodsList.get(position).getStock()
				+ "件");
		viewHolder.standard_text.setText("规格:"
				+ goodsList.get(position).getStandard());
		if (position % 3 == 1) {
			viewHolder.img_image.setImageResource(R.drawable.c1);
		}
		if (position % 3 == 2) {
			viewHolder.img_image.setImageResource(R.drawable.c2);
		}
		if (position % 3 == 0) {
			viewHolder.img_image.setImageResource(R.drawable.c3);
		}
		viewHolder.goods_see.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				context.startActivity(new Intent(context, GoodsViewActivity.class));
			}
		});
		viewHolder.goods_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "复制链接", Toast.LENGTH_SHORT).show();
			}
		});
		viewHolder.goods_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "分享商品", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	private class ViewHolder {
		private TextView title_text;
		private TextView price_text;
		private TextView standard_text;
		private TextView sellmount_text;
		private TextView stock_text;
		private ImageView img_image;
		private ImageButton goods_see;
		private ImageButton goods_bind;
		private ImageButton goods_share;
		private LinearLayout bottm_rl;
		private TextView goods_discount;
		private TextView original_price;
	}

}
