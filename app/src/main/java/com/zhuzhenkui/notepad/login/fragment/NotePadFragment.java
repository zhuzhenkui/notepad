package com.zhuzhenkui.notepad.login.fragment;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shengdan.base_lib.recylerview.BaseListFragmentV2;
import com.zhuzhenkui.notepad.home.entity.NoteContentEntity;
import com.zhuzhenkui.notepad.home.vm.MainViewModel;

public class NotePadFragment extends BaseListFragmentV2<NoteContentEntity, MainViewModel> {
    @Override
    protected void initObserver() {

    }

    @Override
    protected MainViewModel getViewModel() {
        return null;
    }

    @Override
    protected void fetchDataByPage(int page) {

    }

    @Override
    protected View createFooterView() {
        return null;
    }

    @Override
    protected View createHeaderView() {
        return null;
    }

    @Override
    protected View createEmptyView() {
        return null;
    }

    @Override
    protected BaseQuickAdapter<NoteContentEntity, BaseViewHolder> createQuickAdapter() {
        return null;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return null;
    }

    @Override
    protected int pageNum() {
        return 0;
    }
}
