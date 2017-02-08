package com.example.model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.entry.CommentItem;
import com.example.entry.NewDetail;
import com.example.entry.NewItem;
import com.example.https.HttpRequest;
import com.example.iface.INewsMessage;
import com.example.iface.OnResultListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/5.
 */
@Deprecated
public class NewsMessageModel implements INewsMessage {
    private OnResultListener listener;

    @Override
    public void setListener(OnResultListener listener) {
        this.listener = listener;
    }

    /**
     * 新闻列表的业务模型
     *
     * @param userId
     * @param count
     * @param alrequest
     * @param tag
     */
    @Override
    public void newList(String userId, int count, int alrequest, String tag) {
        NewListAsyncTask task = new NewListAsyncTask();
        task.execute(count + "", alrequest + "", userId, tag);
    }

    @Override
    public void newDtail(String userId, String newId) {

        NewDetailAsyncTask task = new NewDetailAsyncTask();
        task.execute(newId, userId);
    }

    /**
     * 热点新闻的业务模型
     *
     * @param type
     * @param count
     * @param alrequest
     */
    @Override
    public void newSort(int type, int count, int alrequest) {

        NewHotAsyncTask task = new NewHotAsyncTask();
        task.execute(count + "", alrequest + "", type + "");
    }

    /**
     * 用户喜欢的新闻的业务模型
     *
     * @param userId
     */
    @Override
    public void newLove(String userId) {

        NewLoveAsyncTask task = new NewLoveAsyncTask();
        task.execute(userId);

    }

