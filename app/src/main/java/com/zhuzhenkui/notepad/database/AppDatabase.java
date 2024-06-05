package com.zhuzhenkui.notepad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.zhuzhenkui.notepad.home.entity.NoteContentDbEntity;
import com.zhuzhenkui.notepad.home.entity.NoteEntity;
import com.zhuzhenkui.notepad.login.entity.UserEntity;

@Database(entities = {UserEntity.class, NoteEntity.class, NoteContentDbEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase()  {
        if (INSTANCE == null) {
            throw new NullPointerException("please initialize AppDatabase first");
        }
        return INSTANCE;
    }

    public static void init(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "user_database")
                            .build();
                }
            }
        }
    }


}
