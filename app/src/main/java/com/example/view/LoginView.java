package com.example.view;

import com.example.entry.User;

/**
 * Created by Administrator on 2016/8/8.
 */
public interface LoginView {
    //登录后跳转到首页
    void moveToIndex();
    //进入到注册页面
    void moveToRegist();
    //进入到随便看看页面
    void moveToNoUser();
    //登录时提示
    void showTaost(String msg);
    //获取用户登录信息
    User getUser();
    //设置用户id
    void setUser(String id);

}
