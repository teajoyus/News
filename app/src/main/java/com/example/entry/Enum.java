package com.example.entry;

/**
 * Created by Administrator on 2016/10/26.
 */
public class Enum {
  /**
   *   排序方式,1为喜欢,2为阅读,3为评论
   */
  public enum SortType {
    LOVE, READ, COMMENT
  }

  /**
   * 初始化、刷新、加载更多
   */
    public enum NewsStatus{
  INIT,FLASH,MOST
}
}
