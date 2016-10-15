package com.example.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.adapter.LoveAdapter;
import com.example.entry.NewItem;
import com.example.iface.INewsMessage;
import com.example.iface.OnResultListener;
import com.example.model.NewsMessageModel;
import com.example.myview.dialog.SpotsDialog;
import com.example.runtime.RunTime;
import com.example.myview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class LoveActivity extends Activity implements OnResultListener{
    private XListView lv;
    private LoveAdapter adapter;
    private List<NewItem> list;
    private Handler handler;
    private INewsMessage newsMessage;
    private ImageView iv_back_detail;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love);
        initView();
        initData();
        initHandler();
        initListener();
    }
    private void initView() {

        lv = (XListView) findViewById(R.id.lv_love);
        iv_back_detail = (ImageView) findViewById(R.id.iv_back_detail);
        iv_back_detail.setVisibility(View.VISIBLE);
        dialog = new SpotsDialog(LoveActivity.this,"正在获取喜欢列表...");
        dialog.show();
    }

    private void initData() {
        newsMessage = new NewsMessageModel();
        newsMessage.setListener(this);
        list = new ArrayList<NewItem>();
        NewItem item = new NewItem();
        item.setTitle("野象被偷猎者打伤 竟一拐一瘸去酒店求助");
        item.setRead("3");
        item.setComment("23");
        item.setLove("2");
        item.setUserTime("6月25日");
        item.setLabel("娱乐");
        for (int i = 0; i < 10; i++) {
            list.add(item);
        }
        // adapter = new LoveAdapter(getActivity(), list);
        handler = new Handler();

    }

    private void initHandler() {
        String userId = getIntent().getExtras().getString("userid");
//        newsMessage.newLove(userId);
        newsMessage.newLove(userId);
        //lv.setAdapter(adapter);

    }

    private void initListener() {
        iv_back_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoveActivity.this.finish();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoveActivity.this, NewDetailActivity.class);
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
        dialog.dismiss();
    }

    @Override
    public void onSuccess(Object o) {
            dialog.dismiss();
        list = (List<NewItem>) o;
        adapter = new LoveAdapter(LoveActivity.this, list);
        lv.setAdapter(adapter);
    }

    @Override
    public void onFaild(Object o) {
        dialog.dismiss();
    }
}
