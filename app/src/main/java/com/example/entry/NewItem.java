package com.example.entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 林妙鸿 on 2016/5/26 0026.
 */
public class NewItem {
   // @SerializedName("news_title")
    private String title;
    private String comment;
    private String time;
    private String read;
    private String urlNew;
    private String newId;
    private String abstracted;
    private String urlPicture;
    private String label;
    private  String source;
    private  String love;
    private String userTime;
    public NewItem(){}

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getAbstracted() {
        return abstracted;
    }

    public void setAbstracted(String abstracted) {
        this.abstracted = abstracted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getUrlNew() {
        return urlNew;
    }

    public void setUrlNew(String urlNew) {
        this.urlNew = urlNew;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    @Override
    public String toString() {
        return "NewItem{" +
                "title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                ", read='" + read + '\'' +
                ", urlNew='" + urlNew + '\'' +
                ", urlPicture=" + urlPicture +
                ", source='" + source + '\'' +
                ", love='" + love + '\'' +
                '}';
    }
}
