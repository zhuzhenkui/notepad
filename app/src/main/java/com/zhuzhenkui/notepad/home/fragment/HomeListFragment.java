package com.zhuzhenkui.notepad.home.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.zhuzhenkui.notepad.home.entity.NoteEntity;
import com.zhuzhenkui.notepad.home.view.TimeSortView;
import com.zhuzhenkui.notepad.home.vm.MainViewModel;
import com.zhuzhenkui.notepad.BR;
import com.zhuzhenkui.notepad.R;

import java.util.List;

public class HomeListFragment extends BaseListFragmentV2<NoteEntity, MainViewModel> {
    private int orderType = TimeSortView.UPDATE_TIME_DESC;
    boolean isLoad = false;

    @Override
    public void onStart() {
        super.onStart();
        //首次不加载
       if (isLoad)fetchDataByPage(1);
    }

    @Override
    protected void initObserver() {
        mViewModel.noteListData.observe(getViewLifecycleOwner(), this::resNoteListData);
        mViewModel.delEventData.observe(getViewLifecycleOwner(), this::resDelEvent);

    }

    private void resDelEvent(Integer integer) {
        fetchDataByPage(1);
    }

    private void resNoteListData(List<NoteEntity> noteListEntities) {
        firstFetchComlpete(noteListEntities);
        Log.d(TAG, "resNoteListData: "+noteListEntities.size());
    }

    @Override
    protected MainViewModel getViewModel() {
        return new ViewModelProvider(getActivity()).get(MainViewModel.class);
    }


    @Override
    protected void fetchDataByPage(int page) {
        switch (orderType){
            case TimeSortView.CREATE_TIME_ASC :
                mViewModel.getNoteAllByCrateTimeAsc();
                break;
            case TimeSortView.CREATE_TIME_DESC:
                mViewModel.getNoteAllByCrateTimeDesc();
                break;
            case TimeSortView.UPDATE_TIME_ASC:
                mViewModel.getNoteAllByUpdateTimeAsc();
                break;
            case TimeSortView.UPDATE_TIME_DESC:
                mViewModel.getNoteAllByUpdateTimeDesc();
                break;
            }
        isLoad = true;

    }


    @Override
    protected View createFooterView() {
        return null;
    }

    @Override
    protected View createHeaderView() {
        TimeSortView sortView = new TimeSortView(getContext());
        sortView.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,90);
        layoutParams.setMargins(0,0,0,20);
        sortView.setLayoutParams(layoutParams);
        sortView.setOrder(orderType);
        sortView.setOnItemClickListener(type -> {
            HomeListFragment.this.orderType = type;
            fetchDataByPage(1);
        });
        return sortView;
    }

    @Override
    protected View createEmptyView() {
        TextView tv = new TextView(getContext());
        tv.setHeight(60);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        tv.setText("当前无笔记...");
        return tv;
    }

    @Override
    protected BaseQuickAdapter<NoteEntity, BaseViewHolder> createQuickAdapter() {
        return new BaseQuickAdapter<NoteEntity, BaseViewHolder>(R.layout.item_note_list) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, NoteEntity item) {
                ((ViewHolder) helper).getBinding().setVariable(BR.item, item);
                ((ViewHolder) helper).getBinding().getRoot().setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), NoteActivity.class);
                    intent.putExtra("noteId",item.getId());
                    intent.putExtra("noteTitle",item.getTitle());
                    startActivity(intent);
                });
                ((ViewHolder) helper).getBinding().getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("确定要删除这条记录吗?")
                                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 用户点击了“删除”，执行删除操作
                                        mViewModel.deleteById(item.getId());
                                    }
                                })
                                .setNegativeButton("取消", null) // 用户点击取消，对话框关闭
                                .show();
                        return false;
                    }
                });

            }
            @Override
            protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
                ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_note_list, parent, false);
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
