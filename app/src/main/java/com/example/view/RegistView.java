package com.example.view;

import android.content.Context;

/**
 * Created by Administrator on 2016/8/8.
 */
public interface RegistView {
    //获取验证码后更新视图
    void updateViewAsCodeRequest();
    //发送验证码后更新视图
    void updateViewAsCodeSend();
    //校对验证码后更新视图
    void updateViewAsCodeSendSuccess();
    //跳转到主页
    void moveToIndex();
    //跳到标签页
    void moveToLabel();

    //注册时弹出 Toast
    void showToast(String msg);
    Context getContext();
    //设置用户
    void setUser(String id);

}
