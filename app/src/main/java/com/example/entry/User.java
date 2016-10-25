package com.example.entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 用户个人信息的实体
 * Created by 林妙鸿 on 2016/6/5.
 */
public class User extends Message<User> {
  private String phone;
  private String password;
  private String name;
  @SerializedName("user_id")
  private String id;
  private String adresss;
  private String email;
  private String avstarUrl;
  private List<Label> label;
  private String currentlabel;
  private static User user;
  private int readAllNum;
  private int loveAllNum;
  private int commentAllNum;

  public User() {
  id="";//还没登陆是游客身份
    currentlabel="";//默认是推荐新闻

  }

  public int getReadAllNum() {
    return readAllNum;
  }

  public void setReadAllNum(int readAllNum) {
    this.readAllNum = readAllNum;
  }

  public int getLoveAllNum() {
    return loveAllNum;
  }

  public void setLoveAllNum(int loveAllNum) {
    this.loveAllNum = loveAllNum;
  }

  public int getCommentAllNum() {
    return commentAllNum;
  }

  public void setCommentAllNum(int commentAllNum) {
    this.commentAllNum = commentAllNum;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAdresss() {
    return adresss;
  }

  public void setAdresss(String adresss) {
    this.adresss = adresss;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAvstarUrl() {
    return avstarUrl;
  }

  public void setAvstarUrl(String avstarUrl) {
    this.avstarUrl = avstarUrl;
  }

  public static User getUser() {
    return user;
  }

  public static void setUser(User user) {
    User.user = user;
  }

  public String getCurrentlabel() {
    return currentlabel;
  }

  public void setCurrentlabel(String currentlabel) {
    this.currentlabel = currentlabel;
  }

  public String getLabel() {
    if (label == null || label.size() == 0) {
      return "";
    } else {
      StringBuffer buffer = new StringBuffer();
      for (Label l : label) {
        String s = l.getName();
        buffer.append(s);
        buffer.append(",");
      }
      buffer.deleteCharAt(buffer.length() - 1);
      return buffer.toString();
    }
  }

  public void setLabel(List<Label> label) {
    this.label = label;
  }


  @Override
  public String toString() {
    return "User{" +
      "phone='" + phone + '\'' +
      ", password='" + password + '\'' +
      ", name='" + name + '\'' +
      ", id='" + id + '\'' +
      ", adresss='" + adresss + '\'' +
      ", email='" + email + '\'' +
      ", avstarUrl='" + avstarUrl + '\'' +
      ", label=" + label +
      ", currentlabel='" + currentlabel + '\'' +
      ", readAllNum=" + readAllNum +
      ", loveAllNum=" + loveAllNum +
      ", commentAllNum=" + commentAllNum +
      "}";
  }
}
