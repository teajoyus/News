package com.example.entry;

import java.util.List;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class Quan {
    private String avator;
    private String name;
    private String userId;
    private String content;
    private String time;
    private String readNum;
    private String zanNum;
    private String newId;
    private String newTitle;
    private String newAbstract;
    private String newPicture;
    private List<CommentItem>commentItems;

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getZanNum() {
        return zanNum;
    }

    public void setZanNum(String zanNum) {
        this.zanNum = zanNum;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getNewAbstract() {
        return newAbstract;
    }

    public void setNewAbstract(String newAbstract) {
        this.newAbstract = newAbstract;
    }

    public String getNewPicture() {
        return newPicture;
    }

    public void setNewPicture(String newPicture) {
        this.newPicture = newPicture;
    }

    public List<CommentItem> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(List<CommentItem> commentItems) {
        this.commentItems = commentItems;
    }

    @Override
    public String toString() {
        return "Quan{" +
                "avator='" + avator + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", readNum='" + readNum + '\'' +
                ", zanNum='" + zanNum + '\'' +
                ", newId='" + newId + '\'' +
                ", newTitle='" + newTitle + '\'' +
                ", newAbstract='" + newAbstract + '\'' +
                ", newPicture='" + newPicture + '\'' +
                ", commentItems=" + commentItems +
                '}';
    }
}
