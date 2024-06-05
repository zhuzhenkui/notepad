package com.zhuzhenkui.notepad.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common_lib.base.AbsBaseView;
import com.zhuzhenkui.notepad.R;

public class SearchView extends AbsBaseView {
    public SearchView(@NonNull Context context) {
        super(context);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getLayout() {
        return R.layout.view_search;
    }
}
