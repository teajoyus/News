package com.example.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entry.NewItem;
import com.example.news.R;
import com.example.util.TableUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class LoveAdapter extends BaseAdapter {

	private Context context;
	private List<NewItem> list;
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	public LoveAdapter(Context context, List<NewItem> list) {
		super();
		this.context = context;
		this.list = list;

		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.new_logo2)          // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.new_logo2)  // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.new_logo2)       // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
				.build();
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder holder = null;
		if (convertView == null) {
			inflater = LayoutInflater.from(parent.getContext());
//			if(TableUtils.isTablet(context)) {
//				convertView = inflater.inflate(R.layout.love_item2, null);
//			}else {
//				convertView = inflater.inflate(R.layout.love_item, null);
//
//			}
			convertView = inflater.inflate(R.layout.love_item, null);
			holder = new holder();

			holder.usertime = (TextView) convertView.findViewById(R.id.show_time_love_item);
			holder.title = (TextView) convertView.findViewById(R.id.tv_title_love_item);
			holder.comment = (TextView) convertView.findViewById(R.id.comment_love_item);
			holder.type = (TextView) convertView.findViewById(R.id.type_love_item);
			holder.love = (TextView) convertView.findViewById(R.id.love_love_item);
			holder.read = (TextView) convertView.findViewById(R.id.read_love_item);
			holder.abstarted = (TextView) convertView.findViewById(R.id.tv_abstrated_item);
			holder.picture = (ImageView) convertView.findViewById(R.id.iv_love_item);
			convertView.setTag(holder);
		} else {
			holder = (holder) convertView.getTag();
		}
		holder.usertime.setText(list.get(position).getUserTime());
	String title = list.get(position).getTitle();
		holder.title.setText(title);
		//if(title.length()<24){
			//Log.i("1234",title.length()+"--"+title+"=="+position);
			holder.abstarted.setText("    "+list.get(position).getAbstracted());
		//}

		holder.read.setText("阅读 ");
		holder.read.append(stringColorSpaned("#EE7621",list.get(position).getRead()));
		holder.love.setText("喜欢 ");
		holder.love.append(stringColorSpaned("#EE7621",list.get(position).getLove()));
		holder.title.setText(list.get(position).getTitle());
		holder.comment.setText("评论 ");
		holder.comment.append(stringColorSpaned("#EE7621",list.get(position).getComment()));
		holder.type.setText("类型:");
		holder.type.append(stringColorSpaned("#EE7621",list.get(position).getLabel()));
		//holder.picture.setImageResource(R.drawable.shili);
		System.out.println(position + "==" + list.get(position).getUrlPicture());
		if(list.get(position).getUrlPicture()==null||list.get(position).getUrlPicture().length()<8){
			holder.picture.setVisibility(View.GONE);
		}else {
			holder.picture.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(list.get(position).getUrlPicture(), holder.picture, options, null);
		}
		return convertView;
	}

	 class holder {
		TextView title;
		TextView comment;
		TextView usertime;
		TextView abstarted;
		TextView type;
		TextView love;
		TextView read;
		ImageView picture;
	}
	private Spanned stringColorSpaned(String color, String source){
		String test =  "<font color= '"+color+"'>"+source+"</font>";
		return Html.fromHtml(test);
	}
}
