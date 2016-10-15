package com.example.api;

import com.example.constant.Constant;

/**
 * Created by Administrator on 2016/8/11.
 */
public class NewsAPI {
    private static String IP;
    private static String PORT;

    static {
        IP = Constant.IP_OUT2;
        PORT = Constant.PORT_OUT2;
    }

    /**
     * 更换接口方案 总共有三套
     * @param i
     */
   public static void changeIPandPORT(int i){
       switch (i){
           case 0:  IP = Constant.IP_IN;
               PORT = Constant.PORT_IN;
               break;
           case 1:  IP = Constant.IP_OUT1;
               PORT = Constant.PORT_OUT1;
               break;
           case 2:  IP = Constant.IP_OUT2;
               PORT = Constant.PORT_OUT2;
               break;
       }
   }
    public static int getAPIType(){
        if(IP.equals(Constant.IP_IN))return 0;
        if(IP.equals(Constant.IP_OUT1))return 1;
        if(IP.equals(Constant.IP_OUT2))return 2;
        else return 0;
    }
    private static String url() {
        return "http://" + IP + ":" + PORT;
    }

    public static String getLoginAPI() {
        return url() + Constant.LOGIN;
    }

    public static String getRegistAPI() {
        return url() + Constant.REGIST;
    }

    public static String getNewsListAPI() {
        return url() + Constant.NEWSLIST;
    }

    public static String getNewsHotAPI() {
        return url() + Constant.NEWSHOT;
    }
    public static String getSearchAPI(){
        return url()+ Constant.NEWSSEARCH;
    }
    public static String getNewsDetailAPI(){
        return url()+ Constant.NEWDETAIL;
    }
    public static String getNewsLoveAPI(){
        return url()+ Constant.NEWLOVE;
    }
    public static  String getNewsCommentAPI(){
        return url()+ Constant.NEWCOMMENT;
    }
    public static  String getUserInfoAPI(){
        return url()+ Constant.USERINFO;
    }
    public static  String getUserInfoChangeAPI(){
        return url()+ Constant.USERINFOCHANGE;
    }
    public static  String getUserZanAPI(){
        return url()+ Constant.USERZAN;
    }
    public static  String getUserFeedbackAPI(){
        return url()+  Constant.FEEDBACK;
    }
    public static  String getLabelAPI(){
        return url()+ Constant.ALLLABEL;
    }
    public static  String getUserLoveAPI(){
        return url()+ Constant.USERLOVE;
    }
}
