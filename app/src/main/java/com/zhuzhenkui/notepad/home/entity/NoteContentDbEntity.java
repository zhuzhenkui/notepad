package com.zhuzhenkui.notepad.home.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_content")
public class NoteContentDbEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "user_id")
    public int user_id;
    @ColumnInfo(name = "note_id")
    public int note_id;

    //1:文字，2:图片，3:手写，4:音乐
    @ColumnInfo(name = "type")
    public int type;
    @ColumnInfo(name = "contentString")
    public String contentString;
    @ColumnInfo(name = "imageUrl")
    public String imageUrl;
    @ColumnInfo(name = "voiceUrl")
    public String voiceUrl;

    @ColumnInfo(name = "position")
    public int position;

    @ColumnInfo(name = "title")
    public String title;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getNote_id() {
        return note_id;
    }

    public int getType() {
        return type;
    }

    public String getContentString() {
        return contentString;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "NoteContentDbEntity{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", note_id=" + note_id +
                ", type=" + type +
                ", contentString='" + contentString + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceUrl='" + voiceUrl + '\'' +
                ", position=" + position +
                ", title='" + title + '\'' +
                '}';
    }
}
