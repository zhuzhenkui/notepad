package com.zhuzhenkui.notepad.login.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shengdan.base_lib.base.BaseVMActivity;
import com.zhuzhenkui.notepad.R;
import com.zhuzhenkui.notepad.databinding.ActivityRegisterBinding;
import com.zhuzhenkui.notepad.login.vm.RegisterViewModel;

public class RegisterActivity extends BaseVMActivity<RegisterViewModel, ActivityRegisterBinding> {


    @Override
    protected void init(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    protected RegisterViewModel getViewModel() {
        return new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected boolean isCanGoBack() {
        return false;
    }

    @Override
    protected int toolbarTitleRes() {
        return R.string.register_page;
    }

    public void onRegisterClick(View view) {
        mViewModel.addUser(getBinding().userNameEt.getText().toString().trim(), getBinding().pwdEt.getText().toString().trim());
        Intent intent = new Intent();
        intent.putExtra("ACCOUNT", getBinding().userNameEt.getText().toString().trim());
        intent.putExtra("PASSWORD", getBinding().pwdEt.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish(); // 关闭当前注册页面，返回登录页面
    }
}