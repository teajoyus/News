package com.example.model.impl;

import com.example.api.NewsAPI;
import com.example.entry.Enum;
import com.example.entry.Message;
import com.example.entry.NewItem;
import com.example.entry.ParamsPair;
import com.example.iface.OnNewListener;
import com.example.model.iface.HotNewsModel;
import com.example.util.OKHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class HotNewsModelImpl implements HotNewsModel {

  @Override
  public void SortNewsByLove(int count, int alrRequest, OnNewListener listener) {
    requestNews(count, alrRequest, listener, Enum.SortType.LOVE,false);
  }

  @Override
  public void SortNewsByRead(int count, int alrRequest, OnNewListener listener) {
    requestNews(count, alrRequest, listener, Enum.SortType.READ,false);
  }

  @Override
  public void SortNewsByComment(int count, int alrRequest, OnNewListener listener) {
    requestNews(count, alrRequest, listener, Enum.SortType.COMMENT, false);
  }

  @Override
  public void mostNews(int count, int alrRequest, Enum.SortType type, OnNewListener listener) {
    requestNews(count, alrRequest, listener, type, true);
  }

  private void requestNews(int count, int alrRequest, final OnNewListener listener, final Enum.SortType type, final boolean isMost) {
    String url = makeUrl(count, alrRequest, type);
    System.out.println("hotnews:" + url);
    OKHttpUtil.enqueue(url, new Callback() {
      @Override
      public void onFailure(Request request, IOException e) {
        listener.onFaild();
      }

      @Override
      public void onResponse(Response response) throws IOException {
        String result = response.body().string();
        System.out.println("hot news result:" + result);
        Gson gson = new Gson();
        Message<List<NewItem>> msg = gson.fromJson(result, new TypeToken<Message<List<NewItem>>>() {
        }.getType());
        List<NewItem> list = msg.getData();
        if (!msg.isSuccess()) {
          System.out.println(msg.getFlag());
          listener.onFaild();
          return;
        }
        if (!isMost) {
          listener.onSuccessWithNew(list);
        } else {
          listener.onSuccessWithMost(list);
        }

      }
    });
  }

  private String makeUrl(final int count, final int alrRequest, final Enum.SortType type) {
    final List<ParamsPair> list = new ArrayList<>();
    list.add(new ParamsPair("count", count + ""));
    list.add(new ParamsPair("alrequest", alrRequest + ""));
    list.add(new ParamsPair("hot", (type.ordinal() + 1) + ""));
    return OKHttpUtil.makeURL(NewsAPI.getNewsHotAPI(), list);
  }
}
