package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.example.util.BitmapUtils;
import com.example.util.NewsUtils;
import com.example.util.TableUtils;
import com.nostra13.universalimageloader.cache.disc.impl.BaseDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class NewsAdapter extends BaseAdapter {
  private Context context;
  private List<NewItem> list;
  private boolean isSearch;
  private String keyword;
  private boolean scrollState;

  public NewsAdapter(Context context, List<NewItem> list) {
    isSearch = false;
    this.context = context;
    this.list = list;
    scrollState = false;
  }

  public void setSearch(boolean isSearch) {
    this.isSearch = isSearch;
  }

  public void setKeyWord(String keyWord) {
    this.keyword = keyWord;
  }
  public void cleanKeyWord() {
    isSearch = false;
    keyword = null;
  }

  /**
   * 获取listview的滚动状态
   *
   * @param scrollState
   */
  public void setScrollState(boolean scrollState) {
    this.scrollState = scrollState;
    Log.i("setScrollState", "scrollState:" + scrollState);
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
  public View getView(final int position, View convertView, ViewGroup parent) {
    convertView =  initView(convertView);//初始化布局
    ViewHolder holder = (ViewHolder) convertView.getTag();//拿到封装好的控件item
    showText(holder, position);//设置文本标签
    setListener(holder, position);//设置监听事件
    showImage(holder.picture,position);//设置图片
    LogUtil.i("position:" + position);
    return convertView;
  }

  /**
   * 初始化item控件，返回item控件类
   *
   * @param convertView
   * @return ViewHolder
   */
  private View initView(View convertView) {
    //如果不为空就返回复用convertView
    if (convertView != null) {
      return convertView;
    }
    //里面只能存放对控件变量的初始化，不能放对tag的设置或者监听事件等等其他，否则会有数据错乱
    convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null, false);
    ViewHolder holder = new ViewHolder();
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
    return convertView;
  }

  /**
   * 把数据的文字显示到item上
   *
   * @param holder
   * @param position
   */
  private void showText(ViewHolder holder,  final int position) {
    holder.abstrated.setText("    " + list.get(position).getAbstracted());
    holder.read.setText("阅读 ");
    holder.read.append(NewsUtils.textColorSpaned("#EE7621", list.get(position).getRead()));
    holder.love.setText("喜欢 ");
    holder.love.append(NewsUtils.textColorSpaned("#EE7621", list.get(position).getLove()));
    holder.comment.setText("评论 ");
    holder.comment.append(NewsUtils.textColorSpaned("#EE7621", list.get(position).getComment()));
    holder.time.setText(NewsUtils.textColorSpaned("#000000", list.get(position).getTime()));
    holder.source.setText(list.get(position).getSource());
    if (isSearch == true) {
      showKeyword(holder.title, list.get(position).getTitle(), keyword);
    } else {
      holder.title.setText(list.get(position).getTitle());
    }
  }

  private void setListener(final ViewHolder holder, final int position) {
    holder.unlove.setTag(list.get(position));
    //点击item的下拉的时候，弹出或收起不感兴趣的按钮
    holder.xiala.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        holder.unlove.setVisibility(holder.unlove.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
      }
    });
    /**
     * 不感兴趣
     */
    holder.unlove.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        holder.unlove.setVisibility(View.GONE);
        list.remove(v.getTag());
        LogUtil.i("unlove" + list.size());
        NewsAdapter.this.notifyDataSetChanged();
      }
    });
  }

  /**
   * 显示新闻网络图片
   *
   * @param view
   * @param  position
   */
  private void showImage(final ImageView view, final int position) {
    String url =  list.get(position).getUrlPicture();
    //如果有url则加载、没有url则隐藏掉控件
    if (!TextUtils.isEmpty(url)) {
      view.setVisibility(View.VISIBLE);
      // view.setImageResource(R.drawable.new_logo2);
      ImageLoader.getInstance().displayImage(url, view, new myImageLoadingListener());
    } else {
      view.setVisibility(View.GONE);
    }

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

  /**
   * 设置textview中关键字高亮
   *
   * @param tv
   * @param title
   * @param keyword
   */
  private void showKeyword(TextView tv, String title, String keyword) {
    if (title.contains(keyword)) {
      int index = title.indexOf(keyword);
      int len = keyword.length();
      tv.setText(title.substring(0, index));
//            tv.append(NewsUtils.textColorSpaned("#FF0000",title.substring(index, index + len)));
      tv.append(NewsUtils.textColorSpaned("#4087bf", title.substring(index, index + len)));
      tv.append(title.substring(index + len, title.length()));
    } else {
      tv.setText(title);
    }
  }

  /**
   * 图片下载过程的监听，用于过滤掉小图标图片
   */
  class myImageLoadingListener implements ImageLoadingListener {
    @Override
    public void onLoadingStarted(String s, View view) {
    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {
    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
      if (bitmap.getHeight() < 96 && bitmap.getWidth() < 96) {
        view.setVisibility(View.GONE);
      }
    }

    @Override
    public void onLoadingCancelled(String s, View view) {
    }
  }

}
