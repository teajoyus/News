package com.example.news;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.LoveAdapter;
import com.example.myview.NewAlertDialog;
import com.example.runtime.RunTime;
import com.example.util.TableUtils;


public class IndexActivity extends FragmentActivity implements View.OnClickListener {


    //4个fragment
    private MainFragment fragmentFirst;
    private HotFragment fragmentSecond;
    private LoveFragment fragmentThird;
    private UserFragment fragmentFourth;
    //用于对fragment进行管理
    private FragmentManager fragmentManager;

    //4个消息布局
    private View firstLayout,secondLayout,thridLayout,fourthLayout;
    //tab布局上图片控件
    private ImageView firstImage,secondImage,thridImage,fourthImage;
    //tab布局上文字控件
    private TextView firstText,secondText,thirdText,fourthText;

    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(TableUtils.isTablet(IndexActivity.this)){
//            setContentView(R.layout.activity_main2);
//        }else{
//            setContentView(R.layout.activity_main);
//
//        }
        setContentView(R.layout.activity_main);
        initViews();
        fragmentManager = getFragmentManager();
        //第一次启动时选中第0个tab
        setTabSelection(0);

    }



    private void initViews() {
        firstLayout = findViewById(R.id.search_layout);
        secondLayout = findViewById(R.id.monitor_layout);
        thridLayout = findViewById(R.id.myself_layout);
        fourthLayout = findViewById(R.id.more_layout);
        firstImage = (ImageView) findViewById(R.id.search_image);
        secondImage = (ImageView) findViewById(R.id.monitor_image);
        thridImage = (ImageView) findViewById(R.id.myself_image);
        fourthImage = (ImageView) findViewById(R.id.more_image);
        firstText = (TextView) findViewById(R.id.search_text);
        secondText = (TextView) findViewById(R.id.monitor_text);
        thirdText = (TextView) findViewById(R.id.myself_text);
        fourthText = (TextView) findViewById(R.id.more_text);
        firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);
        thridLayout.setOnClickListener(this);
        fourthLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_layout:
                setTabSelection(0);
                break;
            case R.id.monitor_layout:
                setTabSelection(1);
                break;
            case R.id.myself_layout:
                if(!RunTime.isUserLogin()){
                    NewAlertDialog.alertLogin(this);
                }
                setTabSelection(2);
                break;
            case R.id.more_layout:
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示查询，1表示监控，2表示个人，3表示更多。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                firstImage.setImageResource(R.drawable.home_selected);
                firstText.setTextColor(0xFF4087BF);
                if (fragmentFirst == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    fragmentFirst = new MainFragment();
                    transaction.add(R.id.ly_content, fragmentFirst);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fragmentFirst);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                secondImage.setImageResource(R.drawable.hot_selected);
                secondText.setTextColor(0xFF4087BF);
                if (fragmentSecond == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    fragmentSecond = new HotFragment();
                    transaction.add(R.id.ly_content, fragmentSecond);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(fragmentSecond);
                }
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                thridImage.setImageResource(R.drawable.love_selected);
                thirdText.setTextColor(0xDFFF0000);
                if (fragmentThird == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    fragmentThird = new LoveFragment();
                    transaction.add(R.id.ly_content, fragmentThird);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(fragmentThird);
                }
                break;
            case 3:
            default:
                // 当点击了设置tab时，改变控件的图片和文字颜色
                fourthImage.setImageResource(R.drawable.user_selected);
                fourthText.setTextColor(0xFF4087BF);
                if (fragmentFourth == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    fragmentFourth = new UserFragment();
                    transaction.add(R.id.ly_content, fragmentFourth);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(fragmentFourth);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {

        firstImage.setImageResource(R.drawable.home_unselected);
        firstText.setTextColor(0xc0000000);
        secondImage.setImageResource(R.drawable.hot_unselected);
        secondText.setTextColor(0xc0000000);
        thridImage.setImageResource(R.drawable.love_unselected);
        thirdText.setTextColor(0xc0000000);
        fourthImage.setImageResource(R.drawable.user_unselected);
        fourthText.setTextColor(0xc0000000);

    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (fragmentFirst != null) {
            transaction.hide(fragmentFirst);
        }
        if (fragmentSecond != null) {
            transaction.hide(fragmentSecond);
        }
        if (fragmentThird != null) {
            transaction.hide(fragmentThird);
        }
        if (fragmentFourth != null) {
            transaction.hide(fragmentFourth);
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}