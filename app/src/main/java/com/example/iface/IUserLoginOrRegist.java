package com.example.iface;

import com.example.entry.User;

/**用户注册、登录接口
 * Created by 林妙鸿 on 2016/6/5.
 */
public interface IUserLoginOrRegist {
    public void setListener(OnResultListener listener);
    public void login(User user);
    public void regist(User user);
}
