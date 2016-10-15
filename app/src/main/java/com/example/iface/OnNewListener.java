package com.example.iface;

import com.example.entry.NewItem;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public interface OnNewListener {
    /**
     * 首次加载新闻
     * @param list
     */
    void onSuccessWithNew(List<NewItem>list);
    /**
     *     下拉刷新新闻
     */

    void onSuccessWithFlash(List<NewItem>list);
    /**
     * 上啦加载更多
     * @param list
     */
    void onSuccessWithMost(List<NewItem>list);
    void onSuccessWithSearch(List<NewItem>list);
    /**
     * 请求不到新闻
     */
    void onNoFlashNew();
    /**
     * 请求不到新闻
     */
    void onNoMostNew();

    /**
     * 搜索不到新闻
     */
    void onNotFound();
    /**
     * 请求失败
     */
    void onFaild();
}
