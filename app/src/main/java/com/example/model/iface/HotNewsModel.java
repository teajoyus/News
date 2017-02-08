package com.example.model.iface;

import com.example.entry.Enum;
import com.example.iface.OnNewListener;
import com.example.presenter.HotNewsPresenter;

/**
 * Created by Administrator on 2016/10/25.
 */
public interface HotNewsModel {
  void SortNewsByLove(int count,int alrRequest,OnNewListener listener);
  void SortNewsByRead(int count,int alrRequest, OnNewListener listener);
  void SortNewsByComment(int count, int alrRequest, OnNewListener listener);
  void mostNews(int count, int alrRequest,Enum.SortType type, OnNewListener listener);
}
