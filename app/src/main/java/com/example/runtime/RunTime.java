package com.example.runtime;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.entry.User;
import com.example.news.LoginActivity;

/**
 * 用户储存运行时环境的封装类
 * Created by 林妙鸿 on 2016/6/6.
 */
public class RunTime {
    private static User user;

    public static User getRunTimeUser() {
        if (user == null) user = new User();
        return user;
    }

    public static boolean isUserLogin() {
        return getRunTimeUser().getId() != null;
    }

    public static void loggout() {
        user = null;
    }

    public static boolean isRecommend() {
        return "".equals(user.getCurrentlabel());
    }

}
