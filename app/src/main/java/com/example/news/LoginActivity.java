package com.example.news;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.constant.Constant;
import com.example.entry.User;
import com.example.iface.IUserModel;
import com.example.iface.OnResultListener;
import com.example.model.UserModel;
import com.example.myview.dialog.SpotsDialog;
import com.example.presenter.LoginPresenter;
import com.example.runtime.RunTime;
import com.example.util.NewsSharedPreferences;
import com.example.util.TableUtils;
import com.example.view.LoginView;
import com.tencent.bugly.beta.Beta;

/**
 * Created by Administrator on 2016/5/25.
 */
public class LoginActivity extends Activity implements LoginView{


    private Button login;
    private TextView regist;
    private EditText et_phone, et_pwd;
    private ProgressBar pb_login;
    private AlertDialog dialog;
    private TextView casual;
    private LoginPresenter presenter;
    private CheckBox cb_remain_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        presenter = new LoginPresenter(this);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        NewsSharedPreferences share = new NewsSharedPreferences(LoginActivity.this, "login");
        String s = share.load("phone");
        String s2 = share.load("pwd");
        if (s != null && !"".equals(s)) {
            et_phone.setText(s);
        }
        if (s2 != null && !"".equals(s2)) {
            et_pwd.setText(s2);
        }
    }

    private void initView() {
        login = (Button) findViewById(R.id.bt_login);
        regist = (TextView) findViewById(R.id.regist_tv_login);
        et_phone = (EditText) findViewById(R.id.et_phone_login);
        et_pwd = (EditText) findViewById(R.id.et_pwd_login);
        pb_login = (ProgressBar) findViewById(R.id.pb_login);
        casual = (TextView) findViewById(R.id.tv_casual_login);
        cb_remain_login = (CheckBox) findViewById(R.id.cb_remain_login);
        dialog = new SpotsDialog(LoginActivity.this,"登录中...");

    }

    private void initListener() {
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.regist();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
               presenter.login();
            }
        });
        casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.nouser();
            }
        });
    }
  //进入主页
    @Override
    public void moveToIndex() {
        dialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void moveToRegist() {
        Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
        startActivity(intent);
    }

    @Override
    public void moveToNoUser() {
        Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
        startActivity(intent);
        this.finish();

    }

    @Override
    public void showTaost(final String msg) {
        dialog.dismiss();
        System.out.println(msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public User getUser() {
        String phone = et_phone.getText().toString().trim();
        String pwd = et_pwd.getText().toString().trim();
        User user = new User();
        user.setPhone(phone);
        user.setPassword(pwd);
        return user;
    }

    //回调设置用户信息到运行时环境和缓存
    @Override
    public void setUser(String id) {
        //设置用户id
        RunTime.getRunTimeUser().setId(id);
        //取得登录时候的用户放进运行时
        User user = getUser();
        RunTime.getRunTimeUser().setPhone(user.getPhone());
        RunTime.getRunTimeUser().setPassword(user.getPassword());
        if(cb_remain_login.isChecked()) {
            //取得登录时候的用户放进缓存方便下次记住用户帐号密码
            NewsSharedPreferences share = new NewsSharedPreferences(LoginActivity.this, "login");
            share.save("phone", user.getPhone());
            share.save("pwd", user.getPassword());
        }
    }


}
