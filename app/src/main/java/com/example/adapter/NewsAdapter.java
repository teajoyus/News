package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entry.NewItem;
import com.example.news.R;
import com.example.util.NewsUtils;
import com.example.util.TableUtils;
import com.nostra13.universalimageloader.cache.disc.impl.BaseDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<NewItem> list;
    private DisplayImageOptions options;
    private boolean isSearch;
    private String keyword;
    public NewsAdapter(Context context, List<NewItem> list) {
        isSearch=false;
        this.context = context;
        this.list = list;
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.new_logo2)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.new_logo2)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.new_logo2)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();
    }
public void setSearch(boolean isSearch){
    this.isSearch = isSearch;
}
    public  void setKeyWord(String keyWord){
        this.keyword = keyWord;
    }
    public void cleanKeyWord(){
        isSearch=false;
        keyword=null;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            //里面只能存放对控件变量的初始化，不能放对tag的设置或者监听事件等等其他，否则会有数据错乱
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title_item);
            holder.comment = (TextView) convertView.findViewById(R.id.tv_comment_item);
            holder.read = (TextView) convertView.findViewById(R.id.tv_read_item);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time_item);
            holder.source = (TextView) convertView.findViewById(R.id.tv_resource_item);
            holder.love = (TextView) convertView.findViewById(R.id.tv_love_item);
            holder.abstrated = (TextView) convertView.findViewById(R.id.tv_abstrated_item);
            holder.picture = (ImageView) convertView.findViewById(R.id.iv_picture_item);
            holder.xiala = (ImageView) convertView.findViewById(R.id.more_xiala__item);
            holder.unlove = (RelativeLayout) convertView.findViewById(R.id.rl_unlove_item);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.unlove.setTag(list.get(position));
        final RelativeLayout unlove = holder.unlove;
        //点击item的下拉的时候，弹出不感兴趣的按钮
        holder.xiala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unlove.getVisibility() != View.GONE) {
                    unlove.setVisibility(View.GONE);

                } else {
                    unlove.setVisibility(View.VISIBLE);
                }
            }
        });
        /**
         * 不感兴趣
         */
        unlove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlove.setVisibility(View.GONE);
                list.remove(v.getTag());
                Log.i("123", "unlove" + list.size());
                NewsAdapter.this.notifyDataSetChanged();

            }
        });
        //如果没有图片的URL，则把图片设置为不可见
        if (list.get(position).getUrlPicture() != null && list.get(position).getUrlPicture().length() > 10) {
            holder.picture.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(list.get(position).getUrlPicture(), holder.picture, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if(bitmap.getHeight()<96&&bitmap.getWidth()<96){
                        view.setVisibility(View.GONE);
                    }

               }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        } else {
            holder.picture.setVisibility(View.GONE);
        }
//      Log.i("1234",list.get(position).getUrlPicture());
//        if(list.get(position).getAbstracted())
        holder.abstrated.setText("    " + list.get(position).getAbstracted());
        holder.read.setText("阅读 ");
        holder.read.append(NewsUtils.textColorSpaned("#EE7621", list.get(position).getRead()));
        holder.love.setText("喜欢 ");
        holder.love.append(NewsUtils.textColorSpaned("#EE7621", list.get(position).getLove()));
        Log.i("123","key="+keyword+isSearch);
        if(isSearch==true){
            showKeyword(holder.title,list.get(position).getTitle(),keyword);
        }else{
            holder.title.setText(list.get(position).getTitle());
        }

        holder.comment.setText("评论 ");
        holder.comment.append(NewsUtils.textColorSpaned("#EE7621", list.get(position).getComment()));
        holder.time.setText(NewsUtils.textColorSpaned("#000000", list.get(position).getTime()));
//        holder.time.append(" 小时前");
        holder.source.setText(list.get(position).getSource());

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView comment;
        TextView time;
        TextView source;
        TextView love;
        TextView read;
        TextView abstrated;
        ImageView picture;
        ImageView xiala;
        RelativeLayout unlove;
    }
    private void  showKeyword(TextView tv,String title,String keyword){
        if (title.contains(keyword)) {

            int index = title.indexOf(keyword);
            int len = keyword.length();
            tv.setText(title.substring(0, index));
//            tv.append(NewsUtils.textColorSpaned("#FF0000",title.substring(index, index + len)));
            tv.append(NewsUtils.textColorSpaned("#4087bf",title.substring(index, index + len)));
            tv.append(title.substring(index + len, title.length()));

        } else {
            tv.setText(title);
        }
    }

}
