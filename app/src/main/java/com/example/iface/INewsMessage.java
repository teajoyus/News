package com.example.iface;

/**用于处理新闻来源的业务接口
 * Created by 林妙鸿 on 2016/6/5.
 */
@Deprecated
public interface INewsMessage {
    /**
     *
     * @param listener
     */
    public void setListener(OnResultListener listener);

    /**
     * 新闻列表的业务模型
     * @param userId
     * @param count
     * @param alrequest
     * @param tag
     */
    public void newList(String userId,int count,int alrequest,String tag);

    public void newDtail(String userId,String newId);
    public void newSort(int type, int count, int alrequest);
    public void newLove(String userId);
    public void newSearch(String keyowrd);
    public void newComment(String userId,String newId,String content);
    public void userClickLove(final String newid,final String userid, final boolean isLove);

}
