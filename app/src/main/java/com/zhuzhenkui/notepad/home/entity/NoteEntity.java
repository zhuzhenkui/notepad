package com.zhuzhenkui.notepad.home.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "updateTime")
    public long updateTime;
    @ColumnInfo(name = "content")
    public String content;
    @ColumnInfo(name = "location")
    public String location;
    //1:学习，2:生活，3:工作
    @ColumnInfo(name = "type")
    public int type;
    @ColumnInfo(name = "createTime")
    public long createTime;

    @ColumnInfo(name = "user_id")
    public int user_id;

//    @Ignore
//    public NoteEntity(int id, String title, long updateTime, String content, String location, int type, long createTime) {
//        this.id = id;
//        this.title = title;
//        this.updateTime = updateTime;
//        this.content = content;
//        this.location = location;
//        this.type = type;
//        this.createTime = createTime;
//    }
//    @Ignore
//    public NoteEntity(int id, String title, long updateTime, String content) {
//        this.id = id;
//        this.title = title;
//        this.updateTime = updateTime;
//        this.content = content;
//    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
