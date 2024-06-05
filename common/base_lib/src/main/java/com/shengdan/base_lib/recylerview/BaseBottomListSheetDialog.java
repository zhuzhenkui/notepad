package com.shengdan.base_lib.recylerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shengdan.base_lib.R;
import com.shengdan.base_lib.base.BaseViewModel;

import java.util.List;

/**
 * author : ChenShengDan
 * date   : 2021/10/21
 * desc   :
 */
public abstract class BaseBottomListSheetDialog<T, VM extends BaseViewModel> extends BottomSheetDialogFragment {
    protected VM mViewModel;
    protected String TAG = this.getClass().getSimpleName();;
    protected RecyclerView recyclerview;
    protected BaseQuickAdapter<T, BaseViewHolder> adapter;
    protected int currentPage = 1;
    protected View headerView, footerView, emptyView;
    protected View view;
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
////        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//        if (getContext() == null)super.onCreateDialog(savedInstanceState);
//        return new ProxyBottomSheetDialog(getContext(),getTheme());
//    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_list, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = getViewModel();
        if (mViewModel != null) {
            initObserver();
            initView(view);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchDataByPage(currentPage);
    }

    protected abstract void initObserver();

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //圆角
        setStyle(STYLE_NORMAL, R.style.AppBottomSheet);
    }

    private void initView(View view) {
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(createLayoutManager());
        recyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = createQuickAdapter();
        if (adapter != null) {
            recyclerview.setAdapter(adapter);
            adapter.setHeaderAndEmpty(true);
            emptyView = createEmptyView();
            headerView = createHeaderView();
            footerView = createFooterView();

            if (emptyView != null) adapter.setEmptyView(emptyView);
            if (headerView != null) adapter.setHeaderView(headerView);
            if (footerView != null) adapter.setFooterView(footerView);
        }
    }

    /**
     * 创建适配器
     *
     * @return
     */
    protected abstract BaseQuickAdapter<T, BaseViewHolder> createQuickAdapter();

    /**
     * 创建布局管理器
     *
     * @return
     */
    protected abstract LinearLayoutManager createLayoutManager();

    /**
     * 创建head视图
     *
     * @return
     */
    protected abstract View createHeaderView();

    /**
     * 创建footer视图
     *
     * @return
     */
    protected abstract View createFooterView();

    /**
     * 空 创建视图
     *
     * @return
     */
    protected abstract View createEmptyView();

    /**
     * 获取Viemodel
     *
     * @return
     */
    protected abstract VM getViewModel();


    /**
     * 根据page从数据源拿数据
     *
     * @param page
     */
    protected abstract void fetchDataByPage(int page);

    /**
     * 每页的数据个数
     *
     * @return
     */
    protected abstract int pageNum();

    /**
     * 1、成功，数据size为0  显示empty view
     * 2、成功，数据size小于pageNum  loadingView 显示为end状态，没有更多
     * 3、成功，数据size等于pageNum  loadingView 显示为默认
     *
     * @param newData
     */
    protected void firstFetchComlpete(List<T> newData) {
//        adapter.removeAllHeaderView();
//        if (isRefreshViewEnable) {
//            mPtrLayout.finishRefresh();
//        }
//            mPtrLayout.setRefreshing(false);
        if (newData == null || newData.size() == 0) {
            if (emptyView != null) adapter.setEmptyView(emptyView);
            adapter.setNewData(newData);
        } else if (newData.size() < pageNum()) {
            adapter.setNewData(newData);
            adapter.loadMoreEnd();
        } else if (newData.size() == pageNum()) {
            adapter.setNewData(newData);
        }
    }

    /**
     * 1、成功，数据size为0  显示empty view
     * 2、成功，数据size小于pageNum  loadingView 显示为end状态，没有更多
     * 3、成功，数据size等于pageNum  loadingView 显示为默认
     *
     * @param newData
     */
    protected void firstFetchComlpete(List<T> newData, boolean isRemoveAllHeaderView) {
//        if (isRemoveAllHeaderView) {
//            adapter.removeAllHeaderView();
//        }


//            mPtrLayout.setRefreshing(false);

        if (newData == null || newData.size() == 0) {
            if (emptyView != null) adapter.setEmptyView(emptyView);
            adapter.setNewData(newData);
        } else if (newData.size() < pageNum()) {
            adapter.setNewData(newData);
            adapter.loadMoreEnd();
        } else if (newData.size() == pageNum()) {
            adapter.setNewData(newData);
        }
    }


    /**
     * 1、成功，数据size为0  loadingView 为没有更多。
     * 2、成功，数据size小于pageNum  loadingView 显示为end状态，没有更多
     * 3、成功，数据size等于pageNum  loadingView 显示为默认
     *
     * @param newData
     */
    protected void loadMoreComplete(List<T> newData) {
        if (newData.size() == 0) {
            adapter.loadMoreEnd();
        } else if (newData.size() < pageNum()) {
            adapter.addData(newData);
            adapter.loadMoreEnd();
        } else if (newData.size() == pageNum()) {
            adapter.addData(newData);
            adapter.loadMoreComplete();
        }
    }

    /**
     * 1、成功，数据size为0  loadingView 为没有更多。
     * 2、成功，数据size小于pageNum  loadingView 显示为end状态，没有更多
     * 3、成功，数据size等于pageNum  loadingView 显示为默认
     *
     * @param newData
     */
    protected void loadMoreComplete(List<T> newData, int pageCount) {
        if (newData.size() == 0) {
            adapter.loadMoreEnd();
        } else if (newData.size() < pageNum()) {
            if (currentPage < pageCount) {
                adapter.addData(newData);
                adapter.loadMoreComplete();
            } else {
                adapter.addData(newData);
                adapter.loadMoreEnd();
            }
        } else if (newData.size() == pageNum() && currentPage == pageCount) {
            adapter.addData(newData);
            adapter.loadMoreComplete();
            adapter.loadMoreEnd();
        } else if (newData.size() == pageNum()) {
            adapter.addData(newData);
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
