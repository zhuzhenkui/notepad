package com.zhuzhenkui.notepad.home.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.shengdan.base_lib.base.BaseVMActivity;
import com.zhuzhenkui.notepad.home.vm.SearchViewModel;
import com.zhuzhenkui.notepad.R;
import com.zhuzhenkui.notepad.databinding.ActivityMainBinding;
import com.zhuzhenkui.notepad.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseVMActivity<SearchViewModel, ActivitySearchBinding> {

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        getBinding().searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged() called with: s = [" + s + "], start = [" + start + "], count = [" + count + "], after = [" + after + "]");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "searchNotesByTitle() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
                mViewModel.searchNotesByTitle(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged() called with: s = [" + s + "]");
            }
        });
        getBinding().searchEt.requestFocus();

    }

    @Nullable
    @Override
    protected SearchViewModel getViewModel() {
        return new ViewModelProvider(this).get(SearchViewModel.class);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected boolean isCanGoBack() {
        return true;
    }

    @Override
    protected int toolbarTitleRes() {
        return R.string.search_page;
    }
}