package com.example.iface;

import java.util.Objects;

/** 对结果进行监听的接口
 * Created by 林妙鸿 on 2016/6/5.
 */
public interface OnResultListener {
    public void onStartDoing();
    public void onSuccess(Object o);
    public void onFaild(Object o);
}
