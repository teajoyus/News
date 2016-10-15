package com.example.model.impl;

import android.content.Context;

import com.example.api.NewsAPI;
import com.example.constant.Constant;
import com.example.entry.ParamsPair;
import com.example.entry.User;
import com.example.iface.OnRegistListener;
import com.example.model.iface.RegistModel;
import com.example.runtime.RunTime;
import com.example.util.OKHttpUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by Administrator on 2016/8/8.
 */
public class RegistModelImpl implements RegistModel {

    public void regist(User user, final OnRegistListener listener) {
        try {
//            String url = Constant.REGIST+"?passwd="+user.getPassword()+"&phone="+user.getPhone()+"&name="+ URLEncoder.encode(user.getName(), "utf-8")+"&tags="+URLEncoder.encode(user.getLabel(), "utf-8");
            List<ParamsPair> list = new ArrayList<>();
            list.add(new ParamsPair("passwd", user.getPassword()));
            list.add(new ParamsPair("phone", user.getPhone()));
            list.add(new ParamsPair("name",URLEncoder.encode(user.getName(), "utf-8")));
            list.add(new ParamsPair("tags", URLEncoder.encode(user.getLabel(), "utf-8")));
            String url = OKHttpUtil.makeURL(NewsAPI.getRegistAPI(), list);
            Log.i("url", url);

            OKHttpUtil.enqueue(url, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    listener.onFailure();
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    String s =  response.body().string();
                    Log.i("body", s);
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONObject data = object.getJSONObject("data");
                        if("failed".equals(object.get("message"))){
                            int code = data.getInt("flag");
                            if(code==-1){
                                listener.onUserNameExist();
                            }else{
                                listener.onFailure();
                            }
                        }else{
                            listener.onSuccess(data.getString("user_id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestCode(Context context, String phone, final OnRegistListener listener) {
        BmobSMS.initialize(context, "e388ec3ec9417e1c8aa7c1279beda897");
        BmobSMS.requestSMSCode(context, phone, "mynews", new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//验证码获取成功
                    listener.onUserCodeRequestSucess();
                } else {
                    listener.onUserCodeRequestFailed();
                }
            }
        });
    }

    @Override
    public void sendCode(Context context, String phone,String code, final OnRegistListener listener) {
        Log.i("123","code"+code);
        BmobSMS.verifySmsCode(context, phone, code, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//短信验证码已验证成功
                    Log.i("123","验证码成功");
                    listener.onCodeSuccess();
                } else {
                    Log.i("123","验证码失败");
                    listener.onUserCodeSendFailed();
                }
            }
        });
    }
}
