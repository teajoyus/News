package com.example.model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.entry.Label;
import com.example.entry.User;
import com.example.https.HttpRequest;
import com.example.iface.IUserModel;
import com.example.iface.OnResultListener;
import com.example.runtime.RunTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**用户登录注册的业务模型
 * Created by 林妙鸿 on 2016/6/5.
 */
@Deprecated
public class UserModel implements IUserModel {
    private OnResultListener listener;
    public void setListener(OnResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void login(User user) {
     if(listener==null)return;
    LoginAsyncTask task = new LoginAsyncTask();
        task.execute(user.getPassword(),user.getPhone());
    }

    @Override
    public void regist(User user) {
        if(listener==null)return;
        RegsistAsyncTask task = new RegsistAsyncTask();

        try {
            task.execute(user.getPassword(),user.getPhone(),URLEncoder.encode(user.getName(),"utf-8"), URLEncoder.encode(user.getLabelString(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户信息的获取
     * @param userId
     */
    public void userInfo(String userId){
        if(listener==null)return;
        UserInfoAsyncTask task = new UserInfoAsyncTask();
        task.execute(userId);

    }

    /**
     * 用户信息的修改
     * @param userId
     */
    public void userInfoChange(String userId,String name,String email){
        if(listener==null)return;
       UserInfoChangeAsyncTask task = new UserInfoChangeAsyncTask();
        try {
            task.execute(userId, URLEncoder.encode(name,"utf-8"),email);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    public void alllabel(){
        AllLabelAsyncTask task = new AllLabelAsyncTask();
        task.execute();
    }


    /**
     * 用户登录的异步加载类，参数数组params[0]是密码，params[1]是手机号码
     *
     */
    public class LoginAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return HttpRequest.getInstance().login(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "null";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            onExecute(s);
        }
    }
    /**
     * 用户注册的异步加载类，参数数组params[0]是密码，params[1]是手机号码,params[2]是用户昵称 params[3是逗号隔开的标签
     *
     */
    public class RegsistAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }
        @Override
        protected String doInBackground(String... params) {
                return HttpRequest.getInstance().regist(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            onExecute(s);

        }
    }
    private  void onExecute(String s){
        try {
            JSONObject object = new JSONObject(s);
            JSONObject data = object.getJSONObject("data");
            if("failed".equals(object.get("message"))){

                listener.onFaild(data.getInt("flag")+"");
            }else{

                listener.onSuccess(data.getString("user_id"));
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        listener.onFaild("faild");
    }
    /**
     * 用户信息获取的异步加载类，参数数组params[0]是userId
     *
     */
    public class UserInfoAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }
        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().userInfo(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                JSONObject data = object.getJSONObject("data");
                if("failed".equals(object.get("message"))){

                    listener.onFaild(data.getInt("flag")+"");
                }else{
                    RunTime.getRunTimeUser().setLoveAllNum(data.getInt("love_times"));
                    RunTime.getRunTimeUser().setReadAllNum(data.getInt("read_times"));
                    RunTime.getRunTimeUser().setCommentAllNum(data.getInt("message_times"));
                    RunTime.getRunTimeUser().setEmail(data.getString("email"));
                    RunTime.getRunTimeUser().setAvstarUrl(data.getString("image"));
                    RunTime.getRunTimeUser().setName(data.getString("user_name"));
                    RunTime.getRunTimeUser().setPhone(data.getString("phone"));
                    listener.onSuccess(s);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            listener.onFaild("faild");

        }
    }
    /**
     * 用户信息获取的异步加载类，参数数组params[0]是userId
     *
     */
    public class UserInfoChangeAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }
        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().userInfoChange(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                JSONObject data = object.getJSONObject("data");
                if("failed".equals(object.get("message"))){

                    listener.onFaild(data.getInt("flag")+"");
                }else{
                    RunTime.getRunTimeUser().setLoveAllNum(data.getInt("love_times"));
                    RunTime.getRunTimeUser().setReadAllNum(data.getInt("read_times"));
                    RunTime.getRunTimeUser().setCommentAllNum(data.getInt("message_times"));
                    RunTime.getRunTimeUser().setEmail(data.getString("email"));
                    RunTime.getRunTimeUser().setAvstarUrl(data.getString("image"));
                    RunTime.getRunTimeUser().setName(data.getString("user_name"));
                    RunTime.getRunTimeUser().setPhone(data.getString("phone"));
                    listener.onSuccess(s);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            listener.onFaild("faild");

        }
    }
    /**
     * 用户信息获取的异步加载类，参数数组params[0]是userId
     *
     */
    public class AllLabelAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }
        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().allLabel();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);

                if("failed".equals(object.get("message"))){
                    JSONObject data = object.getJSONObject("data");
                    listener.onFaild(data.getInt("flag")+"");
                }else{
                    JSONArray array = object.getJSONArray("data");
                    List<Label> list = new ArrayList<Label>();
                    Label label = null;
                    for(int i=0;i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        label = new Label();
                        label.setKey(o.getString("key"));
                        label.setName(o.getString("name"));
                        list.add(label);
                    }
                    listener.onSuccess(list);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            listener.onFaild("faild");

        }

    }
}
