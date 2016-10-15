package com.example.presenter;

import com.example.entry.NewItem;
import com.example.iface.OnNewListener;
import com.example.model.iface.NewsModel;
import com.example.model.impl.NewModelImpl;
import com.example.view.NewView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class NewPresenter implements OnNewListener {
    NewView nv;
    NewsModel nm;
    int alrquest;
    int count;
    static final int DEFAULT_NEW_COUNT = 10;
    static final int DEFAULT_MOST_FLASH_COUNT = 5;
    static final int DEFAULT_MOST_NEW_COUNT = 5;

    public NewPresenter(NewView nv) {
        this.nv = nv;
        nm = new NewModelImpl();
        alrquest = 0;
    }

    public void initNew(String userId) {
        count = DEFAULT_NEW_COUNT;//默认每次请求数量
        nm.requestNew(userId, alrquest, count, "", this);
    }

    public void changeNew(String userId, String tag) {
        count = DEFAULT_NEW_COUNT;//默认每次请求数量
        alrquest = 0;
        nm.requestNew(userId, alrquest, count, tag, this);
    }

    public void flashNew(String userId, String tag) {
        count = DEFAULT_MOST_FLASH_COUNT;//默认每次请求数量
        nm.requestFalshNew(userId, alrquest, count, tag, this);
      // onNoFlashNew();
    }

    public void mostNew(String userId, String tag) {
        count = DEFAULT_MOST_NEW_COUNT;//默认每次请求数量
        nm.requestMostNew(userId, alrquest, count, tag, this);
    }

    public void searchNew(String keyword) {
        count = DEFAULT_MOST_NEW_COUNT;//默认每次请求数量
        nm.requestSearchNew(keyword, this);
    }

    @Override
    public void onSuccessWithNew(List<NewItem> list) {
        alrquest = count;//已经请求的新闻数量
        nv.initNewList(list);
    }

    @Override
    public void onSuccessWithFlash(List<NewItem> list) {
        if(list==null||list.size()==0){
            onNoFlashNew();
        }else {
            alrquest += list.size();//已经请求的新闻数量
            nv.flashNewList(list);
        }
    }

    @Override
    public void onSuccessWithMost(List<NewItem> list) {
        if(list==null||list.size()==0){
            onNoMostNew();
        }else {
            alrquest +=  list.size();//已经请求的新闻数量
            nv.mostNewList(list);
        }
    }

    @Override
    public void onSuccessWithSearch(List<NewItem> list) {
        alrquest = 0;
        count = DEFAULT_NEW_COUNT;
        nv.searchNewList(list);
    }

    @Override
    public void onNoFlashNew() {
        nv.showToast("当前已经是最新的新闻了");
    }

    @Override
    public void onNoMostNew() {
        nv.showToast("没有更多新闻了");
    }

    @Override
    public void onNotFound() {
        nv.showToast("找不到包含该关键字的新闻");
    }


    @Override
    public void onFaild() {
        nv.showToast("请求新闻列表失败");
    }
}
