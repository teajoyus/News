package com.example.myview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.entry.Label;

import static com.example.news.R.*;

public class NewsScrollView {
	
	private HorizontalScrollView scrollView;
	private LinearLayout linearLayout;
	private Context context;
	private OnItemClickListener listener;
	/** tab名称 */
	private List<Label> newsClassify = new ArrayList<Label>();
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	public NewsScrollView(Context context,int scrollView,
			int linearLayout) {
		Activity a = (Activity)context;
		this.context=context;
		this.scrollView =(HorizontalScrollView) a.findViewById(scrollView);
		this.linearLayout = (LinearLayout) a.findViewById(linearLayout);
		WindowManager manager = ((Activity)context).getWindowManager();
		mScreenWidth = manager.getDefaultDisplay().getWidth();
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		
	}
	public void initView(){
		initTabColumn();
	}
	public List<Label> getNewsClassify() {
		return newsClassify;
	}
	public void setNewsClassify(List<Label> newsClassify) {
		this.newsClassify = newsClassify;
	}
	public void removeAllView(){
		linearLayout.removeAllViews();
	}
	public void notifyDatasetChange(){
		removeAllView();
		//Toast.makeText(context, this.newsClassify.size(), 0).show();
		initView();
		
		
	}
	private void initTabColumn() {
		linearLayout.removeAllViews();
		int count = newsClassify.size();
		for (int i = 0; i < count; i++) {
			TextView columnTextView = new TextView(context);
			columnTextView.setTextAppearance(context, style.top_category_scroll_view_item_text);
			// localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setId(1000+i);
			columnTextView.setText(newsClassify.get(i).getName());
			columnTextView.setTag(newsClassify.get(i).getKey());
			// 下横杠背景（想要哪种效果打开哪个）
			/*
			 columnTextView.setBackgroundResource(R.drawable.tab_indicator);
			 columnTextView.setTextColor(context.getResources().getColorStateList(R.color.top_category_scroll_text_color_day2));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, LayoutParams.MATCH_PARENT);
			*/
			//==============
			// 颜色方块背景
			columnTextView.setBackgroundResource(drawable.radio_buttong_bg);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setTextColor(context.getResources().getColorStateList(color.top_category_scroll_text_color_day));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, LayoutParams.WRAP_CONTENT);
		//=======================
			params.leftMargin = 5;
			params.rightMargin = 5;
			if (columnSelectIndex == i) {
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int position =0;
					for (int i = 0; i < linearLayout.getChildCount(); i++) {
						View localView = linearLayout.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else {
							localView.setSelected(true);
							position = i;

						}
					}
					Log.i("123",position+"");
					if(listener!=null)listener.OnItemClickLister(position,v);

				}
			});
			linearLayout.addView(columnTextView, i, params);
		}

	}


	public void  setOnItemClickListener(OnItemClickListener listener) {
		this.listener =listener;
	}
	public interface OnItemClickListener{
		public void OnItemClickLister(int position, View v);
	} 
}
