package com.cjh.adapter;

import java.util.List;

import com.cjh.bean.CategoryItem;
import com.cjh.cjh_sell.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 分类列表的适配器
 * @author ps
 *
 */
public class GoodsCategoryAdapter extends BaseAdapter{
	private List<CategoryItem> categoryList;
	private Context context;
	public GoodsCategoryAdapter(Context context,List<CategoryItem> categoryList) {
		this.context=context;
		this.categoryList=categoryList;
	}
	@Override
	public int getCount() {
		return categoryList.size();
	}
	@Override
	public CategoryItem getItem(int position) {
		return categoryList.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_good_category, null);
			viewHolder.img_image=(ImageView) convertView.findViewById(R.id.category_image);
			viewHolder.categort_detail=(TextView) convertView.findViewById(R.id.category_detail);
			viewHolder.title_text=(TextView) convertView.findViewById(R.id.category_title);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
	
	//	viewHolder.title_text.setText(categoryList.get(position).getTitle());
		CategoryItem categoryItem = categoryList.get(position);
		viewHolder.img_image.setImageResource(R.drawable.goods_fruit);
		viewHolder.title_text.setText(categoryItem.getTitle());
		viewHolder.categort_detail.setText(categoryItem.getDetail());
		
		/*if (position%5==0) {
			viewHolder.img_image.setImageResource(R.drawable.goods_fruit);
			viewHolder.title_text.setText("果蔬生鲜");
			viewHolder.categort_detail.setText("蔬菜水果 肉蛋海鲜 速冻包装 西点面包...");
		}
		if (position%5==1) {
			viewHolder.img_image.setImageResource(R.drawable.goods_nutrition);
			viewHolder.title_text.setText("家庭营养");
			viewHolder.categort_detail.setText("奶类制品 维生素类 果汁鲜榨 蛋白质粉...");
		}
		if (position%5==2) {
			viewHolder.img_image.setImageResource(R.drawable.goods_tonic);
			viewHolder.title_text.setText("滋补养生");
			viewHolder.categort_detail.setText("参茶燕窝 生熟普洱 阿胶蜂 龟苓凉茶...");
		}
		if (position%5==3) {
			viewHolder.img_image.setImageResource(R.drawable.goods_packing);
			viewHolder.title_text.setText("包装食品");
			viewHolder.categort_detail.setText("休闲食品 方便食品 饮料酒水 干果调料...");
		}
		if (position%5==4) {
			viewHolder.img_image.setImageResource(R.drawable.goods_nurse);
			viewHolder.title_text.setText("个人护理");
			viewHolder.categort_detail.setText("蔬菜水果 肉蛋海鲜 速冻包装 西点面包...");
		}*/
		
		return convertView;
	}
	private class ViewHolder{
		private TextView title_text;
		private TextView categort_detail;
		private ImageView img_image;
	}
}
