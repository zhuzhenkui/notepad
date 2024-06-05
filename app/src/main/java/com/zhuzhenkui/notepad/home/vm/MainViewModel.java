package com.zhuzhenkui.notepad.home.vm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.shengdan.base_lib.base.BaseViewModel;
import com.shengdan.base_lib.utils.SharedPreferenceUtils;
import com.zhuzhenkui.notepad.database.AppDatabase;
import com.zhuzhenkui.notepad.home.entity.NoteEntity;
import com.zhuzhenkui.notepad.utils.AppExecutors;
import com.zhuzhenkui.notepad.utils.SpKey;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends BaseViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<NoteEntity>> noteListData = new MutableLiveData<>();
    public MutableLiveData<Integer> delEventData = new MutableLiveData<>();

    public void getNoteListData() {
        List<NoteEntity> listEntities = new ArrayList<>();
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.id = -1;
        noteEntity.title = "设计思路";
        noteEntity.updateTime = 1716613280;
        noteEntity.content = "产品经理说这次的需求稿需要";

        listEntities.add(noteEntity);
    }

    public void getNoteAllByUpdateTimeDesc() {
        int user_id = SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID);
        AppExecutors.getDiskIO().execute(() -> {
            List<NoteEntity> data = AppDatabase.getDatabase().noteDao().getNoteAllByUpdateTimeDesc(user_id);
            noteListData.postValue(data);
        });
    }

    public void getNoteAllByUpdateTimeAsc() {
        int user_id = SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID);
        AppExecutors.getDiskIO().execute(() -> {
            List<NoteEntity> data = AppDatabase.getDatabase().noteDao().getNoteAllByUpdateTimeAsc(user_id);
            noteListData.postValue(data);
        });
    }

    public void getNoteAllByCrateTimeDesc() {
        int user_id = SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID);
        Log.d("asdas", "getNoteAllByCrateTimeDesc() called");
        AppExecutors.getDiskIO().execute(() -> {
            List<NoteEntity> data = AppDatabase.getDatabase().noteDao().getNoteAllByCrateTimeDesc(user_id);
            noteListData.postValue(data);
        });
    }

    public void getNoteAllByCrateTimeAsc() {
        int user_id = SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID);
        AppExecutors.getDiskIO().execute(() -> {
            List<NoteEntity> data = AppDatabase.getDatabase().noteDao().getNoteAllByCrateTimeAsc(user_id);
            noteListData.postValue(data);
        });
    }

    public void deleteById(int id){
        AppExecutors.getDiskIO().execute(() -> {
             AppDatabase.getDatabase().noteDao().deleteById(id);
             delEventData.postValue(1);
        });
    }
}
