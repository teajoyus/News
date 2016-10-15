package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entry.CommentItem;
import com.example.https.HttpRequest;
import com.example.news.LoginActivity;
import com.example.news.LoveActivity;
import com.example.news.R;
import com.example.runtime.RunTime;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26 0026.
 */

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<CommentItem> list;
    private Animation animation;
    private DisplayImageOptions options;
    public CommentAdapter(Context context, List<CommentItem> list) {
        this.context = context;
        this.list = list;
        animation = AnimationUtils.loadAnimation(context, R.anim.nn);
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.avatar)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.avatar)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.avatar)       // 设置图片加载或解码过程中发生错误显示的图片
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
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name_comment);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content_comment);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time_comment);
            holder.jia = (TextView) convertView.findViewById(R.id.tv_add_one_comment);
            holder.zan_num = (TextView) convertView.findViewById(R.id.tv_zan_number_comment);
            holder.picture = (ImageView) convertView.findViewById(R.id.iv_picture_comment);
            holder.zan = (ImageView) convertView.findViewById(R.id.iv_add_one_comment);
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LoveActivity.class);
                    intent.putExtra("userid",list.get(position).getUserId());
                    context.startActivity(intent);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.name.setText(list.get(position).getName());
        holder.time.setText(list.get(position).getTime());
        holder.content.setText(list.get(position).getContent());
        if(list.get(position).getAvatarUrl()!=null&&list.get(position).getAvatarUrl().length()>10) {
            ImageLoader.getInstance().displayImage(list.get(position).getAvatarUrl(), holder.picture, options, null);
        }
        final boolean isZan = list.get(position).isZan();
        if (isZan) {
            holder.zan.setImageResource(R.drawable.love_selected);
        } else {
            holder.zan.setImageResource(R.drawable.love_unselected);

        }
        final TextView tv = holder.jia;
        final TextView num = holder.zan_num;
        final ImageView iv = holder.zan;
        final int p = position;
        num.setText(list.get(p).getNum());
        holder.zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果该用户已经赞过的话 则不能在点赞
                if (list.get(position).isZan()) {
                    Toast.makeText(context,"您已经赞过该条评论",Toast.LENGTH_SHORT).show();
                    return;
                } else if (!RunTime.isUserLogin()) {
                    //没有登录则提示用户
                    alertLogin();
                    return;
                }
                list.get(position).setZan(true);
                //点赞+1的效果
                tv.setVisibility(View.VISIBLE);
                tv.startAnimation(animation);
                iv.setImageResource(R.drawable.love_selected);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        tv.setVisibility(View.GONE);
                        num.setText(Integer.parseInt(num.getText().toString()) + 1 + "");
                    }
                }, 1000);

                String time =list.get(p).getTime();
                String content = list.get(p).getContent();
                System.out.println(time);
                String newId = list.get(p).getNewId();
                String comment_userId = list.get(p).getUserId();
                //点赞后把数据发给服务端
                new ZanThread(content, newId, comment_userId,time).start();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView content;
        TextView time;
        TextView jia;
        TextView zan_num;
        ImageView zan;
        ImageView picture;

    }

    private Spanned stringColorSpaned(String color, String source) {
        String test = "<font color= '" + color + "'>" + source + "</font>";
        return Html.fromHtml(test);
    }

    class ZanThread extends Thread {
        private String content;
        private String newId;
        private String comment_userId;
        private String time;

        public ZanThread(String content, String newId, String comment_userId,String time) {

            this.content = content;
            this.newId = newId;
            this.comment_userId = comment_userId;
            this.time=time;

        }

        @Override
        public void run() {
            try {
                HttpRequest.getInstance().zan(new String[]{comment_userId,newId, URLEncoder.encode(time,"utf-8"), URLEncoder.encode(content,"utf-8")});
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void alertLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("没有权限");
        builder.setMessage("对不起，您还没有登录，不能对评论点赞。您要登录吗");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        builder.show();
    }
}
