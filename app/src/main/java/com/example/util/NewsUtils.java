package com.example.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.news.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/4.
 */
public class NewsUtils {
    /**
     * 修改textview的字体颜色
     * @param color
     * @param source
     * @return
     */
    public static Spanned textColorSpaned(String color,String source){
        String test =  "<font color= '"+color+"'>"+source+"</font>";
        return Html.fromHtml(test);
    }
    /**
     *
     * 动态获取评论列表的高度
     * @param listView
     * @return
     */
    public static int getListViewHeight(Context context,ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + DensityUtil.dipTopx(context, 50);
    }
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    "com.example.news", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.example.news", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return verName;
    }

    /**
     * 获取APK应用名称
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String verName = context.getResources()
                .getText(R.string.app_name).toString();
        return verName;
    }
    /**
     * 关键字高亮显示
     *
     * @param target  需要高亮的关键字
     * @param text	     需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target,int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(color);// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }


    /**
     * 计算文件或者文件夹的大小 ，单位 MB
     * @param file 要计算的文件或者文件夹 ， 类型：java.io.File
     * @return 大小，单位：MB
     */
    public static double getSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                //获取文件大小
                File[] fl = file.listFiles();
                double ss = 0;
                for (File f : fl)
                    ss += getSize(f);
                return ss;
            } else {
                double ss = (double) file.length() / 1024 / 1024;
                System.out.println(file.getName() + " : " + ss + "MB");
                return ss;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }
}
