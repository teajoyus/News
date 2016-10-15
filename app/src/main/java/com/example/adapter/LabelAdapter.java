package com.example.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.entry.Label;
import com.example.news.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 */
public class LabelAdapter extends BaseAdapter {
    private Context mContext;
    private List<Label> list;
    public LabelAdapter(Context context, List<Label> list) {
        this.mContext = context;
        this.list = list;
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
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //定义一个ImageView,显示在GridView里
        final TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(150, 90));
            textView.setTextSize(16);
            if(list.get(position).isSelected()){
                textView.setTextColor(0xA0EE7621);
            }else{
                textView.setTextColor(0xA0000000);
            }
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.label_select_shape);

        } else {
            textView = (TextView) convertView;
        }
        textView.setText(list.get(position).getName());
        return textView;
    }


}