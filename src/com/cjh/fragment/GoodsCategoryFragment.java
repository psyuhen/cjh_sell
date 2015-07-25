package com.cjh.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.kymjs.aframe.ui.widget.KJListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cjh.activity.CategoryDetailsActivity;
import com.cjh.adapter.GoodsCategoryAdapter;
import com.cjh.bean.CategoryItem;
import com.cjh.bean.ClassifyInfo;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;

/**
 * 
 * @author 类别列表
 *
 */
public class GoodsCategoryFragment extends Fragment{
	public static final String TAG = "GoodsCategoryFragment";
	
	private KJListView kjListView;
	private GoodsCategoryAdapter goodsCategoryAdapter;
	private List<CategoryItem> categoryList;
	
	public static GoodsCategoryFragment newInstance() {
		GoodsCategoryFragment fragment = new GoodsCategoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView=inflater.inflate(R.layout.view_goods_category, container,false);
		initData();
		kjListView=(KJListView) contentView.findViewById(R.id.good_category_listview);
		goodsCategoryAdapter=new GoodsCategoryAdapter(getActivity(), categoryList);
		kjListView.setAdapter(goodsCategoryAdapter);
		kjListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View convertView, int position,
					long len) {
				CategoryItem item = (CategoryItem)parent.getItemAtPosition(position);
				
				Intent intent=new Intent(getActivity(), CategoryDetailsActivity.class);
				intent.putExtra("classify_id", item.getId()); 
				startActivity(intent);
			}
		});
		return contentView;
	}
	
	private void initData() {
		categoryList=new ArrayList<CategoryItem>();
		
		String url = HttpUtil.BASE_URL + "/classify/querybytype.do?classify_type="+1;
		try {
			String jsons = HttpUtil.getRequest(url);
			if(jsons == null){
				CommonsUtil.showShortToast(getActivity(), "查询分类信息失败");
				return;
			}
			List<ClassifyInfo> list = JsonUtil.parse2ListClassifyInfo(jsons);
			if(list == null){
				Log.w(TAG, "转换分类列表信息失败");
				return;
			}
			for (ClassifyInfo classifyInfo : list) {
				CategoryItem categoryItem=new CategoryItem();
				categoryItem.setId(classifyInfo.getClassify_id());
				categoryItem.setNum(classifyInfo.getClassify_num());
				categoryItem.setTitle(classifyInfo.getName());
				
				categoryList.add(categoryItem);
			}
		} catch (InterruptedException e) {
			Log.e(TAG, "查询分类列表失败", e);
			CommonsUtil.showShortToast(getActivity(), "查询分类列表失败");
		} catch (ExecutionException e) {
			Log.e(TAG, "查询分类列表失败", e);
			CommonsUtil.showShortToast(getActivity(), "查询分类列表失败");
		}
		/*for (int i = 0; i < 10; i++) {
			CategoryItem categoryItem=new CategoryItem();
			categoryItem.setId(i);
			categoryItem.setImg("temp");
			categoryItem.setNum(i*32+45);
			if (i%4==0) {
				categoryItem.setTitle("����");
			}
			if (i%4==1) {
				categoryItem.setTitle("����");
			}
			if (i%4==2) {
				categoryItem.setTitle("ˮ��");
			}
			if (i%4==3) {
				categoryItem.setTitle("��Ĥ");
			}
			categoryList.add(categoryItem);
		}*/
	}
}
