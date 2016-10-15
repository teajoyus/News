package com.example.constant;

/**
 * Created by Administrator on 2016/6/5.
 */
public class Constant {
    //内网接口
    public static  String IP_IN = "172.17.32.35";
    //外网接口1
    public static  String IP_OUT1 = "45.78.24.111";
    //外网接口2
    public static  String IP_OUT2 = "123.207.236.147";
    //内网端口
    public static final String PORT_IN= "8888";
    //外网端口1
    public static final String PORT_OUT1= "8884";
    //外网端口2
    public static final String PORT_OUT2= "8888";

    //登录API
    public static final String LOGIN = "/api/login";
    //注册API
    public static final String REGIST = "/api/register";
    //新闻列表API
    public static final String NEWSLIST = "/api/newstags";
    //热点新闻
    public static final String NEWSHOT =  "/api/hotlist";
    //搜索新闻
    public static final String NEWSSEARCH =   "/api/keyword";

    //新闻内容API
    public static final String NEWDETAIL =  "/api/newscontent";
    //用户喜欢新闻PAI
    public static final String NEWLOVE =   "/api/lovelist";
    //用户评论新闻API
    public static final String NEWCOMMENT =   "/api/comment";
    //用户信息获取API
    public static final String USERINFO =   "/api/userinfo";
    //用户信息修改
    public static final String USERINFOCHANGE =   "/api/userinfochange";
    //用户点赞API
    public static final String USERZAN =   "/api/lovecomment";
    //用户反馈API
    public static final String FEEDBACK =   "/api/feedback";
    //所有标签
    public static final String ALLLABEL =   "/api/returntags";
    //用户点击喜欢新闻
    public static final String USERLOVE =   "/api/lovenews";




}
