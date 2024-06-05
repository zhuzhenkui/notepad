package com.zhuzhenkui.notepad.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zhuzhenkui.notepad.home.entity.NoteContentDbEntity;
import com.zhuzhenkui.notepad.home.entity.NoteEntity;
import com.zhuzhenkui.notepad.login.entity.UserEntity;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    UserEntity login(String username, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNote(NoteEntity user);

    /**
     * 根据修改时间降序排序
     * @param user_id
     * @return
     */
    @Query("SELECT * FROM notes WHERE user_id = :user_id ORDER BY updateTime DESC")
    List<NoteEntity> getNoteAllByUpdateTimeDesc(int user_id);

    /**
     * 根据修改时间升序排序
     * @param user_id
     * @return
     */
    @Query("SELECT * FROM notes WHERE user_id = :user_id ORDER BY updateTime ASC")
    List<NoteEntity> getNoteAllByUpdateTimeAsc(int user_id);

    /**
     * 根据创建时间降序排序
     * @param user_id
     * @return
     */
    @Query("SELECT * FROM notes WHERE user_id = :user_id ORDER BY createTime DESC")
    List<NoteEntity> getNoteAllByCrateTimeDesc(int user_id);

    /**
     * 根据创建时间升序排序
     * @param user_id
     * @return
     */
    @Query("SELECT * FROM notes WHERE user_id = :user_id ORDER BY createTime ASC")
    List<NoteEntity> getNoteAllByCrateTimeAsc(int user_id);

    @Query("SELECT * FROM notes WHERE user_id = :user_id AND title LIKE '%' || :query || '%' ")
    List<NoteEntity> searchNotesByTitle(int user_id,String query);

    @Query("SELECT * FROM notes WHERE id = :note_id")
    NoteEntity getNotesByNoteId(int note_id);

//    @Query("SELECT * FROM note_content WHERE user_id = :user_id AND (contentString LIKE '%' || :query || '%' OR title LIKE :query)")
    @Query("SELECT * FROM note_content WHERE user_id = :user_id AND contentString LIKE '%' || :contentString || '%'")
    List<NoteContentDbEntity> searchNotesByContent(int user_id, String contentString);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNoteAndReturnId(NoteContentDbEntity note);

    // 批量插入方法
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNoteAll(List<NoteContentDbEntity> entities);

    @Query("SELECT * FROM note_content WHERE note_id = :note_id")
    List<NoteContentDbEntity> getNotesContentByNoteId(int note_id);

    // 在DAO中定义一个自定义查询来更新特定字段
    @Query("UPDATE notes SET title = :title, updateTime = :updateTime,content = :content WHERE id = :id")
    int updateNote(int id, String title, long updateTime, String content);

    @Update()
    void updateContent(NoteContentDbEntity noteContentDbEntity);

    // 或者直接使用Long类型ID作为参数，如果你知道ID的话
    @Query("DELETE FROM notes WHERE id = :id")
    void deleteById(long id);
}
