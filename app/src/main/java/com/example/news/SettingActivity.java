package com.example.news;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.api.NewsAPI;
import com.example.https.HttpService;
import com.example.runtime.RunTime;
import com.example.util.NewsSharedPreferences;
import com.example.myview.NewAlertDialog;
import com.example.myview.UISwitchButton;
import com.example.util.NewsUtils;
import com.example.util.OKHttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import cn.bmob.push.BmobPush;

/**
 * Created by Administrator on 2016/6/11 0011.
 */
public class SettingActivity extends Activity {
    private UISwitchButton sb_share, sb_night, sb_word, sb_push;
    private Button logout, btn_testip_setting;
    private RelativeLayout rl_clean_setting,rl_api_setting;
    private TextView tv_clean_setting,tv_version_setting;
    private NewsSharedPreferences preferences;
    private RelativeLayout rl_version_setting;//版本更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initView();
        initHandler();
        initListener();
    }


    private void initView() {
        sb_share = (UISwitchButton) findViewById(R.id.sb_unlook_setting);
        sb_night = (UISwitchButton) findViewById(R.id.sb_night_setting);
        sb_word = (UISwitchButton) findViewById(R.id.sb_word_setting);
        sb_push = (UISwitchButton) findViewById(R.id.sb_push_setting);
        logout = (Button) findViewById(R.id.bt_logout_setting);
        rl_clean_setting = (RelativeLayout) findViewById(R.id.rl_clean_setting);
        tv_clean_setting = (TextView) findViewById(R.id.tv_clean_setting);
        tv_version_setting = (TextView) findViewById(R.id.tv_version_setting);
        rl_version_setting = (RelativeLayout) findViewById(R.id.rl_version_setting);
        rl_api_setting = (RelativeLayout) findViewById(R.id.rl_api_setting);


    }

    private void initListener() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logout.getText().toString().equals("登录")){
                    Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SettingActivity.this.finish();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("注销");
                    builder.setMessage("确定要注销吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RunTime.loggout();
                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            SettingActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }

            }
        });
        rl_clean_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageLoader imageLoader  = ImageLoader.getInstance();
                NewAlertDialog.showAlert(SettingActivity.this, "清除", "您确定清除缓存吗", "清除",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingActivity.this, "缓存已经清除", Toast.LENGTH_SHORT).show();
                                // imageLoader.getDiskCache().getDirectory().delete();
                                String path = imageLoader.getDiskCache().getDirectory().getAbsolutePath();
                                File file =   new File(path);
                                if(file.exists()){
                                    file.delete();
                                }

                                imageLoader.clearDiskCache();
                                imageLoader.clearMemoryCache();
                                tv_clean_setting.setText("已清除");
                            }
                        });
            }
        });
        /**
         * 设置版本更新的事件
         */
        rl_version_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * true代表手动更新
                 */
                Beta.checkUpgrade(true, false);
            }
        });
        rl_api_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  AlertDialog.Builder(SettingActivity.this)
                        .setTitle("请选择接口")
                        .setIcon(R.drawable.new_logo2)
                        .setSingleChoiceItems(new  String[] {"内网接口", "外网接口1（yu）", "外网接口2（chi）" , }, NewsAPI.getAPIType(),
                                new  DialogInterface.OnClickListener() {
                                    public   void  onClick(DialogInterface dialog,  int  which) {
                                        System.out.print(which+"qq");
                                        NewsAPI.changeIPandPORT(which);
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消" ,  null )
                        .show();
            }
        });
        sb_push.setOnCheckedChangeListener(new SettingListener());
        sb_night.setOnCheckedChangeListener(new SettingListener());
        sb_word.setOnCheckedChangeListener(new SettingListener());
        sb_share.setOnCheckedChangeListener(new SettingListener());

    }

    private class SettingListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                switch (buttonView.getId()) {
                    case R.id.sb_unlook_setting:
                        preferences.save("share", "can");
                        break;
                    case R.id.sb_night_setting:
                        preferences.save("night", "can");
                        break;
                    case R.id.sb_word_setting:
                        preferences.save("word", "can");
                        break;
                    case R.id.sb_push_setting:
                        preferences.save("push", "can");

                        // 启动推送服务
                        BmobPush.startWork(getApplicationContext());
                        break;
                }
            } else {
                switch (buttonView.getId()) {
                    case R.id.sb_unlook_setting:
                        preferences.remove("share");
                        break;
                    case R.id.sb_night_setting:
                        preferences.remove("night");
                        break;
                    case R.id.sb_word_setting:
                        preferences.remove("word");
                        break;
                    case R.id.sb_push_setting:
                        preferences.remove("push");
                        // 启动推送服务
                        BmobPush.stopWork();
                        break;
                }
            }
        }
    }

    private void initHandler() {
        //初始化选择框的状态
        initUIButton();
        //初始化版本更新
        checkUpgrade();
        //初始化图片缓存
        checkDiskCache();
        //初始化注销按钮状态
        initLogoutButton();
        //获得版本号
        versionQuery();

    }
    private  void initUIButton(){
        preferences = new NewsSharedPreferences(SettingActivity.this, "setting");
        if ("can".equals(preferences.load("share"))) {
            sb_share.setChecked(true);
        } else {
            sb_share.setChecked(false);
        }
        if ("can".equals(preferences.load("night"))) {
            sb_night.setChecked(true);
        } else {
            sb_night.setChecked(false);
        }
        if ("can".equals(preferences.load("word"))) {
            sb_word.setChecked(true);
        } else {
            sb_word.setChecked(false);
        }
        if ("can".equals(preferences.load("push"))) {
            sb_push.setChecked(true);
        } else {
            sb_push.setChecked(false);
        }
    }
