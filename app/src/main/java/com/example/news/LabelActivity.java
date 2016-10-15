package com.example.news;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.entry.Label;
import com.example.iface.IUserModel;
import com.example.iface.OnResultListener;
import com.example.model.UserModel;
import com.example.util.NewsSharedPreferences;
import java.util.List;
import com.example.adapter.LabelAdapter;
public class LabelActivity extends Activity implements OnResultListener {

    private GridView alllabel;
    private List<Label>labelList;
    private Button btnLabel;
    private IUserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label);
        initView();
        initDatas();
        initListener();

    }

    /**
     * 初始化控件变量
     */
    private void initView() {
        alllabel = (GridView) findViewById(R.id.gd__alllabel_label);
        btnLabel = (Button) findViewById(R.id.btn_label);//确定标签选择按钮
    }

    /**
     * 初始化事件监听
     */
    private void initListener() {
        alllabel.setOnItemClickListener(new MyItemClickListener());
        btnLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsSharedPreferences nsp = new NewsSharedPreferences(LabelActivity.this, "label");
                nsp.removeAll();
                for (Label s : labelList) {
                    if(s.isSelected())nsp.save(s.getKey(), s.getName());
                }
                LabelActivity.this.finish();
            }
        });
    }
    @Override
    public void onStartDoing() {

    }

    /**
     * 新闻标签请求回调函数
     * @param o
     */
    @Override
    public void onSuccess(Object o) {

        labelList = (List<Label>) o;
        NewsSharedPreferences nsp = new NewsSharedPreferences(LabelActivity.this,"label");
        //从缓存里取出数据来匹配哪些是用户已经选择的标签
        for(Label l:labelList){
            l.setIsSelected(!"".equals(nsp.load(l.getKey())));
        }
        alllabel.setAdapter(new LabelAdapter(LabelActivity.this, labelList));

    }

    @Override
    public void onFaild(Object o) {
        Toast.makeText(LabelActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();

    }

    /**
     * 标签的事件监听
     */
    class MyItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView textView = (TextView) view;
            String s = textView.getText().toString().trim();
            if (labelList.get(position).isSelected()) {
                labelList.get(position).setIsSelected(false);
                textView.setTextColor(0xA0000000);
            } else {
                labelList.get(position).setIsSelected(true);
                textView.setTextColor(0xA0EE7621);
            }
        }
    }

    /**
     * 初始化标签数据请求
     */
    private void initDatas() {
        userModel =new UserModel();
        userModel.setListener(this);
        userModel.alllabel();//请求服务器的新闻标签数据
        /*
        hotList = new ArrayList<String>();
        hotList.add("国际");
        hotList.add("科技");
        hotList.add("明星");
        hotList.add("美女");
        hotList.add("军事");
        hotList.add("娱乐");
        allList = new ArrayList<String>();
        allList.add("社会");
        allList.add("娱乐");
        allList.add("科技");
        allList.add("汽车");
        allList.add("体育");
        allList.add("财经");
        allList.add("军事");
        allList.add("国际");
        allList.add("时尚");
        allList.add("旅游");
        allList.add("探索");
        allList.add("育儿");
        allList.add("养生");
        allList.add("故事");
        allList.add("美文");
        allList.add("游戏");
        allList.add("历史");
        allList.add("美食");
        */

    }



}
