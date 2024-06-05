package com.zhuzhenkui.notepad.utils;

import com.zhuzhenkui.notepad.login.entity.UserEntity;

public class UserManager {
    private static UserManager instance;
    private  UserEntity user; // 使用内存存储用户列表，实际应用中应替换为数据库操作

    private UserManager() {
        // 初始化数据或从数据库加载数据的操作可以放在这里
    }

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }


}
