package com.example.entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class NewDetail {
    private String newId;
    private String newUrl;
    @SerializedName("comment_list")
    private List<CommentItem>commentItems;
    private boolean isComment;
    private boolean isLove;
    @SerializedName("relate_list")
    private List<NewItem>relateItems;
    public String getNewId() {
        return newId;
    }
    public void setNewId(String newId) {
        this.newId = newId;
    }
    private String content;

    public List<CommentItem> getCommentItems() {
        return commentItems;
    }



    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isComment() {
        return isComment;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }

    public boolean isLove() {
        return isLove;
    }

    public void setLove(boolean love) {
        isLove = love;
    }

    public List<NewItem> getRelateItems() {
        return relateItems;
    }

    public void setCommentItems(List<CommentItem> commentItems) {
        this.commentItems = commentItems;
    }

    public void setRelateItems(List<NewItem> relateItems) {
        this.relateItems = relateItems;
    }

    @Override
    public String toString() {
        return "NewDetail{" +
                "newId='" + newId + '\'' +
                ", commentItems=" + commentItems +
                ", content='" + content + '\'' +
                ", isComment=" + isComment +
                ", isLove=" + isLove +
                '}';
    }
}
