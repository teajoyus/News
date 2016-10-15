package com.example.presenter;

import com.example.entry.User;
import com.example.iface.OnLoginListener;
import com.example.model.iface.LoginModel;
import com.example.model.impl.LoginModelImpl;
import com.example.view.LoginView;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LoginPresenter implements OnLoginListener {
    LoginView lv;
    LoginModel lm;

    public LoginPresenter(LoginView lv) {
        this.lv = lv;
        this.lm = new LoginModelImpl();
    }

    /**
     *登录业务
     */
    public void login() {
        User user = lv.getUser();
        String name = user.getPhone();
        String psw = user.getPassword();
        lm.login(name, psw, this);
    }

    public void regist(){
        lv.moveToRegist();
    }
    public void nouser(){
        lv.moveToNoUser();
    }
    @Override
    public void onUserNameError() {
        lv.showTaost("用户名不存在");
    }

    @Override
    public void onUserPasswordError() {
        lv.showTaost("密码错误");
    }

    @Override
    public void onSuccess(String id) {
        lv.setUser(id);
        lv.showTaost("登录成功");
        lv.moveToIndex();
    }

    @Override
    public void onFailure() {
    lv.showTaost("请求失败");
    }

}
