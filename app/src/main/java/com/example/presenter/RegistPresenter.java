package com.example.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.entry.User;
import com.example.iface.OnRegistListener;
import com.example.model.iface.RegistModel;
import com.example.model.impl.RegistModelImpl;
import com.example.runtime.RunTime;
import com.example.view.RegistView;


/**
 * Created by Administrator on 2016/8/8.
 */
public class RegistPresenter implements OnRegistListener {
    private RegistModel rm;
    private RegistView rv;

    public RegistPresenter(RegistView rv) {
        this.rv = rv;
        this.rm = new RegistModelImpl();
    }

    public void requestCode(String phone) {
        rm.requestCode(rv.getContext(), phone, this);

    }

    public void sendCode(String phone,String code) {
        rv.updateViewAsCodeSend();
        rm.sendCode(rv.getContext(), phone, code, this);
    }
    public void lebel(){
        rv.moveToLabel();
    }
    public void regist(User user){
        rm.regist(user,this);
    }
    @Override
    public void onUserNameExist() {
        rv.showToast("已经存在该用户");
    }

    @Override
    public void onUserCodeRequestSucess() {
        rv.updateViewAsCodeRequest();
    }

    @Override
    public void onUserCodeRequestFailed() {
        rv.showToast("无法获取验证码");
    }

    @Override
    public void onUserCodeSendSuccess() {
        rv.updateViewAsCodeSend();
    }

    @Override
    public void onUserCodeSendFailed() {
        rv.showToast("验证码错误");
    }


    @Override
    public void onSuccess(String id) {
        rv.setUser(id);
        rv.moveToIndex();
    }

    @Override
    public void onCodeSuccess() {
        rv.updateViewAsCodeSendSuccess();
    }

    @Override
    public void onFailure() {
        rv.showToast("请求失败");
    }
}
