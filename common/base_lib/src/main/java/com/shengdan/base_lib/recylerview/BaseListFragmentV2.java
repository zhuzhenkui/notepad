package com.shengdan.base_lib.recylerview;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shengdan.base_lib.R;
import com.shengdan.base_lib.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * author : ChenShengDan
 * date   : 2021/10/19
 * desc   :
 */
public abstract class BaseListFragmentV2<T, VM extends BaseViewModel> extends Fragment {
    protected RecyclerView recyclerview;
    protected BaseQuickAdapter<T, BaseViewHolder> adapter;
    protected VM mViewModel;
    protected String TAG;
    protected View headerView, footerView, emptyView;
    protected int currentPage = 1;
    protected SmartRefreshLayout mPtrLayout;
    private boolean isRefreshViewEnable = true;
    protected List<T> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list_v2, container, false);
        TAG = this.getClass().getSimpleName();
        mViewModel = getViewModel();
        initView(view);
        initObserver();
        return view;
    }

    protected void setListBg(int color) {
        if (recyclerview != null) recyclerview.setBackgroundColor(color);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startFirstFetch();
    }

    private void setListener() {

        adapter.setLoadMoreView(isShowLoadMoreView()?new SimpleLoadMoreView():new EmptyLoadView());
        adapter.setOnLoadMoreListener(() -> {
            currentPage++;
            fetchDataByPage(currentPage);
        }, recyclerview);

//        failedTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                failedTv.setVisibility(View.GONE);
//                mPtrLayout.setVisibility(View.VISIBLE);
//                startFirstFetch();
//            }
//        });

//        setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                startFirstFetch();
//            }
//        });
        mPtrLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                startFirstFetch();
            }
        });
//
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recyclerView.scrollToPosition(0);
//            }
//        });
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //processTopIcon();
//            }
//        });
    }

    protected boolean isShowLoadMoreView() {
        return true;
    }

    private void initView(View view) {
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(createLayoutManager());
        recyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mPtrLayout = (SmartRefreshLayout) view.findViewById(R.id.recyclerview_ptr);
        mPtrLayout.setEnableOverScrollBounce(false);
        mPtrLayout.setEnableRefresh(isRefreshViewEnable());
        data = new ArrayList<>();
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

            setListener();

        }
    }

    /**
     * 首次加载或刷新时调用
     */
    protected void startFirstFetch() {
        currentPage = 1;
        fetchDataByPage(currentPage);
    }

    /**
     * 初始化观察者
     */
    protected abstract void initObserver();

    /**
     * 获取ViewModel
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
     * 构建footer布局
     *
     * @return
     */
    protected abstract View createFooterView();


    /**
     * 构建header布局
     *
     * @return
     */
    protected abstract View createHeaderView();


    /**
     * 构建空布局
     *
     * @return
     */
    protected abstract View createEmptyView();

    /**
     * 构建适配器
     *
     * @return
     */
    protected abstract BaseQuickAdapter<T, BaseViewHolder> createQuickAdapter();

    /**
     * 构建布局管理器
     *
     * @return
     */
    protected abstract RecyclerView.LayoutManager createLayoutManager();

    /**
     * 每页数据
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
        if (isRefreshViewEnable()) {
            mPtrLayout.finishRefresh();
        }
//            mPtrLayout.setRefreshing(false);

        if (newData == null || newData.size() == 0) {
            if (emptyView != null)adapter.setEmptyView(emptyView);
            adapter.setNewData(newData);
        } else if (newData.size() < pageNum()) {
            adapter.setNewData(newData);
            adapter.loadMoreEnd();
            mPtrLayout.finishLoadMoreWithNoMoreData();
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
    protected void firstFetchComlpete(List<T> newData, Integer pageCount) {
        adapter.removeAllHeaderView();
        if (isRefreshViewEnable()) {
            mPtrLayout.finishRefresh();
        }
//            mPtrLayout.setRefreshing(false);

        if (newData == null || newData.size() == 0) {
            if (emptyView != null)adapter.setEmptyView(emptyView);
            adapter.setNewData(newData);
        } else if (newData.size() < pageNum()) {
            if (currentPage < pageCount) {
                adapter.setNewData(newData);
            } else {
                adapter.setNewData(newData);
                adapter.loadMoreEnd();
            }

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
        if (isRefreshViewEnable()) {
            adapter.removeAllHeaderView();
        }

        if (isRefreshViewEnable) {
            mPtrLayout.finishRefresh();
        }
//            mPtrLayout.setRefreshing(false);

        if (newData == null || newData.size() == 0) {
            if (emptyView != null)adapter.setEmptyView(emptyView);
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
    protected void firstFetchComlpete(List<T> newData, boolean isRemoveAllHeaderView, boolean isHideHeader) {
        if (isRemoveAllHeaderView) {
            adapter.removeAllHeaderView();
        }

        if (isRefreshViewEnable()) {
            mPtrLayout.finishRefresh();
        }
//            mPtrLayout.setRefreshing(false);

        if (newData == null || newData.size() == 0) {
            if (isHideHeader) {
                if (emptyView != null)adapter.setEmptyView(emptyView);
            } else {
                if (emptyView != null) adapter.setEmptyView(emptyView);
                adapter.setHeaderAndEmpty(true);
            }
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

    protected boolean isRefreshViewEnable() {
        return isRefreshViewEnable;
    }
}
