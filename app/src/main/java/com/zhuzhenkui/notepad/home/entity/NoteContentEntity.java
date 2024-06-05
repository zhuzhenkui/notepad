package com.zhuzhenkui.notepad.home.entity;

import android.util.Log;

public class NoteContentEntity {
    private int contentId;
    //1:文字，2:图片，3:手写，4:音乐
    private int type;
    private String contentString;
    private String imageUrl;
    private String voiceUrl;

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContentString() {
        return contentString;
    }

    public void setContentString(String contentString) {
        this.contentString = contentString;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public NoteContentEntity(int contentId, int type, String contentString, String imageUrl, String voiceUrl) {
        this.contentId = contentId;
        this.type = type;
        this.contentString = contentString;
        this.imageUrl = imageUrl;
        this.voiceUrl = voiceUrl;
    }

    @Override
    public String toString() {
        return "NoteContentEntity{" +
                "contentId=" + contentId +
                ", type=" + type +
                ", contentString='" + contentString + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceUrl='" + voiceUrl + '\'' +
                '}';
    }
}
