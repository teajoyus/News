package com.example.presenter;

import com.example.entry.Enum;
import com.example.entry.NewItem;
import com.example.iface.OnNewListener;
import com.example.model.iface.HotNewsModel;
import com.example.model.impl.HotNewsModelImpl;
import com.example.view.HotNewsView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class HotNewsPresenter implements OnNewListener {
  private HotNewsModel hm;
  private HotNewsView hv;
  private List<NewItem> list;
  private Enum.SortType type;
  private final static int DEFAULT_NEWS_COUNT = 10;
  private final static int MIN_NEWS_COUNT = 5;
  private int count, alrRequest;//请求数量，已经请求数量

  public HotNewsPresenter(HotNewsView hv) {
    this.hv = hv;
    hm = new HotNewsModelImpl();
    type = Enum.SortType.READ; //默认类型是按照阅读量的排序方式

  }

  public void setType(Enum.SortType type) {
    this.type = type;
  }

  public List<NewItem> getList() {
    return list;
  }

  public Enum.SortType getType() {
    return type;
  }

  public void requestHotNews() {
    alrRequest = 0;//请求新的新闻排序，要清空已经请求数量
    count = DEFAULT_NEWS_COUNT;//默认请求
    hv.showDialog();//显示加载框
    switch (type) {
      case LOVE:
        hm.SortNewsByLove(count, alrRequest, this);
        break;
      case READ:
        hm.SortNewsByRead(count, alrRequest, this);
        break;
      case COMMENT:
        hm.SortNewsByComment(count, alrRequest, this);
        break;
    }


  }
public void requestMostNews(){
  hv.showDialog();//显示加载框
  count = MIN_NEWS_COUNT;//加载更多新闻的数目
  hm.mostNews(count,alrRequest,type,this);

}
  @Override
  public void onSuccessWithNew(List<NewItem> list) {
    System.out.println(list);
    this.list=list;
    hv.sortNewsView(list);
    alrRequest = list.size();//成功后刷新已请求数目
  }

  @Override
  public void onSuccessWithFlash(List<NewItem> list) {
    hv.dismssDialog();
  }

  @Override
  public void onSuccessWithMost(List<NewItem> list) {
    hv.dismssDialog();
    System.out.println(list);
    if(list==null||list.size()==0){
      hv.showToast("没有更多新闻了");
      hv.noMostView();
      return;
    }
    for(NewItem item:list){
      this.list.add(item);
    }
    hv.moreNewsView(list);
    alrRequest += list.size();//成功后刷新已请求数目
  }

  @Override
  public void onSuccessWithSearch(List<NewItem> list) {
    hv.dismssDialog();
  }

  @Override
  public void onNoFlashNew() {
    hv.dismssDialog();
  }

  @Override
  public void onNoMostNew() {
    hv.dismssDialog();
  }

  @Override
  public void onNotFound() {
    hv.dismssDialog();
  }

  @Override
  public void onFaild() {
    hv.dismssDialog();
    hv.showToast("请求热点新闻失败");
  }
}
