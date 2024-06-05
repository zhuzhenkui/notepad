package com.shengdan.base_lib.utils;

import android.text.TextUtils;


/**
 * 本地Ticket管理
 * 内存保存一份，sp中保存一份
 * 保存ticket、current_time、expire三个属性
 * sp中加密存放
 */
public class TokenManager {
    private static volatile TokenManager sTokenManager;

    private String token;
    private String refreshToken;
    private String TokenSPKey = "TOKEN";

    private TokenManager() {
    }


    public static TokenManager getInstance() {
        if (sTokenManager == null) {
            synchronized (TokenManager.class) {
                if (sTokenManager == null) {
                    sTokenManager = new TokenManager();
                }
            }
        }
        return sTokenManager;
    }


    public void saveToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            // 设置到内存
            this.token = token;

            //设置本地,这里可以做一个加密保存，目前不加密
            SharedPreferenceUtils.getInstance().savePreference(TokenSPKey, token);
        }
    }


    public void saveRefreshToken(String refreshToken) {
        if (!TextUtils.isEmpty(refreshToken)) {
            // 设置到内存
            this.refreshToken = refreshToken;
            //设置本地
            SharedPreferenceUtils.getInstance().savePreference(TokenSPKey, token);
        }
    }


    /**
     * 备注： 获取刷新token  webview种cookie的时候需要
     */
    public String getRefreshToken() {
        // 如果内存中没有token，从sp中读取，然后设置到内存
        if (TextUtils.isEmpty(refreshToken)) {
            refreshToken = SharedPreferenceUtils.getInstance().getSharePreferenceString(TokenSPKey);
        }
        return refreshToken;
    }


    /**
     * 获取token
     */
    public String getToken() {
        // 如果内存中没有token，从sp中读取，然后设置到内存
        if (TextUtils.isEmpty(token)) {
            token = SharedPreferenceUtils.getInstance().getSharePreferenceString(TokenSPKey);
        }
        return token;
    }


    /**
     * 项目名称：
     * 修改时间：2018/9/4 下午3:17
     * 类描述：   清除用户信息相关
     * 修改备注：
     *
     * @version V1.0
     */
    public void clearToken() {
        this.token = null;
        this.refreshToken = null;
        SharedPreferenceUtils.getInstance().savePreference(TokenSPKey, "");

    }


    public boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }
}
