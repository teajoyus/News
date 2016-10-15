package com.example.news;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iface.IUserModel;
import com.example.iface.OnResultListener;
import com.example.model.UserModel;
import com.example.runtime.RunTime;

/**
 * Created by Administrator on 2016/6/11 0011.
 */
public class UserActivity extends Activity implements OnResultListener {
private ImageView back;
    private TextView tv_bar_finish_user;
private EditText et_phone_user,et_name_user,et_email_user;
    private IUserModel userModel;
private ProgressBar pb_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        initView();
        initHandler();
        initListener();
    }


    private void initView() {
        back = (ImageView)findViewById(R.id.iv_back_user);
        tv_bar_finish_user= (TextView) findViewById(R.id.tv_bar_finish_user);
        et_phone_user = (EditText) findViewById(R.id.et_phone_user);
        et_name_user = (EditText) findViewById(R.id.et_name_user);
        et_email_user = (EditText) findViewById(R.id.et_email_user);
        pb_user = (ProgressBar) findViewById(R.id.pb_user);
    }
    private void initListener() {
        tv_bar_finish_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone_user.getText().toString().trim();
                String name = et_name_user.getText().toString().trim();
                String email = et_email_user.getText().toString().trim();
                userModel.userInfoChange(RunTime.getRunTimeUser().getId(),name,email);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.this.finish();
            }
        });

    }
    private void initHandler() {
        userModel  = new UserModel();
        userModel.setListener(this);
        et_phone_user.setText(RunTime.getRunTimeUser().getPhone());
        et_name_user.setText(RunTime.getRunTimeUser().getName());
        String email =  RunTime.getRunTimeUser().getEmail();
        if(email!=null&&email.length()>1){

            et_email_user.setText(email);
        }else{
            et_email_user.setHint("您还没有设置邮箱");
        }
    }


    @Override
    public void onStartDoing() {
        pb_user.setVisibility(View.VISIBLE);



    }

    @Override
    public void onSuccess(Object o) {
        pb_user.setVisibility(View.GONE);
        Toast.makeText(UserActivity.this,"信息修改成功",Toast.LENGTH_SHORT).show();
        UserActivity.this.finish();
    }

    @Override
    public void onFaild(Object o) {
        pb_user.setVisibility(View.GONE);
    }
}

