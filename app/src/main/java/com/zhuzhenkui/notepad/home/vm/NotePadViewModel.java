package com.zhuzhenkui.notepad.home.vm;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.shengdan.base_lib.base.BaseViewModel;
import com.shengdan.base_lib.entity.ErrorEntity;
import com.shengdan.base_lib.utils.SharedPreferenceUtils;
import com.zhuzhenkui.notepad.Const;
import com.zhuzhenkui.notepad.database.AppDatabase;
import com.zhuzhenkui.notepad.home.entity.NoteContentDbEntity;
import com.zhuzhenkui.notepad.home.entity.NoteContentEntity;
import com.zhuzhenkui.notepad.home.entity.NoteEntity;
import com.zhuzhenkui.notepad.home.utils.NoteDataUtil;
import com.zhuzhenkui.notepad.utils.AppExecutors;
import com.zhuzhenkui.notepad.utils.SpKey;

import java.util.ArrayList;
import java.util.List;

public class NotePadViewModel extends BaseViewModel {
    public NotePadViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<NoteContentEntity>> noteListData = new MutableLiveData<>();
    public MutableLiveData<NoteContentEntity> newNoteData = new MutableLiveData<>();
    public MutableLiveData<Integer> saveNoteEvent = new MutableLiveData<>();
    public MutableLiveData<Boolean> saveNoteData = new MutableLiveData<>();
    public MutableLiveData<Object> delPressData = new MutableLiveData<>();
    public ObservableField<Integer> timeDownCountObs = new ObservableField<Integer>(-1);
    public String noteTitle = "";

    private int noteId;

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void loadNote() {
        List<NoteContentEntity> noteData = new ArrayList<>();
        if (getNoteId() == -1) {
            noteData.add(new NoteContentEntity(Const.TITLE_ID, 1, noteTitle, "", ""));
            //新笔记
        } else {
            //旧笔记,发起查询
            AppExecutors.getDiskIO().execute(() -> {
                List<NoteContentDbEntity> data = AppDatabase.getDatabase().noteDao().getNotesContentByNoteId(noteId);
                Log.d("csdcsd", "loadNote: " + data);
                noteData.addAll(NoteDataUtil.noteContentDbList2NoteContentList(data));
                //手动新增标题
                noteData.add(0, new NoteContentEntity(Const.TITLE_ID, 1, noteTitle, "", ""));
            });
        }
        noteListData.postValue(noteData);

    }

    public void addImageNote(String absolutePath) {
        Log.d("TAG", "addImageNote() called with: absolutePath = [" + absolutePath + "]");
        NoteContentEntity newNote = new NoteContentEntity(-1, 2, "", absolutePath, "");
        List<NoteContentEntity> noteData = new ArrayList<>();
        noteData.add(newNote);
        noteData.add(getContentStingData());
        noteListData.postValue(noteData);
    }

    public void addAudioNote(String absolutePath) {
        Log.d("TAG", "addImageNote() called with: absolutePath = [" + absolutePath + "]");
        NoteContentEntity newNote = new NoteContentEntity(-1, 4, "", "", absolutePath);
        List<NoteContentEntity> noteData = new ArrayList<>();
        noteData.add(newNote);
        noteData.add(getContentStingData());
        noteListData.postValue(noteData);
    }

    public void addNewData() {
        newNoteData.postValue(getContentStingData());
    }

    NoteContentEntity getContentStingData() {
        return new NoteContentEntity(-1, 1, "", "", "");
    }

    public void delPress() {
        delPressData.postValue("");
    }

    public void setDownCount(int time) {
        timeDownCountObs.set(time);
    }

    public void toSave() {
        saveNoteEvent.postValue(noteId);
    }

    public void insertNewNote(String title, String content, String location, int type, List<NoteContentEntity> data) {
        if (TextUtils.isEmpty(title)) {
            getErrorData().postValue(new ErrorEntity(-1, "请输入标题"));
            return;
        }
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.title = title;
        noteEntity.updateTime = System.currentTimeMillis();
        noteEntity.content = content;
        noteEntity.location = location;
        noteEntity.type = type;
        noteEntity.createTime = System.currentTimeMillis();
        noteEntity.user_id = SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID);

        AppExecutors.getDiskIO().execute(() -> {
            long id = AppDatabase.getDatabase().noteDao().insertNote(noteEntity);

            if (id != -1) {
                data.remove(0);
                for (int i = 0; i < data.size(); i++) {
                    insertContent(data.get(i), id, i,title);
                }
                saveNoteData.postValue(true);
            } else {
                getErrorData().postValue(new ErrorEntity(-1, "保存出错"));
            }
        });
    }

    public void updateNoteAndContent(String title, String content, String location, int type, List<NoteContentEntity> data) {
        if (data.size() == 0) return;


        AppExecutors.getDiskIO().execute(() -> {
            long res = AppDatabase.getDatabase().noteDao().updateNote(noteId, title, System.currentTimeMillis(), content);
            if (res != -1) {
                //第一项是标题，移除
                data.remove(0);

                for (int i = 0; i < data.size(); i++) {
                    Log.d("TAG", "updateNoteAndContent: "+data.get(i).getContentId()+"  "+i);
                    if (data.get(i).getContentId() == -1) {
                        //插入
                        insertContent(data.get(i), noteId, i,title);
                    } else {
                        //更新
                        updateContent(data.get(i), noteId, i,title);
                    }
                }

                saveNoteData.postValue(true);
            } else {
                getErrorData().postValue(new ErrorEntity(-1, "保存出错"));
            }
        });

    }

    private void updateContent(NoteContentEntity data, int noteId, int position,String title) {
        NoteContentDbEntity db = NoteDataUtil.noteContent2NoteContentDb(data, position, (int) noteId,
                SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID),title);
        AppExecutors.getDiskIO().execute(() -> AppDatabase.getDatabase().noteDao().updateContent(db));

    }


    private void insertContent(NoteContentEntity data, long noteId, int position,String title) {
        NoteContentDbEntity db = NoteDataUtil.noteContent2NoteContentDbNoId(data, position, (int) noteId,
                SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID), title);

        AppExecutors.getDiskIO().execute(() -> {
            AppDatabase.getDatabase().noteDao().insertNoteAndReturnId(db);
        });
    }

}
