package com.example.model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.entry.User;
import com.example.https.HttpRequest;
import com.example.iface.IUserLoginOrRegist;
import com.example.iface.OnResultListener;
import com.example.util.TokenWithTime;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**用户登录注册的业务模型
 * Created by 林妙鸿 on 2016/6/5.
 */
@Deprecated
public class UserLoginOrRegistModel implements IUserLoginOrRegist {
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
            Log.i("123", "result:" + s);

            try {
                JSONObject object = new JSONObject(s);
                if("failed".equals(object.get("message"))){
                    JSONObject data = object.getJSONObject("data");
                    listener.onFaild(data.getInt("flag")+"");
                }else{
                    listener.onSuccess("success");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onFaild("faild");
        }
    }
    /**
     * 用户登录的异步加载类，参数数组params[0]是密码，params[1]是手机号码
     *
     */
    public class ResgistAsyncTask extends AsyncTask<String,Integer,String>{
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
            try {
                JSONObject object = new JSONObject(s);
                if("failed".equals(object.get("message"))){
                    JSONObject data = object.getJSONObject("data");
                    listener.onFaild(data.getInt("flag")+"");
                }else{
                    listener.onSuccess("success");
                }
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onFaild("faild");
        }
    }
}
