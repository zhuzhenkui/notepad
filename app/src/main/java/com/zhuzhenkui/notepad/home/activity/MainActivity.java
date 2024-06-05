package com.zhuzhenkui.notepad.home.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shengdan.base_lib.base.BaseVMActivity;
import com.shengdan.base_lib.utils.SharedPreferenceUtils;
import com.zhuzhenkui.notepad.home.vm.MainViewModel;
import com.zhuzhenkui.notepad.utils.SpKey;
import com.zhuzhenkui.notepad.R;
import com.zhuzhenkui.notepad.databinding.ActivityMainBinding;

public class MainActivity extends BaseVMActivity<MainViewModel,ActivityMainBinding > {

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "init: "+ SharedPreferenceUtils.getInstance().getSharePreferenceInt(SpKey.USER_ID));

    }

    @Nullable
    @Override
    protected MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isCanGoBack() {
        return false;
    }

    @Override
    protected int toolbarTitleRes() {
        return R.string.memorandum_page;
    }
    @Nullable
    @Override
    public View getToolRight() {
        TextView tv = new TextView(this);
        tv.setHeight(60);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        tv.setText("新增");
        tv.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            //新增的id默认为-1
            intent.putExtra("noteId",-1);
            startActivity(intent);
        });
        return tv;
    }

    public void onSearchClick(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }
}