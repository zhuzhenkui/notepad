package com.zhuzhenkui.notepad.login.vm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.shengdan.base_lib.base.BaseViewModel;
import com.shengdan.base_lib.entity.ErrorEntity;
import com.zhuzhenkui.notepad.database.AppDatabase;
import com.zhuzhenkui.notepad.utils.AppExecutors;
import com.zhuzhenkui.notepad.login.entity.UserEntity;

public class RegisterViewModel extends BaseViewModel {

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }
    public void addUser(String username,String pwd) {
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(pwd)){
            getErrorData().postValue(new ErrorEntity(1,"请输入正确的账号密码"));
            return;
        }
        AppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                UserEntity newUser = new UserEntity();
                newUser.username = username;
                newUser.password = pwd; // 注意：实际应用中密码应加密存储
                AppDatabase.getDatabase().noteDao().insertUser(newUser);
            }
        });
    }
}
