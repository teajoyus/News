package com.example.model.iface;

import com.example.iface.OnNewListener;

/**
 * Created by Administrator on 2016/8/9.
 */
public interface NewsModel {
    void requestNew(String userId,int alrReq,int count,String tag,OnNewListener listener);
    void requestSearchNew(String keyword,OnNewListener listener);
    void requestMostNew(String userId,int alrReq,int count,String tag,OnNewListener listener);
    void requestFalshNew(String userId,int alrReq,int count,String tag,OnNewListener listener);
}
