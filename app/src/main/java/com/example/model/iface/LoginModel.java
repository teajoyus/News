package com.example.model.iface;

import com.example.iface.OnLoginListener;

/**
 * Created by Administrator on 2016/8/8.
 */
public interface LoginModel {
    public void login(String name,String password, final OnLoginListener listener);
}
