package com.zhuzhenkui.notepad;

import com.shengdan.base_lib.base.BaseApplication;
import com.zhuzhenkui.notepad.database.AppDatabase;

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
         AppDatabase.init(this);


    }
}
