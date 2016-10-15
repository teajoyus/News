package com.example.entry;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class CommentItem {
    private String commentUserId;
    private String userId;
    private String newId;
    private String name;
    private String time;
    private String content;
    private String avatarUrl;
    private String num;//点赞数量
    private boolean isZan;//用户是否已经点赞过

    public CommentItem(){}

    public String getUserId() {
        return userId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public boolean isZan() {
        return isZan;
    }

    public void setZan(boolean zan) {
        isZan = zan;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "userId='" + userId + '\'' +
                ", newId='" + newId + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", num='" + num + '\'' +
                ", isZan=" + isZan +
                '}';
    }
}

