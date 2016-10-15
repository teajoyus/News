package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entry.Quan;
import com.example.https.HttpRequest;
import com.example.news.LoginActivity;
import com.example.news.LoveActivity;
import com.example.news.R;
import com.example.runtime.RunTime;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class QuanAdapter extends BaseAdapter {
    private Context context;
    private List<Quan> list;
    private DisplayImageOptions options;
    public QuanAdapter(Context context, List<Quan> list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.quan_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name_quan_item);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content_quan_item);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time_quan_item);
            holder.read = (TextView) convertView.findViewById(R.id.tv_read_quan_item);
            holder.zan = (TextView) convertView.findViewById(R.id.tv_zan_quan_item);
            holder.avator=(ImageView)convertView.findViewById(R.id.iv_avatar_quan_item);
            holder.newTitle = (TextView) convertView.findViewById(R.id.tv_title_quan_item);
            holder.newAbstract = (TextView) convertView.findViewById(R.id.tv_abstrated_quan_item);
            holder.newPicture=(ImageView)convertView.findViewById(R.id.iv_picture_quan_item);
            holder.comment = (TextView) convertView.findViewById(R.id.tv_comment_quan_item);
            holder.tiji = (TextView) convertView.findViewById(R.id.tv_tiji_quan_item);
            holder.ll_quan_item = (LinearLayout) convertView.findViewById(R.id.ll_quan_item);
            final TextView tiji =  holder.tiji;
            final LinearLayout ll_quan_item =  holder.ll_quan_item;
            holder.tiji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = (String) tiji.getTag();
                    if(s==null||"".equals(s)){
                        ll_quan_item.setVisibility(View.GONE);
                        Drawable nav_up=context.getResources().getDrawable(R.drawable.down_quan_item);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        tiji.setCompoundDrawables(null, null, nav_up, null);
                        tiji.setTag("1");
                    }else{
                        ll_quan_item.setVisibility(View.VISIBLE);
                        Drawable nav_up=context.getResources().getDrawable(R.drawable.top_quan_item);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        tiji.setCompoundDrawables(null, null, nav_up, null);
                        tiji.setTag("");
                    }

                }
            });
            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.name.setText(list.get(position).getName());
        holder.time.setText(list.get(position).getTime());
        holder.content.setText("    "+list.get(position).getContent());
        holder.read.setText("浏览 "+list.get(position).getReadNum());
        holder.zan.setText("点赞 "+list.get(position).getZanNum());
        holder.newTitle.setText(list.get(position).getNewTitle());
        holder.newAbstract.setText("    "+list.get(position).getNewAbstract());
        if(list.get(position).getNewPicture()!=null&&list.get(position).getNewPicture().length()>3){
            holder.newPicture.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(list.get(position).getNewPicture(),holder.newPicture,options,null);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView content;
        TextView time;
        TextView read;
        TextView zan;
        ImageView avator;
        TextView newTitle;
        TextView newAbstract;
        ImageView newPicture;
        TextView comment;
        TextView tiji;
        LinearLayout ll_quan_item;

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
            HttpRequest.getInstance().zan(new String[]{comment_userId,newId, time,content});
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
