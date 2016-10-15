package com.example.util;

/**提供提取当前时间戳和约定的token
 * Created by 林妙鸿 on 2016/6/5.
 */
public class TokenWithTime {
    public static String getCurrentTime(){
        return System.currentTimeMillis()+"";
    }
    public static String getToken(String time){
        return MD5Util.MD5("apiNews"+time);
    }

    /**
     * 封装一个&time=xxx&token=xxx
     * @return
     */
    public static String formatGETToken(){
        String time = TokenWithTime.getCurrentTime();
        String token = TokenWithTime.getToken("1");
     //   return "&time="+time +"&tooken="+token;
        return "&time="+1 +"&tooken="+token;
    }
    public static void main(String[] args){
        System.out.println(formatGETToken());
    }
}
