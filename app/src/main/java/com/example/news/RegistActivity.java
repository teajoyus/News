package com.example.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entry.Label;
import com.example.entry.User;
import com.example.iface.IUserModel;
import com.example.iface.OnResultListener;
import com.example.model.UserModel;
import com.example.myview.dialog.SpotsDialog;
import com.example.presenter.RegistPresenter;
import com.example.runtime.RunTime;
import com.example.util.NewsSharedPreferences;
import com.example.view.RegistView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.sms.*;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by Administrator on 2016/5/25.
 */
public class RegistActivity extends Activity implements RegistView {
    private Button regist, bt_code_regist;
    private EditText et_phone_regist, et_pwd_regist, et_pwd2_regist;
    private RegistPresenter presenter;
    private String phone;
    private TextView login;
private android.app.AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        presenter = new RegistPresenter(this);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        NewsSharedPreferences s = new NewsSharedPreferences(RegistActivity.this, "label");
        s.removeAll();
    }

    private void initView() {
        regist = (Button) findViewById(R.id.bt_regist);
        bt_code_regist = (Button) findViewById(R.id.bt_code_regist);
        et_phone_regist = (EditText) findViewById(R.id.et_phone_regist);
        et_pwd_regist = (EditText) findViewById(R.id.et_pwd_regist);
        et_pwd2_regist = (EditText) findViewById(R.id.et_pwd2_regist);
        login = (TextView) findViewById(R.id.login_tv_regist);

        dialog = new SpotsDialog(RegistActivity.this,"正在注册...");

    }

    private void initListener() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistActivity.this.finish();
            }
        });
        bt_code_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_code_regist.getText().equals("获取验证码")) {
                    phone = et_phone_regist.getText().toString().trim();
                    presenter.requestCode();

                } else {
                    String smsId = et_phone_regist.getText().toString().trim();
                    presenter.sendCode( smsId);
                }

            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_pwd_regist.getText().toString().trim();
                String pwd2 = et_pwd2_regist.getText().toString().trim();
                if (et_phone_regist.isEnabled()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    builder.setTitle("验证手机");
                    builder.setMessage("您还没有进行手机验证");
                    builder.show();
                    return;
                }
                if (!pwd.equals(pwd2)) {
                    Toast.makeText(RegistActivity.this, "您两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    et_pwd2_regist.setText("");
                    et_pwd_regist.setText("");
                    return;
                }
                RunTime.getRunTimeUser().setPhone(phone);
                RunTime.getRunTimeUser().setPassword(pwd);
                RunTime.getRunTimeUser().setName("用户" + phone.substring(0, 3) + "****" + phone.substring(8, 11));
                presenter.lebel();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //100是在主界面添加标签后回跳
        if (requestCode == 200) {
           dialog.show();
            NewsSharedPreferences s = new NewsSharedPreferences(RegistActivity.this, "label");
            List<Label> list = s.getAll();
            RunTime.getRunTimeUser().setLabel(list);
            presenter.regist();
        }
    }
    @Override
    public void updateViewAsCodeRequest() {
        et_phone_regist.setText("");
        bt_code_regist.setText("发送验证码");
        et_phone_regist.setHint("请输入验证码...");
    }

    @Override
    public void updateViewAsCodeSend() {
        bt_code_regist.setText("获取验证码");
        et_phone_regist.setHint("请输入手机号码...");
    }

    @Override
    public void updateViewAsCodeSendSuccess() {
        Log.i("bmob", "验证通过");
        et_phone_regist.setEnabled(false);
        et_phone_regist.setText("已经成功通过验证");
        bt_code_regist.setEnabled(false);
        bt_code_regist.setTextColor(0x20000000);
    }

    @Override
    public void moveToIndex() {
       dialog.dismiss();
        Intent intent = new Intent(RegistActivity.this, IndexActivity.class);
        startActivity(intent);
        RegistActivity.this.finish();
    }

    @Override
    public void moveToLabel() {
        Intent intent = new Intent(RegistActivity.this, LabelActivity.class);
        startActivityForResult(intent, 200);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(RegistActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return RegistActivity.this;
    }

    @Override
    public void setUser(String id) {
        RunTime.getRunTimeUser().setId(id);
        NewsSharedPreferences share = new NewsSharedPreferences(RegistActivity.this, "login");
        share.save("phone", RunTime.getRunTimeUser().getPhone());
        share.save("pwd", RunTime.getRunTimeUser().getPassword());
    }

  @Override
  public User getUser() {
    return RunTime.getRunTimeUser();
  }
}
