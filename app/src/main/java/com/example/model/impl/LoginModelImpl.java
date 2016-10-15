package com.example.model.impl;

import com.example.api.NewsAPI;
import com.example.constant.Constant;
import com.example.entry.ParamsPair;
import com.example.iface.OnLoginListener;
import com.example.model.iface.LoginModel;
import com.example.util.OKHttpUtil;
import com.example.util.TokenWithTime;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/8/8.
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
                Log.i("body", s);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject data = object.getJSONObject("data");
                    if ("failed".equals(object.get("message"))) {
                        int code = data.getInt("flag");
                        if (code == -1) {
                            listener.onUserPasswordError();
                        } else if (code == -2) {
                            listener.onUserNameError();
                        } else {
                            listener.onFailure();
                        }
                    } else {
                        listener.onSuccess(data.getString("user_id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
