package com.cjh.fragment;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.aframe.ui.widget.KJListView;
import org.kymjs.aframe.ui.widget.KJListView.KJListViewListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cjh.activity.CategoryAddActivity;
import com.cjh.activity.GoodActivity;
import com.cjh.activity.GoodsActivity;
import com.cjh.adapter.GoodsCategoryAdapter;
import com.cjh.auth.SessionManager;
import com.cjh.bean.CategoryItem;
import com.cjh.bean.ClassifyInfo;
import com.cjh.cjh_sell.R;
import com.cjh.utils.CommonsUtil;
import com.cjh.utils.FileUtil;
import com.cjh.utils.HttpUtil;
import com.cjh.utils.JsonUtil;
import com.cjh.utils.PageUtil;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 
 * @author 类别列表
 *
 */
public class GoodsCategoryFragment extends Fragment implements OnClickListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsCategoryFragment.class);
	private RelativeLayout add_category_rl;
	private RelativeLayout edit_category_rl;
	
	private KJListView kjListView;
	private GoodsCategoryAdapter goodsCategoryAdapter;
	private List<CategoryItem> categoryList;
	private LinearLayout finish_ll;
	private LinearLayout edit_ll;
	private int start = PageUtil.START;//分页的开始
	
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
		add_category_rl = (RelativeLayout) contentView.findViewById(R.id.add_category_rl);
		add_category_rl.setOnClickListener(this);
		edit_category_rl = (RelativeLayout) contentView.findViewById(R.id.edit_category_rl);
		edit_category_rl.setOnClickListener(this);
		finish_ll=(LinearLayout) contentView.findViewById(R.id.finish_ll);
		finish_ll.setOnClickListener(this);
		edit_ll=(LinearLayout) contentView.findViewById(R.id.edit_ll);
		edit_ll.setOnClickListener(this);
		kjListView = (KJListView) contentView.findViewById(R.id.good_category_listview);
		
		categoryList=new ArrayList<CategoryItem>();
		goodsCategoryAdapter=new GoodsCategoryAdapter(getActivity(), categoryList,edit_category_rl,finish_ll,edit_ll);
		kjListView.setAdapter(goodsCategoryAdapter);
		//列表点击事件
		kjListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View convertView, int position,
					long len) {
				CategoryItem item = (CategoryItem)parent.getItemAtPosition(position);
				
				Intent intent=new Intent(getActivity(), GoodsActivity.class);
				intent.putExtra("classify_id", item.getId()); 
				intent.putExtra("classify_name", item.getTitle()); 
				intent.putExtra("from", "GoodsCategoryFragment"); 
				startActivity(intent);
			}
		});
		
		//上下拉刷新
		kjListView.setPullLoadEnable(true);
		kjListView.setKJListViewListener(new KJListViewListener() {
			@Override
			public void onRefresh() {//重新刷新
				queryClassify(PageUtil.START);
			}

			@Override
			public void onLoadMore() {//加载更多
				queryClassify(GoodsCategoryFragment.this.start);
			}
		});
		
		
		initData();
		
		return contentView;
	}
	
	private void initData() {
		queryClassify(PageUtil.START);
	}
	
	//根据用户查询分类
	private void queryClassify(int start){
		GoodActivity activity = (GoodActivity)context;
		SessionManager sessionManager = activity.sessionManager;
		int user_id = sessionManager.getInt(SessionManager.KEY_USER_ID);
		
		String url = HttpUtil.BASE_URL + "/classify/querybyuseridpage.do?user_id="+user_id+"&start="+start+"&limit="+PageUtil.LIMIT;
		try {
			String jsons = HttpUtil.getRequest(url);
			if(jsons == null){
				CommonsUtil.showShortToast(getActivity(), "查询分类信息失败");
				kjListView.stopRefreshData();
				return;
			}
			List<ClassifyInfo> list = JsonUtil.parse2ListClassifyInfo(jsons);
			if(list == null){
				LOGGER.warn("转换分类列表信息失败");
				kjListView.stopRefreshData();
				return;
			}
			
			int length = list.size();
			//查询的数据为空时，不作处理
			if(length == 0){
				kjListView.stopRefreshData();
				return;
			}
			
			//默认开始的时候，先清空列表数据
			if(start == PageUtil.START){
				categoryList.clear();
			}
			
			for (int i = 0; i < length; i++) {
				ClassifyInfo classifyInfo = list.get(i);
				CategoryItem categoryItem=new CategoryItem();
				categoryItem.setId(classifyInfo.getClassify_id());
				categoryItem.setNum(classifyInfo.getClassify_num());
				categoryItem.setTitle(classifyInfo.getName());
				categoryItem.setDetail(classifyInfo.getDesc());
				
				String classify_image = classifyInfo.getClassify_image();
				if(classify_image != null && !"".equals(classify_image)){
					categoryItem.setBitmap(FileUtil.getCacheFile(classify_image));
				}
				
				categoryList.add(categoryItem);
			}
			
			this.start += PageUtil.LIMIT;//每次改变start的值 
			goodsCategoryAdapter.notifyDataSetChanged();
			kjListView.stopRefreshData();
		} catch (Exception e) {
			LOGGER.error("查询分类列表失败", e);
			CommonsUtil.showShortToast(getActivity(), "查询分类列表失败");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_category_rl:
			startActivity(new Intent(getActivity(), CategoryAddActivity.class));
			break;

		default:
			break;
		}
	}
}