private void checkUpgrade(){
    UpgradeInfo info = Beta.getUpgradeInfo();
    if(info!=null) {
        tv_version_setting.setText("有更新");
        tv_version_setting.setTextColor(0xafff0000);
    }else {
        tv_version_setting.setText("最新版本");
    }
}
    private  void checkDiskCache(){
        ImageLoader imageLoader  = ImageLoader.getInstance();
//        long length = imageLoader.getDiskCache().getDirectory().length();
        String path = imageLoader.getDiskCache().getDirectory().getAbsolutePath();
        System.out.println(path);
        double length = NewsUtils.getSize(new File(path));
        if(length==0){
            tv_clean_setting.setText("已清除");
        }else{
            tv_clean_setting.setText(String.format("%.2f", length)+"MB");
        }
    }
    private void initLogoutButton(){
      Log.i("linmh>>>","uesr:"+RunTime.getRunTimeUser());
      Log.i("linmh>>>","id:"+RunTime.getRunTimeUser().getId());
      Log.i("linmh>>>","isLogin:"+RunTime.isUserLogin());
        if(RunTime.isUserLogin()){
            logout.setText("注销");
            logout.setBackgroundColor(0x80ff0000);
        }else{
            logout.setText("登录");
            logout.setBackgroundResource(R.color.themecolor);
        }
    }
   private  void versionQuery(){
       OKHttpUtil.enqueue("https://api.bugly.qq.com/beta/apiv1/exp_list?app_key=Hk3ij0WLtEY9JDn8&app_id=900034562&pid=1&start=0&limit=1", new Callback() {
           @Override
           public void onFailure(Request request, IOException e) {

           }

           @Override
           public void onResponse(Response response) throws IOException {
                String s = response.body().string();
               System.out.println(s);
               JSONObject o = null;
               try {
                   o = new JSONObject(s);
                   if("Success".equals(o.getString("msg"))){
                        final JSONObject o2 = o.getJSONObject("data").getJSONArray("list").getJSONObject(0);
                       final String v = o2.getString("version");
                       SettingActivity.this.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                                   tv_version_setting.append(v);
                           }
                       });

                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });
   }
}

