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
import android.widget.TextView;
import android.widget.Toast;
import com.example.adapter.NewsAdapter;
import com.example.entry.Enum;
import com.example.entry.NewItem;
import com.example.myview.MoreWindow;
import com.example.myview.XListView;
import com.example.myview.dialog.SpotsDialog;
import com.example.presenter.HotNewsPresenter;
import com.example.view.HotNewsView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HotFragment extends Fragment implements HotNewsView{
  private HotNewsPresenter presenter;
    private ImageView iv_sort_hot;
    private XListView lv_hot;
    private MoreWindow mMoreWindow;
    private NewsAdapter adapter;
    private Handler handler;
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
      presenter = new HotNewsPresenter(this);
        mMoreWindow.init();//初始化弹窗菜单
        handler = new Handler();
      presenter.requestHotNews();
    }



    private void initListener() {
        lv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewDetailActivity.class);
                intent.putExtra("newId",presenter.getList().get(position-1).getNewId());
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
                      //下拉刷新
                       presenter.requestHotNews();
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
                    //加载更多新闻
                        presenter.requestMostNews();
                    }
                }, 1000);


            }
        });
        /**
         * 自定义一个按钮的监听回调事件
         */
        mMoreWindow.setOnItemClickListener(new MoreWindow.OnItemClickListener() {
            @Override
            public void onClick(View v) {
              /**
               * 类型不同就重新刷新列表数据
               */
            switch (v.getId()){
                case R.id.iv_comment_sort:{
                  title_tv.setText("热点评论");
                  presenter.setType(Enum.SortType.COMMENT);
                  break;
                }
                case R.id.iv_read_sort:{
                  title_tv.setText("热点阅读");
                  presenter.setType(Enum.SortType.READ);
                  break;
                }
                case R.id.iv_love_sort_:{
                  title_tv.setText("热点喜欢");
                  presenter.setType(Enum.SortType.LOVE);
                  break;
                }
            }
              presenter.requestHotNews();
            }
        });


    }
  @Override
  public void sortNewsView(final List<NewItem> list) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        adapter = new NewsAdapter(getActivity(),list);
        lv_hot.setAdapter(adapter);
      }
    });

  }
  @Override
  public void moreNewsView(List<NewItem> list) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        adapter.notifyDataSetChanged();
      }
    });

  }
  @Override
  public void showDialog() {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        stopRefresh();
        dialog.show();
        lv_hot.getmFooterView().setmHintView("加载更多..");
      }
    });

  }
  @Override
  public void dismssDialog() {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        stopRefresh();
      }
    });

  }

  @Override
  public void showToast(final String msg) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
      }
    });

  }

  @Override
  public void noNewsView() {

  }

  @Override
  public void noMostView() {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            adapter.notifyDataSetChanged();
          }
        });
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

}
