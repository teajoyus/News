package com.example.news;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.example.adapter.QuanAdapter;
import com.example.entry.Quan;
import com.example.iface.INewsMessage;
import com.example.iface.OnResultListener;
import com.example.model.NewsMessageModel;
import com.example.runtime.RunTime;
import com.example.myview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuanFragment extends Fragment implements OnResultListener {

    private XListView lv;
    private QuanAdapter adapter;
    private List<Quan> list;
    private Handler handler;
    private INewsMessage newsMessage;
    private ProgressBar pb_love;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quan, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initHandler();
        initListener();

    }

    private void initView() {

        lv = (XListView) getActivity().findViewById(R.id.lv_love);
    }

    private void initData() {
        newsMessage = new NewsMessageModel();
        newsMessage.setListener(this);
        list = new ArrayList<Quan>();
        Quan quan = new Quan();

        quan.setNewTitle("野象被偷猎者打伤 竟一拐一瘸去酒店求助");
        quan.setName("用户157****765");
        quan.setContent("这是用户评论新闻的内容这是用户评论新闻的内容这是用户评论新闻的内容这是用户评论新闻的内容这是用户评论新闻的内容这是用户评论新闻的内容");
        quan.setNewAbstract("这是新闻的摘要这是新闻的摘要这是新闻的摘要这是新闻的摘要这是新闻的摘要");
        quan.setTime("6月25日 12:40:20");
        quan.setReadNum("6");
        quan.setZanNum("4");
        for (int i = 0; i < 20; i++) {
            list.add(quan);
        }
        adapter = new QuanAdapter(getActivity(), list);
        handler = new Handler();

    }

    private void initHandler() {
        String userId = RunTime.getRunTimeUser().getId();
//        newsMessage.newLove(userId);
        //newsMessage.newLove(userId);
        lv.setAdapter(adapter);

    }

    private void initListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewDetailActivity.class);
                intent.putExtra("newId",list.get(position-1).getNewId());
                startActivity(intent);
            }
        });
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                       // adapter.notifyDataSetChanged();
                        String userId = RunTime.getRunTimeUser().getId();
                        //        newsMessage.newLove(userId);
                        newsMessage.newLove(userId);
                        stopRefresh();
                    }
                }, 1000);


            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        stopRefresh();
                    }
                }, 500);
                // Toast.makeText(getActivity(),"加载",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 停止刷新，
     */
    private void stopRefresh() {
        lv.stopRefresh();
        lv.stopLoadMore();
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日   HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        lv.setRefreshTime(str);
    }

    @Override
    public void onStartDoing() {
        pb_love.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(Object o) {
        pb_love.setVisibility(View.GONE);
        list = (List<Quan>) o;
        adapter = new QuanAdapter(getActivity(), list);
        lv.setAdapter(adapter);
    }

    @Override
    public void onFaild(Object o) {
        pb_love.setVisibility(View.GONE);
    }
}