package com.zhuzhenkui.notepad.login.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shengdan.base_lib.base.BaseVMActivity;
import com.shengdan.base_lib.utils.SharedPreferenceUtils;
import com.shengdan.base_lib.utils.TokenManager;
import com.zhuzhenkui.notepad.home.activity.MainActivity;
import com.zhuzhenkui.notepad.login.entity.UserEntity;
import com.zhuzhenkui.notepad.login.vm.LoginViewModel;
import com.zhuzhenkui.notepad.utils.SpKey;
import com.zhuzhenkui.notepad.R;
import com.zhuzhenkui.notepad.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseVMActivity<LoginViewModel, ActivityLoginBinding> {
    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        //只添加一次
        boolean f = SharedPreferenceUtils.getInstance().getSharePreferenceBoolean(SpKey.IS_ADD_USER, true);
        if (f) {
            mViewModel.addUser("zhuzhenkui", "123456");
            SharedPreferenceUtils.getInstance().savePreference(SpKey.IS_ADD_USER, false);
        }
        mViewModel.loginData.observe(this, this::resLoginData);
    }

    private void resLoginData(UserEntity userEntity) {
        //存个临时token，如果要退出登录 调用clearToken就好
        TokenManager.getInstance().saveToken("123");
        //存到sp
        SharedPreferenceUtils.getInstance().savePreference(SpKey.USER_ID,userEntity.id);
        SharedPreferenceUtils.getInstance().savePreference(SpKey.USER_NAME,userEntity.username);
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }


    @Nullable
    @Override
    protected LoginViewModel getViewModel() {
        return new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    public void onLoginClick(View view) {
        mViewModel.login(getBinding().userNameEt.getText().toString().trim(), getBinding().pwdEt.getText().toString().trim());
    }

    @Override
    protected boolean isCanGoBack() {
        return false;
    }

    @Override
    protected int toolbarTitleRes() {
        return R.string.login_page;
    }

    public void onRegisterClick(View view) {
        goToRegister();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == RESULT_OK) {
            if (data != null) {
                String account = data.getStringExtra("ACCOUNT");
                String password = data.getStringExtra("PASSWORD");
                // 填充到输入框
                getBinding().userNameEt.setText(account);
                getBinding().pwdEt.setText(password);
            }
        }
    }

    // 在跳转到注册页面前记得设置请求码
    private static final int REQUEST_CODE_REGISTER = 1;
    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }
}