package com.zhuzhenkui.notepad.home.vm;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.shengdan.base_lib.base.BaseViewModel;
import com.shengdan.base_lib.utils.SharedPreferenceUtils;
import com.zhuzhenkui.notepad.database.AppDatabase;
import com.zhuzhenkui.notepad.home.entity.NoteContentDbEntity;
import com.zhuzhenkui.notepad.home.entity.NoteEntity;
import com.zhuzhenkui.notepad.utils.AppExecutors;
import com.zhuzhenkui.notepad.utils.SpKey;
import com.zhuzhenkui.notepad.home.entity.SearchEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchViewModel extends BaseViewModel {
    public MutableLiveData<List<SearchEntity>> noteListData = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    HashMap<Integer, Boolean> isFillMap = new HashMap<>();
    HashMap<Integer, SearchEntity> idSearchMap = new HashMap<>();

    public void searchNotesByTitle(String text) {
        if (TextUtils.isEmpty(text)) {
            noteListData.postValue(null);
            return;
        }

        int user_id = SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID);
        AppExecutors.getDiskIO().execute(() -> {
            idSearchMap.clear();
            isFillMap.clear();

            List<SearchEntity> searchEntities = new ArrayList<>();
            //根据标题检索的结果
            List<NoteEntity> data = AppDatabase.getDatabase().noteDao().searchNotesByTitle(user_id, text);
            List<NoteContentDbEntity> data_content = AppDatabase.getDatabase().noteDao().searchNotesByContent(user_id, text);
            Log.d("TAG", "searchNotesByTitle: data " + data.size() + " data_content " + data_content.size());
            for (int i = 0; i < data.size(); i++) {
                //未填充内容，即纯标题
                SearchEntity searchEntity = noteEntity2SearchEntity(data.get(i), "");
                searchEntities.add(searchEntity);
                isFillMap.put(data.get(i).getId(), false);
                idSearchMap.put(data.get(i).getId(),searchEntity);
            }


            for (NoteContentDbEntity noteContentDbEntity : data_content) {
                SearchEntity searchEntity = idSearchMap.get(noteContentDbEntity.note_id);
                if (searchEntity != null) {
                    //包含了,SearchEntity 并且未填充过 则赋值 contentString
                    if (!isFillMap.get(noteContentDbEntity.note_id)){
                        searchEntity.setSearchContent(noteContentDbEntity.contentString);
                        isFillMap.put(noteContentDbEntity.note_id, true);
                    }
                } else {
                    //不包含 重新根据noteId查询一下
                    NoteEntity noteEntity = AppDatabase.getDatabase().noteDao().getNotesByNoteId(noteContentDbEntity.note_id);
                    searchEntity = noteEntity2SearchEntity(noteEntity, noteContentDbEntity.contentString);
                    searchEntities.add(searchEntity);
                    data.add(noteEntity);

                    idSearchMap.put(noteEntity.getId(), searchEntity);
                    isFillMap.put(noteEntity.getId(), true);
                }
            }

//            for (NoteContentDbEntity noteContentDbEntity : data_content) {
//                if (idPositionMap.containsKey(noteContentDbEntity.note_id)) {
//                    //包含了,取出NoteEntity 转成 SearchEntity 并且赋值contentString
//                    if (!isFillMap.get(noteContentDbEntity.note_id)){
//                        SearchEntity searchEntity = noteEntity2SearchEntity(data.get(idPositionMap.get(noteContentDbEntity.note_id)), noteContentDbEntity.contentString);
//                        searchEntities.add(searchEntity);
//                        isFillMap.put(noteContentDbEntity.note_id, true);
//                    }
//                } else {
//                    //不包含 重新根据noteId查询一下
//                    NoteEntity noteEntity = AppDatabase.getDatabase().noteDao().getNotesByNoteId(noteContentDbEntity.note_id);
//                    SearchEntity searchEntity = noteEntity2SearchEntity(noteEntity, noteContentDbEntity.contentString);
//                    searchEntities.add(searchEntity);
//                    data.add(noteEntity);
//                    idPositionMap.put(noteEntity.getId(), data.size() - 1);
//                    isFillMap.put(noteEntity.getId(), true);
//                }
//            }

            noteListData.postValue(searchEntities);
        });
    }

    SearchEntity noteEntity2SearchEntity(NoteEntity noteEntity, String searchContent) {
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setId(noteEntity.getId());
        searchEntity.setContent(noteEntity.getContent());
        searchEntity.setTitle(noteEntity.getTitle());
        searchEntity.setType(noteEntity.type);
        searchEntity.setCreateTime(noteEntity.createTime);
        searchEntity.setUpdateTime(noteEntity.updateTime);
        searchEntity.setUser_id(noteEntity.user_id);
        searchEntity.setLocation(noteEntity.location);
        searchEntity.setSearchContent(searchContent);
        return searchEntity;
    }


}
