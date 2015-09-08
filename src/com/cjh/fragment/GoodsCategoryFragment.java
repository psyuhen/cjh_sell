package com.cjh.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.kymjs.aframe.ui.widget.KJListView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cjh.activity.CategoryDetailsActivity;
import com.cjh.activity.GoodActivity;
import com.cjh.adapter.GoodsCategoryAdapter;
import com.cjh.auth.SessionManager;
import com.cjh.bean.CategoryItem;
import com.cjh.bean.ClassifyInfo;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 
 * @author 类别列表
 *
 */
public class GoodsCategoryFragment extends Fragment{
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsCategoryFragment.class);

	private KJListView kjListView;
	private GoodsCategoryAdapter goodsCategoryAdapter;
	private List<CategoryItem> categoryList;
	private Context context;
	public GoodsCategoryFragment(Context context) {
		this.context = context;
	}
	
	/*public static GoodsCategoryFragment newInstance() {
		GoodsCategoryFragment fragment = new GoodsCategoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }*/
	
	
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
		
		queryClassify();
	}
	
	//根据用户查询分类
	private void queryClassify(){
		GoodActivity activity = (GoodActivity)context;
		SessionManager sessionManager = activity.sessionManager;
		int user_id = sessionManager.getInt(SessionManager.KEY_USER_ID);
		
		String url = HttpUtil.BASE_URL + "/classify/querybyuserid.do?user_id="+user_id;
		try {
			String jsons = HttpUtil.getRequest(url);
			if(jsons == null){
				CommonsUtil.showShortToast(getActivity(), "查询分类信息失败");
				return;
			}
			List<ClassifyInfo> list = JsonUtil.parse2ListClassifyInfo(jsons);
			if(list == null){
				LOGGER.warn("转换分类列表信息失败");
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
			LOGGER.error("查询分类列表失败", e);
			CommonsUtil.showShortToast(getActivity(), "查询分类列表失败");
		} catch (ExecutionException e) {
			LOGGER.error("查询分类列表失败", e);
			CommonsUtil.showShortToast(getActivity(), "查询分类列表失败");
		}
	}
}
