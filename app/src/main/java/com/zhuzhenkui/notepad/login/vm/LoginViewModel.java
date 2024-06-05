package com.zhuzhenkui.notepad.login.vm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.shengdan.base_lib.base.BaseViewModel;
import com.shengdan.base_lib.entity.ErrorEntity;
import com.zhuzhenkui.notepad.database.AppDatabase;
import com.zhuzhenkui.notepad.utils.AppExecutors;
import com.zhuzhenkui.notepad.login.entity.UserEntity;

public class LoginViewModel extends BaseViewModel {
    public MutableLiveData<UserEntity> loginData = new MutableLiveData<>();
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void login(String userName,String pwd){
        if (TextUtils.isEmpty(userName)){
            getErrorData().postValue(new ErrorEntity(3,"请输入用户名"));
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            getErrorData().postValue(new ErrorEntity(3,"请输入密码"));
            return;
        }

        AppExecutors.getDiskIO().execute(() -> {

            UserEntity user = AppDatabase.getDatabase().noteDao().login(userName, pwd);
            if (user != null) {
                // 登录成功，可以在这里处理登录后的逻辑，比如跳转页面
                getErrorData().postValue(new ErrorEntity(0,"登录成功"));
                loginData.postValue(user);
            } else {
                getErrorData().postValue(new ErrorEntity(-1,"登录错误"));
            }

        });

    }

    public void addUser(String username,String pwd) {

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
