package com.example.iface;

/**
 * Created by Administrator on 2016/8/8.
 */
public interface OnLoginListener {
void onUserNameError();
void onUserPasswordError();
void onSuccess(String id);
void onFailure();
}
