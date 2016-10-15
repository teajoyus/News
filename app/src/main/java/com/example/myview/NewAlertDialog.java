package com.example.myview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.news.LoginActivity;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class NewAlertDialog {
    public static void alertLogin(final Context context) {
        showAlert(context, "没有权限", "对不起，您还没有登录，没有权限操作。您要登录吗", "登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }
    public static void showAlert(final Context context,String title,String content ,String positiveString,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton(positiveString, listener);
        builder.show();
    }
}
