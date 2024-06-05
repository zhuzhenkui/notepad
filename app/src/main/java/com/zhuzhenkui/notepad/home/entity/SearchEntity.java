package com.zhuzhenkui.notepad.home.entity;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class SearchEntity {
    public int id;
    public String title;
    public long updateTime;
    public String content;
    public String location;
    //1:学习，2:生活，3:工作
    public int type;
    public long createTime;

    public int user_id;

    public String searchContent;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public String getContent() {
        return content;
    }

    public String getLocation() {
        return location;
    }

    public int getType() {
        return type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getSearchContent() {
        return searchContent;
    }
}
