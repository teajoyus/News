package com.example.news;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.NewsAdapter;
import com.example.entry.NewItem;
import com.example.iface.INewsMessage;
import com.example.iface.OnResultListener;
import com.example.model.NewsMessageModel;
import com.example.myview.MoreWindow;
import com.example.myview.XListView;
import com.example.myview.dialog.SpotsDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotFragment extends Fragment implements OnResultListener{

    private ImageView iv_sort_hot;
    private XListView lv_hot;
    private MoreWindow mMoreWindow;
    private NewsAdapter adapter;
    private List<NewItem> list;
    private Handler handler;
    private INewsMessage newsMessage;
    private int sortType;//排序方式,1为喜欢,2为阅读,3为评论
    private int count,alrRequest;//请求数量，已经请求数量
    private TextView title_tv;
    private AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot, container, false);
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
        iv_sort_hot = (ImageView) getActivity().findViewById(R.id.iv_sort_hot);
        lv_hot = (XListView) getActivity().findViewById(R.id.lv_hot);
        title_tv =(TextView)getActivity().findViewById(R.id.tv_title_hot) ;
        mMoreWindow = new MoreWindow(getActivity());
        dialog = new SpotsDialog(getActivity(),"正在获取新闻...");


    }
    private void initData() {
        newsMessage = new NewsMessageModel();
        mMoreWindow.init();//初始化弹窗菜单
        sortType = 2; //默认类型是2，表示按照阅读量的排序方式
        count=10;
        alrRequest=0;
        list = new ArrayList<NewItem>();
//        NewItem item = new NewItem();
//        item.setTitle("野象被偷猎者打伤 竟一拐一瘸去酒店求助");
//        item.setRead("3");
//        item.setComment("23");
//        item.setLove("2");
//        item.setUserTime("6月25日");
//        item.setLabel("娱乐");
//        for(int i=0;i<10;i++){
//            list.add(item);
//        }
//        adapter = new NewsAdapter(getActivity(),list);
        handler = new Handler();
    }

    private void initHandler() {
        lv_hot.setAdapter(adapter);
        newsMessage.setListener(this);
        newsMessage.newSort(sortType,count,alrRequest);
    }

    private void initListener() {
        lv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewDetailActivity.class);
                intent.putExtra("newId",list.get(position-1).getNewId());
                startActivity(intent);
            }
        });
        iv_sort_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreWindow.showMoreWindow(v, 100);
            }
        });
        /**
         * 设置列表的滚动监听事件，在滚动过程中隐藏排序按钮
         */
        lv_hot.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
               if(scrollState==1){
                   iv_sort_hot.setVisibility(View.GONE);
               }else if(scrollState==0){
                   iv_sort_hot.setVisibility(View.VISIBLE);

               }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        /*
         *  设置列表的下拉刷新和加载更多的监听事件
         *
        *********************************************************************/
        lv_hot.setPullLoadEnable(true);
        lv_hot.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                dialog.show();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        //下拉刷新就是做新的请求，放弃掉原先的数据
                        count = 10;
                        alrRequest = 0;
                        newsMessage.newSort(sortType, count, alrRequest);
                        stopRefresh();
                    }
                }, 1000);


            }
            @Override
            public void onLoadMore() {
                dialog.show();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        //下拉刷新就是做新的请求，放弃掉原先的数据
                        count = 5;
                        newsMessage.newSort(sortType, count, alrRequest);
                    }
                }, 1000);


            }
        });
        /*********************************************************************/
        /**
         * 自定义一个按钮的监听回调事件
         */
        mMoreWindow.setOnItemClickListener(new MoreWindow.OnItemClickListener() {
            @Override
            public void onClick(View v) {
                int type = 2;
            switch (v.getId()){
                case R.id.iv_comment_sort:type=3;title_tv.setText("热点评论");break;
                case R.id.iv_read_sort:type=2;title_tv.setText("热点阅读");break;
                case R.id.iv_love_sort_:type=1;title_tv.setText("热点喜欢");break;
            }
                /**
                 * 类型不同就必须重新刷新列表数据 之前的数据不要
                 */
                if(type!=sortType){
                    sortType =type;
                    count=10;
                    alrRequest =0;
                    newsMessage.newSort(sortType,count,alrRequest);
                }


            }
        });


    }


    /** 停止刷新， */
    private void stopRefresh() {
        lv_hot.stopRefresh();
        lv_hot.stopLoadMore();
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("MM月dd日   HH:mm:ss ");
        Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
        String   str   =   formatter.format(curDate);
        lv_hot.setRefreshTime(str);
    }

    /**
     * 获取新闻列表的结果监听事件
     */
    @Override
    public void onStartDoing() {
        stopRefresh();
        dialog.show();
        lv_hot.getmFooterView().setmHintView("加载更多..");


    }

    @Override
    public void onSuccess(Object o) {
        stopRefresh();
       dialog.dismiss();
        if(o==null||((List<NewItem>) o).size()==0){
            Toast.makeText(getActivity(),"没有更多新闻了",Toast.LENGTH_SHORT).show();
            lv_hot.getmFooterView().setmHintView("--------------已经到底了--------------");
            return;
        }
        /*
        已请求数量=0说明列表还没有过该类型数据
         */
        if(alrRequest==0){
            list =(List<NewItem>) o;
            adapter = new NewsAdapter(getActivity(),list);
            lv_hot.setAdapter(adapter);
            //如果不为0说明是加载更多
        }else{
            List<NewItem>items =(List<NewItem>) o;
            for(NewItem item:items){
                list.add(item);
            }
            alrRequest+=count;//更新已经请求数量
            adapter.notifyDataSetChanged();
        }
        alrRequest+=((List<NewItem>) o).size();

    }

    @Override
    public void onFaild(Object o) {
        dialog.dismiss();
        Toast.makeText(getActivity(),"没有更多新闻了",Toast.LENGTH_SHORT).show();
        lv_hot.getmFooterView().setmHintView("已经到底了");
    }
}
