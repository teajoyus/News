package com.example.entry;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/19.
 */
public class Message<T> implements Serializable{

  /**
   * data : {"flag":-1}
   * message : failed
   */
  private T data;
  protected String message;
  private int flag;
  public void setData(T data) {
    this.data = data;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public String getMessage() {
    return message;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  public int getFlag() {
    return flag;
  }
  public boolean isSuccess(){
    return "success".equals(message);
  }

  @Override
  public String toString() {
    return "Message{" +
      "data=" + data +
      ", message='" + message + '\'' +
      ", flag=" + flag +
      '}';
  }

}
