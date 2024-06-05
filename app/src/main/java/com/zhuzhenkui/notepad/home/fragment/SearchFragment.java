package com.zhuzhenkui.notepad.home.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shengdan.base_lib.recylerview.BaseListFragmentV2;
import com.shengdan.base_lib.recylerview.ViewHolder;
import com.zhuzhenkui.notepad.home.activity.NoteActivity;
import com.zhuzhenkui.notepad.home.entity.SearchEntity;
import com.zhuzhenkui.notepad.home.vm.SearchViewModel;
import com.zhuzhenkui.notepad.BR;
import com.zhuzhenkui.notepad.R;

import java.util.List;

public class SearchFragment extends BaseListFragmentV2<SearchEntity, SearchViewModel> {
    @Override
    protected void initObserver() {
        mViewModel.noteListData.observe(getViewLifecycleOwner(), this::resNoteListData);
    }

    private void resNoteListData(List<SearchEntity> noteListEntities) {
        firstFetchComlpete(noteListEntities);
    }

    @Override
    protected SearchViewModel getViewModel() {
        return new ViewModelProvider(getActivity()).get(SearchViewModel.class);
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
        TextView tv = new TextView(getContext());
        tv.setHeight(60);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        tv.setText("搜索无结果...");
        return tv;
    }

    @Override
    protected BaseQuickAdapter<SearchEntity, BaseViewHolder> createQuickAdapter() {
        return new BaseQuickAdapter<SearchEntity, BaseViewHolder>(R.layout.item_search_list) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, SearchEntity item) {
                ((ViewHolder) helper).getBinding().setVariable(BR.item, item);
                ((ViewHolder) helper).getBinding().getRoot().setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), NoteActivity.class);
                    intent.putExtra("noteId",item.getId());
                    intent.putExtra("noteTitle",item.getTitle());
                    startActivity(intent);
                });

            }
            @Override
            protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
                ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search_list, parent, false);
                return new ViewHolder(binding);
            }
        };
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {

        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);

    }

    @Override
    protected int pageNum() {
        //一次性全加载完
        return Integer.MAX_VALUE;
    }
}
