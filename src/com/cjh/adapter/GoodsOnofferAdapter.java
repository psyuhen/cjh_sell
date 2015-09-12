package com.cjh.adapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjh.activity.GoodsViewActivity;
import com.cjh.activity.LimitTimeActivity;
import com.cjh.activity.LimitTimeAddActivity;
import com.cjh.bean.GoodsItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

public class GoodsOnofferAdapter extends BaseAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsOnofferAdapter.class);

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
			viewHolder.title_text = (TextView) convertView.findViewById(R.id.goods_title);
			viewHolder.price_text = (TextView) convertView.findViewById(R.id.goods_price);
			viewHolder.sellmount_text = (TextView) convertView.findViewById(R.id.goods_sell_mount);
			viewHolder.standard_text = (TextView) convertView.findViewById(R.id.goods_standard);
			viewHolder.stock_text = (TextView) convertView.findViewById(R.id.goods_stock);
			viewHolder.img_image = (ImageView) convertView.findViewById(R.id.goods_image);
			viewHolder.good_delete_image_rl = (RelativeLayout) convertView.findViewById(R.id.good_delete_image_rl);
			viewHolder.good_see_image_rl = (RelativeLayout) convertView.findViewById(R.id.good_see_image_rl);
			viewHolder.good_bind_image_rl = (RelativeLayout) convertView.findViewById(R.id.good_bind_image_rl);
			viewHolder.good_share_image_rl = (RelativeLayout) convertView.findViewById(R.id.good_share_image_rl);
			viewHolder.bottm_rl = (LinearLayout) convertView.findViewById(R.id.bottm_rl);
			viewHolder.goods_discount = (TextView) convertView.findViewById(R.id.goods_discount);
			viewHolder.original_price = (TextView) convertView.findViewById(R.id.goods_orignal_price);
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
		final GoodsItem goodsItem = goodsList.get(position);
		
		if (context instanceof LimitTimeActivity) {
			viewHolder.goods_discount.setVisibility(View.VISIBLE);
			viewHolder.goods_discount.setText("8.0折");
			viewHolder.original_price.setText("￥"+ goodsItem.getPrice());
			viewHolder.original_price.setVisibility(View.VISIBLE);
			viewHolder.original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.price_text.setText("￥" + (goodsItem.getPrice() - 10));
		} else {
			viewHolder.price_text.setText("￥" + goodsItem.getPrice());
		}

		viewHolder.title_text.setText(goodsItem.getTitle());
		viewHolder.sellmount_text.setText("总销量"+ goodsItem.getSellmount() + "件");
		viewHolder.stock_text.setText("库存" + goodsItem.getStock()+ "件");
		viewHolder.standard_text.setText("规格:" + goodsItem.getStandard());
		
		if (position % 3 == 1) {
			viewHolder.img_image.setImageResource(R.drawable.c1);
		}
		if (position % 3 == 2) {
			viewHolder.img_image.setImageResource(R.drawable.c2);
		}
		if (position % 3 == 0) {
			viewHolder.img_image.setImageResource(R.drawable.c3);
		}
		//删除
		viewHolder.good_delete_image_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(context).setTitle("确认")
				.setMessage("确定删除吗？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteGoods(goodsItem);
					}
				})
				.setNegativeButton("否", null).show();
			}
		});
		//查看
		viewHolder.good_see_image_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				context.startActivity(new Intent(context, GoodsViewActivity.class));
			}
		});
		//复制
		viewHolder.good_bind_image_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "复制链接", Toast.LENGTH_SHORT).show();
			}
		});
		//分享
		viewHolder.good_share_image_rl.setOnClickListener(new OnClickListener() {
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
		private RelativeLayout good_delete_image_rl;
		private RelativeLayout good_see_image_rl;
		private RelativeLayout good_bind_image_rl;
		private RelativeLayout good_share_image_rl;
		private LinearLayout bottm_rl;
		private TextView goods_discount;
		private TextView original_price;
	}

	//删除商品
	private void deleteGoods(GoodsItem goodsItem){
		String url = HttpUtil.BASE_URL + "/merch/deletebyid.do?merch_id="+goodsItem.getId();
		
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showLongToast(context, "删除商品失败");
				return;
			}
			
			CommonsUtil.showLongToast(context, json);
			
			if("删除商品成功!".equals(json)){
				goodsList.remove(goodsItem);
				this.notifyDataSetChanged();
			}
			
		} catch (InterruptedException e) {
			CommonsUtil.showLongToast(context, "删除商品失败");
			LOGGER.error(">>> 删除商品失败",e);
		} catch (ExecutionException e) {
			CommonsUtil.showLongToast(context, "删除商品失败");
			LOGGER.error(">>> 删除商品失败",e);
		}
	}
}
