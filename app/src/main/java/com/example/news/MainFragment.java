package com.example.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.NewsAdapter;
import com.example.entry.Label;
import com.example.entry.NewItem;
import com.example.iface.INewsMessage;
import com.example.iface.OnResultListener;
import com.example.model.NewsMessageModel;
import com.example.myview.dialog.SpotsDialog;
import com.example.presenter.NewPresenter;
import com.example.runtime.RunTime;
import com.example.util.NewsSharedPreferences;
import com.example.myview.NewsScrollView;
import com.example.myview.XListView;
import com.example.view.NewView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment implements NewView {

    private XListView lv;
    private NewsAdapter adapter;
    private List<NewItem> list;
    private Handler handler;
    private ImageView more;
    private EditText search;
    private NewPresenter presenter;
    /**
     * tab名称
     */
    private List<Label> newsClassify;
    private NewsScrollView scrollView;
    private INewsMessage newsMessage;
    private AlertDialog dialog;
    private String keyword;
    /**
     * 请求新闻的方式。 type=0表示刚进来，type=1表示下拉刷新，type=2表示查看更多
     */
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new NewPresenter(this);
        initView();
        initColumnData();
        initData();
        initListener();

    }

    private void initView() {
        lv = (XListView) getActivity().findViewById(R.id.lv_main);
        more = (ImageView) getActivity().findViewById(R.id.label_more_main);
        search = (EditText) getActivity().findViewById(R.id.et_search_main);

        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        scrollView = new NewsScrollView(getActivity(), R.id.hs__main, R.id.ll__main);
        dialog = new SpotsDialog(getActivity(), "正在为您推荐新闻...");

    }

    private void initData() {
        String userId = RunTime.isUserLogin() ? RunTime.getRunTimeUser().getId() : "";
        String tag = RunTime.isRecommend() ? "" : RunTime.getRunTimeUser().getCurrentlabel();
        presenter.initNew(userId);
        scrollView.setNewsClassify(newsClassify);
        scrollView.initView();
        handler = new Handler();
        dialog.show();
    }

    private void initColumnData() {
        NewsSharedPreferences s = new NewsSharedPreferences(getActivity(), "label");
//        if (RunTime.isUserLogin()) newsClassify = s.getAll();
//        else newsClassify = new ArrayList<>();
        newsClassify = s.getAll();
        Label label = new Label();
        label.setKey("");
        label.setName("推荐");
        RunTime.getRunTimeUser().setCurrentlabel("");
        newsClassify.add(0, label);
        RunTime.getRunTimeUser().setLabel(newsClassify);
    }


    private void initListener() {
        //编辑框的输入法搜索按钮的监听
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//do something;
                    // Toast.makeText(getActivity(),"true",Toast.LENGTH_SHORT).show();
                    return true;
                }
                // newsMessage.newSearch(v.getText().toString().trim());
                dialog.setMessage("正在搜索新闻..");
                dialog.show();
                keyword = v.getText().toString().trim();
                presenter.searchNew(keyword);
                v.setText("");
                hideKeyboard(getActivity(),search);
                //Toast.makeText(getActivity(),"false",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        scrollView.setOnItemClickListener(new NewsScrollView.OnItemClickListener() {
            @Override
            public void OnItemClickLister(int position, View v) {
                String tag = (String) ((TextView) v).getTag();

                RunTime.getRunTimeUser().setCurrentlabel(tag);
                String userId = RunTime.isUserLogin() ? RunTime.getRunTimeUser().getId() : "";
                if (RunTime.isRecommend()) {
                    presenter.changeNew(userId, "");
                    dialog.setMessage("正在为您推荐新闻..");
                } else {
                    presenter.changeNew(userId, tag);
                    dialog.setMessage("正在获取新闻..");
                }
                dialog.show();

            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LabelActivity.class);
                startActivityForResult(intent, 100);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewDetailActivity.class);
                intent.putExtra("newId", list.get(position - 1).getNewId());
                startActivity(intent);
            }
        });
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

                final String userId = RunTime.isUserLogin() ? RunTime.getRunTimeUser().getId() : "";
                final String tag = RunTime.isRecommend() ? "" : RunTime.getRunTimeUser().getCurrentlabel();
                if ("".equals(tag)) {
                    dialog.setMessage("正在为您推荐新闻..");
                } else {
                    dialog.setMessage("正在获取新闻..");
                }
                dialog.show();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // adapter.notifyDataSetChanged();
                        presenter.flashNew(userId, tag);
                        stopRefresh();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                if(adapter.isSearch()){
                    lv.getmFooterView().setVisibility(View.GONE);
                    return;
                }//如果是搜索关键字的上啦就不做任何请求，并且隐藏底部信息
                else{
                    lv.getmFooterView().setVisibility(View.VISIBLE);
                }
                dialog.setMessage("正在加载更多新闻..");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        String userId = RunTime.isUserLogin() ? RunTime.getRunTimeUser().getId() : "";
                        String tag = RunTime.isRecommend() ? "" : RunTime.getRunTimeUser().getCurrentlabel();
                        presenter.mostNew(userId, tag);
                        stopRefresh();
                    }
                }, 1200);
            }
        });
        lv.setOnScrollListener(new XListView.OnXScrollListener() {
            /**
             * 当头部或者底部item触发的时候 会调用
             * @param view
             */
            @Override
            public void onXScrolling(View view) {
                // Log.i("1234", "onXScrolling:");
            }

            /**
             *
             * @param view listview
             * @param scrollState 1=正在滑动 0=松开
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Log.i("1234", "onScrollStateChanged:" + scrollState);
            }

            /**
             *
             * @param view listview
             * @param firstVisibleItem  屏幕上第一个item的position
             * @param visibleItemCount  当前屏幕可视多少个item
             * @param totalItemCount    item的总数量
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Log.i("1234", "onScroll:" + firstVisibleItem + "--" + visibleItemCount + "--" + totalItemCount);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //100是在主界面添加标签后回跳
        if (requestCode == 100) {
            initColumnData();
            Log.i("123", newsClassify.toString());
            scrollView.setNewsClassify(newsClassify);
            scrollView.notifyDatasetChange();
        }
    }

    @Override
    public void initNewList(final List<NewItem> list) {
        this.list = list;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //请求完成把滚动条隐藏
                dialog.dismiss();
                adapter = new NewsAdapter(getActivity(), list);
                lv.setAdapter(adapter);
            }
        });

    }

    @Override
    public void flashNewList(final List<NewItem> list) {
        if (list == null || list.size() == 0) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //请求完成把滚动条隐藏
                dialog.dismiss();
                for (NewItem item : list) {
                    MainFragment.this.list.add(0, item);
                }
                adapter.notifyDataSetChanged();
                stopRefresh();
            }
        });

    }

    @Override
    public void mostNewList(final List<NewItem> list) {

        if (list == null || list.size() == 0) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //请求完成把滚动条隐藏
                dialog.dismiss();
                for (NewItem item : list) {
                    MainFragment.this.list.add(item);
                }
                adapter.notifyDataSetChanged();
                stopRefresh();
            }
        });
    }

    @Override
    public void searchNewList(final List<NewItem> list) {
        if (list == null || list.size() == 0) {
            showToast("没有找到含有该关键字的新闻");
            return;
        }
        this.list = list;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //请求完成把滚动条隐藏
                dialog.dismiss();
                adapter = new NewsAdapter(getActivity(), list);
                adapter.setSearch(true);
                adapter.setKeyWord(keyword);
                lv.setAdapter(adapter);
                lv.getmFooterView().setVisibility(View.GONE);
                stopRefresh();
            }
        });

    }

    @Override
    public void showToast(final String msg) {
        stopRefresh();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //请求完成把滚动条隐藏
                dialog.dismiss();
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}