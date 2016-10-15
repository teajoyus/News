package com.example.view;

import com.example.entry.NewItem;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public interface NewView {
    void initNewList(List<NewItem> list);
    void flashNewList(List<NewItem> list);
    void mostNewList(List<NewItem> list);
    void searchNewList(List<NewItem> list);
    void showToast(String msg);

}
