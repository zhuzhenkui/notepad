package com.zhuzhenkui.notepad.home.utils;

import com.zhuzhenkui.notepad.home.entity.NoteContentDbEntity;
import com.zhuzhenkui.notepad.home.entity.NoteContentEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NoteDataUtil {

    public static List<NoteContentEntity> noteContentDbList2NoteContentList(List<NoteContentDbEntity> list) {
        //调整序列
        list.sort(Comparator.comparingInt(NoteContentDbEntity::getPosition));
        List<NoteContentEntity> data = new ArrayList<>();
        for (NoteContentDbEntity noteContentDbEntity : list) {
            data.add(noteContentDb2NoteContent(noteContentDbEntity));
        }
        return data;
    }

    public static NoteContentEntity noteContentDb2NoteContent(NoteContentDbEntity noteContentDbEntity) {
        return new NoteContentEntity(noteContentDbEntity.id, noteContentDbEntity.type,
                noteContentDbEntity.contentString, noteContentDbEntity.imageUrl,noteContentDbEntity.voiceUrl);
    }

    public static NoteContentDbEntity noteContent2NoteContentDb(NoteContentEntity noteContentEntity, int position, int noteId, int user_id, String title){
        NoteContentDbEntity db = new NoteContentDbEntity();
        if (noteContentEntity.getType() == 1) {
            //文字
            db.type = 1;
            db.contentString = noteContentEntity.getContentString();
        } else if (noteContentEntity.getType() == 2) {
            //图片
            db.type = 2;
            db.imageUrl = noteContentEntity.getImageUrl();
        } else if (noteContentEntity.getType() == 3) {
            //手写
        }else if (noteContentEntity.getType() == 4) {
            //音乐
            db.type = 4;
            db.voiceUrl = noteContentEntity.getVoiceUrl();
        }
        db.position = position;
        db.note_id = (int) noteId;
        db.user_id = user_id;
        db.title = title;
        db.id = noteContentEntity.getContentId();
        return db;
    }

    public static NoteContentDbEntity noteContent2NoteContentDbNoId(NoteContentEntity noteContentEntity, int position, int noteId, int user_id, String title){
        NoteContentDbEntity db = new NoteContentDbEntity();
        if (noteContentEntity.getType() == 1) {
            //文字
            db.type = 1;
            db.contentString = noteContentEntity.getContentString();
        } else if (noteContentEntity.getType() == 2) {
            //图片
            db.type = 2;
            db.imageUrl = noteContentEntity.getImageUrl();
        } else if (noteContentEntity.getType() == 3) {
            //手写
        }else if (noteContentEntity.getType() == 4) {
            //音乐
            db.type = 4;
            db.voiceUrl = noteContentEntity.getVoiceUrl();
        }
        db.position = position;
        db.note_id = (int) noteId;
        db.title = title;
        db.user_id = user_id;
        return db;
    }

}
