package com.example.news;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.iface.IUserModel;
import com.example.iface.OnResultListener;
import com.example.model.UserModel;
import com.example.runtime.RunTime;
import com.example.util.NewsUtils;
import com.example.myview.NewAlertDialog;

public class UserFragment extends Fragment implements OnResultListener{

private RelativeLayout rl_setting,rl_user_user,rl_feedback_user,rl_love_user;
private IUserModel userModel;
    private TextView  tv_name_user,read_number_user,love_number_user,comment_number_user;
    private ImageView refresh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
//        if(TableUtils.isTablet(getActivity())){
//            view =inflater.inflate(R.layout.usercenter2, container,false);
//        }else{
//            view =inflater.inflate(R.layout.usercenter, container,false);
//
//        }
        view =inflater.inflate(R.layout.usercenter, container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
initView();
        initHanlder();
        initListener();
    }


    private void initView() {

        rl_setting = (RelativeLayout) getActivity().findViewById(R.id.rl_setting_user);
        rl_user_user = (RelativeLayout) getActivity().findViewById(R.id.rl_user_user);
        rl_feedback_user = (RelativeLayout) getActivity().findViewById(R.id.rl_feedback_user);
        rl_love_user = (RelativeLayout) getActivity().findViewById(R.id.rl_love_user);
        tv_name_user = (TextView)getActivity().findViewById(R.id.tv_name_user);
        love_number_user = (TextView)getActivity().findViewById(R.id.love_number_user);
        comment_number_user = (TextView)getActivity().findViewById(R.id.comment_number_user);
        read_number_user = (TextView)getActivity().findViewById(R.id.read_number_user);
        refresh = (ImageView) getActivity().findViewById(R.id.iv_refresh_user);

    }
    private void initHanlder() {
        userModel = new UserModel();
        userModel.setListener(this);
        if(RunTime.getRunTimeUser().getId()!=null){
            userModel.userInfo(RunTime.getRunTimeUser().getId());
        }else{

        }
    }


    private void initListener() {
        getActivity().findViewById(R.id.iv_setting_right_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
            }
        });
        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
            }
        });

        rl_user_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!RunTime.isUserLogin()){
                    NewAlertDialog.alertLogin(getActivity());
                    return;
                }
                Intent intent = new Intent(getActivity(),UserActivity.class);
                startActivityForResult(intent,200);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RunTime.getRunTimeUser().getId()!=null){
                    userModel.userInfo(RunTime.getRunTimeUser().getId());
                }else{

                }
            }
        });
        rl_feedback_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!RunTime.isUserLogin()){
                    NewAlertDialog.alertLogin(getActivity());
                    return;
                }
                Intent intent = new Intent(getActivity(),FeedBackActivity.class);
                startActivity(intent);
            }
        });
        rl_love_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!RunTime.isUserLogin()){
                    NewAlertDialog.alertLogin(getActivity());
                    return;
                }
                Intent intent = new Intent(getActivity(),LoveActivity.class);
                intent.putExtra("userid",RunTime.getRunTimeUser().getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStartDoing() {

    }

    @Override
    public void onSuccess(Object o) {

        love_number_user.setText("喜欢数 ");
        love_number_user.append(NewsUtils.textColorSpaned("#EE7621", "  "+RunTime.getRunTimeUser().getLoveAllNum()));
        comment_number_user.setText("评论数 ");
        comment_number_user.append(NewsUtils.textColorSpaned("#EE7621","  "+ RunTime.getRunTimeUser().getCommentAllNum() + ""));
        read_number_user.setText("阅读数 ");
        read_number_user.append(NewsUtils.textColorSpaned("#EE7621", "  "+RunTime.getRunTimeUser().getReadAllNum() + ""));
        tv_name_user.setText(RunTime.getRunTimeUser().getName());

    }

    @Override
    public void onFaild(Object o) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            if(RunTime.getRunTimeUser().getId()!=null){
                userModel.userInfo(RunTime.getRunTimeUser().getId());
            }else{

            }
        }

    }
}