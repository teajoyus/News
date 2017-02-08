package com.example.model.impl;

import android.util.Log;

import com.example.api.NewsAPI;
import com.example.constant.Constant;
import com.example.entry.Enum;
import com.example.entry.Message;
import com.example.entry.NewItem;
import com.example.entry.ParamsPair;
import com.example.iface.OnNewListener;
import com.example.model.iface.NewsModel;
import com.example.runtime.RunTime;
import com.example.util.OKHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class NewModelImpl implements NewsModel {



    @Override
    public void requestNew(String userId, int alrReq, int count, String tag, OnNewListener listener) {
        request(userId,alrReq,count,tag,listener, Enum.NewsStatus.INIT);
    }

    @Override
    public void requestMostNew(String userId, int alrReq, int count, String tag, OnNewListener listener) {
        request(userId,alrReq,count,tag,listener,Enum.NewsStatus.MOST);
    }

    @Override
    public void requestFalshNew(String userId, int alrReq, int count, String tag, OnNewListener listener) {
        request(userId,alrReq,count,tag,listener,Enum.NewsStatus.FLASH);
    }


    @Override
    public void requestSearchNew(String keyword, final OnNewListener listener) {
        final List<ParamsPair> list = new ArrayList<>();
        list.add(new ParamsPair("keyword", keyword));
        String url = OKHttpUtil.makeURL(NewsAPI.getSearchAPI(), list);
        Log.i("url", url);
        OKHttpUtil.enqueue(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                listener.onFaild();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String s = response.body().string();
                Log.i("body", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if ("failed".equals(object.getString("message"))) {
                        JSONObject data = object.getJSONObject("data");
                        if (data.getInt("flag") == -1) {
                            listener.onNotFound();
                        } else if (data.getInt("flag") == -2) {
                            listener.onFaild();
                        }
                    } else {
                        JSONArray array = object.getJSONArray("data");
                        List<NewItem> list = parseJSONArray(array);
                        listener.onSuccessWithSearch(list);
                    }
                    return;
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                listener.onFaild();

            }
        });
    }




    private void request(String userId, int alrReq, int count, String tag, final OnNewListener listener, final Enum.NewsStatus type) {
        final List<ParamsPair> list = new ArrayList<>();
        list.add(new ParamsPair("count", count + ""));
        list.add(new ParamsPair("alrequest", alrReq + ""));
        list.add(new ParamsPair("userid",userId));
        list.add(new ParamsPair("tag", tag));
        String url = OKHttpUtil.makeURL(NewsAPI.getNewsListAPI(), list);
        Log.i("url", url);
        Log.i("userid:", userId);
        Log.i("userid:", RunTime.getRunTimeUser().getId());
      OKHttpUtil.enqueue(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                listener.onFaild();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                String s = response.body().string();
              if(s==null){
                listener.onFaild();
                return;
              }
              System.out.println(s);
              Gson gson = new Gson();
              Message<List<NewItem>> msg = gson.fromJson(s,new TypeToken<Message<List<NewItem>>>(){}.getType());
              List<NewItem> list = msg.getData();
            if(!msg.isSuccess()){
              System.out.println(msg.getFlag());
              listener.onFaild();
            }else{
              if (type == Enum.NewsStatus.INIT) {
                listener.onSuccessWithNew(list);
              } else if (type == Enum.NewsStatus.FLASH) {
                listener.onSuccessWithFlash(list);
              } else if (type == Enum.NewsStatus.MOST) {
                listener.onSuccessWithMost(list);
              }
            }

              /*
                try {
                    JSONObject object = new JSONObject(s);
                    if ("failed".equals(object.getString("message"))) {
                        JSONObject data = object.getJSONObject("data");
                        if (data.getInt("flag") == -1) {
                            if (type == FLASH_TYPE) listener.onNoFlashNew();
                            else listener.onNoMostNew();
                        } else if (data.getInt("flag") == -2) {
                            listener.onFaild();
                        }
                    } else {
                        JSONArray array = object.getJSONArray("data");
                        List<NewItem> list = parseJSONArray(array);
                        if (type == INIT_TYPE) {
                            listener.onSuccessWithNew(list);
                        } else if (type == FLASH_TYPE) {
                            listener.onSuccessWithFlash(list);
                        } else if (type == MOST_TYPE) {
                            listener.onSuccessWithMost(list);
                        }
                    }
                    return;
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                listener.onFaild();
*/
            }
        });
    }
  @Deprecated
    private List<NewItem> parseJSONArray(JSONArray array) {
        NewItem item = null;
        JSONObject o = null;
        List<NewItem> list = new ArrayList<NewItem>();
        for (int i = 0; i < array.length(); i++) {
            try {
                o = array.getJSONObject(i);
                item = new NewItem();
                item.setTime(o.getString("time"));
                item.setAbstracted(o.getString("abstract"));
                item.setComment(o.getString("comment_times"));
                item.setLove(o.getString("love_times"));
                item.setNewId(o.getString("news_id"));
                item.setSource(o.getString("source"));
                item.setTitle(o.getString("title"));
                item.setRead(o.getString("read_times"));
                item.setUrlPicture(o.getString("image"));
                list.add(item);
            } catch (Exception e) {
                continue;
            }

        }
        return list;
    }
}
