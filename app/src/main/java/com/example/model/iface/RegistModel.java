package com.example.model.iface;

import android.content.Context;

import com.example.entry.User;
import com.example.iface.OnRegistListener;

/**
 * Created by Administrator on 2016/8/8.
 */
public interface RegistModel {
     void regist(User user,OnRegistListener listener);
     void requestCode(Context context,String phone,OnRegistListener listener);
    void sendCode(Context context,String phone,String code,OnRegistListener listener);
}
