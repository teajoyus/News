package com.example.model.impl;

import com.example.api.NewsAPI;
import com.example.constant.Constant;
import com.example.entry.Message;
import com.example.entry.ParamsPair;
import com.example.entry.User;
import com.example.iface.OnLoginListener;
import com.example.model.iface.LoginModel;
import com.example.util.OKHttpUtil;
import com.example.util.TokenWithTime;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by linmh on 2016/8/8.
 */
public class LoginModelImpl implements LoginModel {
    public void login(String name, String password, final OnLoginListener listener) {
        List<ParamsPair> list = new ArrayList<>();
        list.add(new ParamsPair("phone", name));
        list.add(new ParamsPair("passwd", password));
        String url = OKHttpUtil.makeURL(NewsAPI.getLoginAPI(), list);
        Log.i("url", url);
        OKHttpUtil.enqueue(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                listener.onFailure();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String s = response.body().string();
              System.out.println(s);
              Gson gson = new Gson();
              Message<User> msg = gson.fromJson(s, new TypeToken<Message<User>>(){}.getType());

              if(msg==null){
                listener.onFailure();
                return;
              }
             User user = msg.getData();
             System.out.println(msg);
            if(msg.isSuccess()){
              listener.onSuccess(user.getId());
            }else{
              int code = user.getFlag();
              System.out.println(code);
              switch (code){
                case -1:listener.onUserPasswordError();break;
                case -2:  listener.onUserNameError();break;
                default:listener.onFailure();break;
              }
            }
            }
        });


    }
}
