package com.example.https;

import com.example.api.NewsAPI;
import com.example.constant.Constant;
import com.example.util.TokenWithTime;

/**处理业务的网络层
 * 利用单例设计模式，使用整个应用只维护一个HttpRequest实例
 * Created by 林妙鸿 on 2016/6/5.
 */
public class HttpRequest {
    private  static HttpRequest request;
    private  HttpService service;
    private HttpRequest(){
        service = new HttpService();
    }
    public static HttpRequest getInstance(){
        return request==null?new HttpRequest():request;

    }

    /**
     * 对服务端请求登录
     * @param params
     * @return
     */
    public  String login(String[] params){
      return request(NewsAPI.getLoginAPI(),"passwd="+params[0]+"&phone="+params[1]);

    }

    /**
     * 注册信息到服务端
     * @param params
     * @return
     */
    public  String regist(String[] params){
        return request(NewsAPI.getRegistAPI(),"passwd="+params[0]+"&phone="+params[1]+"&name="+params[2]+"&tags="+params[3]);

    }

    /**
     * 获取后台新闻列表数据
     * @param params
     * @return
     */
    public String newList(String[] params){

        return request(NewsAPI.getNewsListAPI(), "count=" + params[0] + "&alrequest=" + params[1] + "&userid=" + params[2] + "&tag=" + params[3] );
    }
    /**
     * 点赞新闻评论发到后台
     */
    public void zan(String[] params){

         request(NewsAPI.getUserZanAPI(),  "userid=" + params[0] + "&newsid=" + params[1]+"&commenttime="+params[2]+"&comment="+params[3]  );
    }

    /**
     * 热点新闻
     * @param params
     * @return
     */
    public String newHot(String[] params){

        return request(NewsAPI.getNewsHotAPI(),"count=" + params[0] + "&alrequest=" + params[1] + "&hot=" + params[2] );
    }
    /**
     * 搜索新闻
     * @param params
     * @return
     */
    public String newsearch(String[] params){

        return request(NewsAPI.getSearchAPI(),"keyword=" + params[0] );
    }
    /**
     * 新闻内容
     * @param params
     * @return
     */

    public String newDetail(String[] params){
    return request(NewsAPI.getNewsDetailAPI(),"newsid="+params[0]+"&userid="+params[1]);

    }

    /**
     * 用户喜欢的新闻
     * @param params
     * @return
     */
    public String newLove(String[] params){
        return request(NewsAPI.getNewsLoveAPI(),"userid="+params[0]);

    }
    /**
     * 用户评论新闻
     * @param params
     * @return
     */
    public String newComment(String[] params){
        return request(NewsAPI.getNewsCommentAPI(),"userid="+params[0]+"&newsid="+params[1]+"&content="+params[2]);

    }

    /**
     * 用户信息获取
     * @param params
     * @return
     */
    public String userInfo(String[] params){
        return request(NewsAPI.getUserInfoAPI(),"userid="+params[0]);

    }
    /**
     * 用户信息修改
     * @param params
     * @return
     */
    public String userInfoChange(String[] params){
        return request(NewsAPI.getUserInfoChangeAPI(),"userid="+params[0]+"&username="+params[1]+
                "&email="+params[2]+"&image=");

    }
    public String userLoveNew(String[] params){
        return request(NewsAPI.getUserLoveAPI(),"newsid="+params[0]+"&userid="+params[1]+"&islove="+params[2]);
    }

public String allLabel(){
    return request(NewsAPI.getLabelAPI(),"");
}


    private String request(String url,String params){

        try {
            return service.request(url,params+TokenWithTime.formatGETToken());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.toString()+"\n"+e.getMessage());
        }
        return "null";
    }


}
