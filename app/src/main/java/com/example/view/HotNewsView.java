package com.example.view;

import com.example.entry.NewItem;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public interface HotNewsView {
  void sortNewsView(List<NewItem> list);
  void moreNewsView(List<NewItem> list);
  void showDialog();
  void dismssDialog();
  void showToast(String msg);
  void noNewsView();
  void noMostView();
}
