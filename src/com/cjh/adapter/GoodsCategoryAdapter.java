package com.cjh.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjh.activity.CategoryDetailsActivity;
import com.cjh.bean.CategoryItem;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 分类列表的适配器
 * @author ps
 *
 */
public class GoodsCategoryAdapter extends BaseAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsCategoryAdapter.class);
	
	private List<CategoryItem> categoryList;
	private Context context;
	private RelativeLayout edit_category_rl;
	private LinearLayout edit_ll;
	private LinearLayout finish_ll;
	private boolean flag = false;

	public GoodsCategoryAdapter(Context context,
			List<CategoryItem> categoryList, RelativeLayout edit_category_rl,
			LinearLayout finish_ll, LinearLayout edit_ll) {
		this.context = context;
		this.categoryList = categoryList;
		this.edit_category_rl = edit_category_rl;
		edit_category_rl.setOnClickListener(new Helper());
		this.finish_ll = finish_ll;
		this.edit_ll = edit_ll;
		finish_ll.setOnClickListener(new FinishHelper());
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
			viewHolder.category_num = (TextView) convertView.findViewById(R.id.category_num);
			viewHolder.category_edit_ll = (LinearLayout) convertView.findViewById(R.id.category_edit_ll);
			viewHolder.category_arrow = (ImageView) convertView.findViewById(R.id.category_arrow);
			viewHolder.good_category_edit = (ImageButton) convertView.findViewById(R.id.good_category_edit);
			viewHolder.good_category_delete = (ImageButton) convertView.findViewById(R.id.good_category_delete);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
	
	//	viewHolder.title_text.setText(categoryList.get(position).getTitle());
		final CategoryItem categoryItem = categoryList.get(position);
		if (flag) {
			viewHolder.category_edit_ll.setVisibility(View.VISIBLE);
			viewHolder.category_arrow.setVisibility(View.GONE);
		} else {
			viewHolder.category_edit_ll.setVisibility(View.GONE);
			viewHolder.category_arrow.setVisibility(View.VISIBLE);
		}
		viewHolder.img_image.setImageBitmap(categoryItem.getBitmap());
		viewHolder.categort_detail.setText(categoryItem.getDetail());
		viewHolder.title_text.setText(categoryItem.getTitle());
		viewHolder.category_num.setText(categoryItem.getNum() + "件商品");
		//编辑分类
		viewHolder.good_category_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,CategoryDetailsActivity.class);
				intent.putExtra("classify_id", categoryItem.getId());
				context.startActivity(intent);
			}
		});
		//删除分类
		viewHolder.good_category_delete
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						new AlertDialog.Builder(context).setTitle("确认")
								.setMessage("确定删除吗？")
								.setPositiveButton("是", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										deleteClassify(categoryItem);
									}
								})
								.setNegativeButton("否", null).show();
						
						
					}
				});
		return convertView;
	}
	
	//查询商品信息
	private void deleteClassify(CategoryItem categoryItem){
		//根据商家ID查询商品信息
		String url = HttpUtil.BASE_URL + "/classify/delete.do?classify_id="+categoryItem.getId();
		try {
			String json = HttpUtil.getRequest(url);
			if(json == null){
				CommonsUtil.showLongToast(context, "删除分类失败！");
			}else{
				CommonsUtil.showLongToast(context, json);
			}
			if("删除分类成功!".equals(json)){
				categoryList.remove(categoryItem);
				this.notifyDataSetChanged();
			}
		} catch (Exception e) {
			LOGGER.error(">>> 删除分类失败",e);
		}
	}
	
	private class ViewHolder{
		private TextView title_text;
		private TextView categort_detail;
		private ImageView img_image;
		private TextView category_num;
		private LinearLayout category_edit_ll;
		private ImageView category_arrow;
		private ImageButton good_category_edit;
		private ImageButton good_category_delete;
	}
	private class Helper implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			flag = true;
			notifyDataSetChanged();
			finish_ll.setVisibility(View.VISIBLE);
			edit_ll.setVisibility(View.GONE);
		}
	}

	private class FinishHelper implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			flag = false;
			notifyDataSetChanged();
			finish_ll.setVisibility(View.GONE);
			edit_ll.setVisibility(View.VISIBLE);

		}
	}
}