    /**
     * 用户搜索新闻
     *
     * @param keyowrd
     */
    public void newSearch(String keyowrd) {
        NewSearchAsyncTask task = new NewSearchAsyncTask();
        try {
            task.execute(URLEncoder.encode(keyowrd, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 用户评论新闻
     *
     * @param userId
     * @param newId
     * @param content
     */
    public void newComment(String userId, String newId, String content) {
        NewCommentAsyncTask task = new NewCommentAsyncTask();
        try {
            task.execute(userId, newId, URLEncoder.encode(content, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户点击喜欢新闻
     *
     * @param userid
     * @param isLove
     */

    public void userClickLove(final String newid, final String userid, final boolean isLove) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest.getInstance().userLoveNew(new String[]{newid, userid, isLove ? "1" : "0"});
            }
        }.start();
    }

    /**
     * 新闻列表的异步加载类，参数数组params[0]是请求数量，params[1]是已经请求数量，params[2]是用户编号，params[3]是新闻标签
     */
    public class NewListAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().newList(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if ("failed".equals(object.getString("message"))) {
                    JSONObject data = object.getJSONObject("data");
                    listener.onFaild(data.getInt("flag") + "");
                } else {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<NewItem>>() {
                    }.getType();
                    ArrayList<NewItem> list = gson.fromJson(s, type);
                    listener.onSuccess(list);
                }
                return;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            listener.onFaild("null");
        }
    }

    /**
     * 新闻列表的异步加载类，参数数组params[0]是请求数量，params[1]是已经请求数量，params[2]是用户编号，params[3]是新闻标签
     */
    public class NewSearchAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().newsearch(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if ("failed".equals(object.getString("message"))) {
                    JSONObject data = object.getJSONObject("data");
                    listener.onFaild(data.getInt("flag") + "");
                } else {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<NewItem>>() {
                    }.getType();
                    ArrayList<NewItem> list = gson.fromJson(s, type);
                    listener.onSuccess(list);
                }
                return;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            listener.onFaild("null");
        }
    }

    /**
     * 新闻内容的异步加载类，参数数组params[0]是newId，params[1]是userId
     */
    public class NewDetailAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().newDetail(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("123", s);
            try {
                JSONObject object = new JSONObject(s);
                if ("failed".equals(object.getString("message"))) {
                    Log.i("123", "failed");
                    JSONObject data = object.getJSONObject("data");
                    listener.onFaild(data.getInt("flag") + "");
                    return;
                } else {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<NewDetail>>() {
                    }.getType();
                    NewDetail detail = gson.fromJson(object.get("data").toString(), NewDetail.class);
/*
                    Log.i("123","successed");
                    NewDetail detail = new NewDetail();
                    List<CommentItem>itemList = new ArrayList<>();

                    JSONObject o = object.getJSONObject("data");
                    detail.setContent(o.getString("content"));
                    String newid = o.getString("news_id");
                  //  detail.setComment(o.getInt("is_comment")!=0);
                    detail.setLove(o.getInt("is_love")!=0);
                    detail.setNewUrl(o.getString("news_url"));
                    Log.i("1234","detail:"+detail.getNewUrl());

                    */
                    CommentItem commentItem = null;
                    JSONObject o = object.getJSONObject("data");
                    JSONArray array = o.getJSONArray("comment_list");
                    String newid = o.getString("news_id");
                    List<CommentItem>itemList = new ArrayList<>();
                    if(array!=null&&array.length()>0) {
                        for (int i = 0; i < array.length(); i++) {
                            Log.i("123",array.toString());
                            commentItem = new CommentItem();
                            JSONObject object1 = array.getJSONObject(i);
                            commentItem.setNewId(newid);
                            commentItem.setTime(object1.getString("comment_time"));
                            commentItem.setName(object1.getString("username"));
                            commentItem.setUserId(object1.getString("user_id"));
                            commentItem.setAvatarUrl(object1.getString("head_url"));
                            commentItem.setContent(object1.getString("comment_content"));
                            commentItem.setNum(object1.getString("dianzan_num"));
                            itemList.add(commentItem);

                        }
                    }
                    try {
                        JSONArray relateArray = o.getJSONArray("relate_list");
                        if (relateArray != null && relateArray.length() > 0) {
                            List<NewItem> relateList = new ArrayList<>();
                            for (int i = 0; i < relateArray.length(); i++) {
                                JSONObject relate = relateArray.getJSONObject(i);
                                NewItem item = new NewItem();
                                item.setNewId(relate.getString("news_id"));
                                item.setTitle(relate.getString("news_title"));
                                relateList.add(item);
                            }
                            detail.setRelateItems(relateList);
                        }
                    }catch (Exception e ){

                    }

                   // detail.setCommentItems(itemList);
                    Log.i("123", detail.toString());
                    listener.onSuccess(detail);
                    return;


                }
            } catch (Exception e) {
                System.err.println(e.toString() + e.getMessage());

            }

            listener.onFaild("null");
        }
    }

    /**
     * 热点新闻的异步加载类，参数数组params[0]是请求数量，params[1]是已经请求数量，params[2]是请求类型
     */
    public class NewHotAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().newHot(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            onExecute(s);
        }
    }

    private void onExecute(String s) {
        try {
            JSONObject object = new JSONObject(s);
            if ("failed".equals(object.getString("message"))) {
                JSONObject data = object.getJSONObject("data");
                listener.onFaild(data.getInt("flag") + "");
            } else {
                List<NewItem> list = new ArrayList<NewItem>();
                JSONArray array = object.getJSONArray("data");
                NewItem item = null;
                JSONObject o = null;
                for (int i = 0; i < array.length(); i++) {
                    try {
                        o = array.getJSONObject(i);
                        item = new NewItem();
                        //item.setTime(o.getString("time"));
                        item.setAbstracted(o.getString("abstract"));
                        item.setComment(o.getString("comment_times"));
                        item.setLove(o.getString("love_times"));
                        item.setNewId(o.getString("news_id"));
                        item.setSource(o.getString("source"));
                        item.setTitle(o.getString("title"));
                        item.setRead(o.getString("read_times"));
                        item.setUrlPicture(o.getString("image"));
                        item.setLabel(o.getString("tag"));
                        item.setTime(o.getString("time"));
                        list.add(item);
                    } catch (Exception e) {
                        continue;
                    }
                }
                listener.onSuccess(list);
            }
            return;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        listener.onFaild("null");
    }

    /**
     * 新欢的新闻列表的异步加载类，参数数组params[0]是userId
     */
    public class NewLoveAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().newLove(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if ("failed".equals(object.getString("message"))) {
                    JSONObject data = object.getJSONObject("data");
                    listener.onFaild(data.getInt("flag") + "");
                } else {
                    List<NewItem> list = new ArrayList<NewItem>();
                    NewItem item = null;
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        item = new NewItem();
                        item.setNewId(o.getString("news_id"));
                        item.setLabel(o.getString("tag"));
                        item.setTitle(o.getString("title"));
                        item.setUserTime(o.getString("time"));
                        item.setAbstracted(o.getString("abstract"));
                        item.setComment(o.getInt("comment_times") + "");
                        item.setLove(o.getInt("love_times") + "");
                        item.setUrlPicture(o.getString("image"));
                        item.setRead(o.getInt("read_times") + "");
                        list.add(item);
                    }
//                    System.out.println(list.toString());
                    listener.onSuccess(list);


                }
            } catch (Exception e) {

            }

            listener.onFaild("null");
        }
    }

    /**
     * 热点新闻的异步加载类，参数数组params[0]是userid，params[1]是newid，params[2]是content
     */
    public class NewCommentAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartDoing();
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.getInstance().newComment(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            onExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if ("failed".equals(object.getString("message"))) {
                    JSONObject data = object.getJSONObject("data");
                    listener.onFaild(data.getInt("flag") + "");
                } else {
                    JSONObject o = object.getJSONObject("data");
                    CommentItem item = new CommentItem();
                    //item.setTime(o.getString("time"));
                    item.setAvatarUrl(o.getString("user_image"));
                    item.setUserId(o.getString("user_id"));
                    item.setName(o.getString("user_name"));
                    item.setNewId(o.getString("news_id"));
                    item.setTime(o.getString("comment_time"));
                    item.setContent(o.getString("content"));
                    item.setNewId(o.getString("news_id"));

                    listener.onSuccess(item);
                }
                return;
            } catch (Exception e) {
                System.out.println(e.toString() + "\n" + e.getMessage());
            }
            listener.onFaild("null");
        }
    }
}
