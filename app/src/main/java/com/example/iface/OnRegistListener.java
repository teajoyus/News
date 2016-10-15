package com.example.iface;

/**
 * Created by Administrator on 2016/8/8.
 */
public interface OnRegistListener {
void onUserNameExist();
void onUserCodeRequestSucess();
void onUserCodeRequestFailed();
void onUserCodeSendSuccess();
void onUserCodeSendFailed();
void onSuccess(String id);
void onCodeSuccess();
void onFailure();
}
