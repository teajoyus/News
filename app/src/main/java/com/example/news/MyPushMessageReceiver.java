package com.example.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.util.NewsSharedPreferences;

import cn.bmob.push.*;

/**
 * Created by LiangCha on 2016/7/31.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        NewsSharedPreferences preferences = new NewsSharedPreferences(context,"setting");
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)&&"can".equals(preferences.load("push"))){
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            Toast.makeText(context,intent.getStringExtra("msg"),Toast.LENGTH_LONG).show();
        }
        preferences=null;
    }

}