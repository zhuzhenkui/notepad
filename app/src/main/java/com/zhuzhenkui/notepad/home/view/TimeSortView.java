package com.zhuzhenkui.notepad.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common_lib.base.AbsBaseView;
import com.zhuzhenkui.notepad.R;

public class TimeSortView extends AbsBaseView {
    CheckBox create_time_rb, update_time_rb;
    OnItemClickListener onItemClickListener;
    private int order = 4;


    //创建时间升序
    public static final int CREATE_TIME_ASC = 0;
    //创建时间降序
    public static final int CREATE_TIME_DESC = 1;
    //更新时间升序
    public static final int UPDATE_TIME_ASC = 2;
    //更新时间降序
    public static final int UPDATE_TIME_DESC = 3;


    public TimeSortView(@NonNull Context context) {
        super(context);
    }

    public TimeSortView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeSortView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(View view) {
        create_time_rb = view.findViewById(R.id.create_time_rb);
        update_time_rb = view.findViewById(R.id.update_time_rb);
        create_time_rb.setOnClickListener(v -> {
            update_time_rb.setChecked(false);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(create_time_rb.isChecked() ? CREATE_TIME_DESC : CREATE_TIME_ASC);
            }

        });
        update_time_rb.setOnClickListener(v -> {
            create_time_rb.setChecked(false);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(update_time_rb.isChecked() ? UPDATE_TIME_DESC : UPDATE_TIME_ASC);
            }
        });
      notifyOrder();
    }

    private void notifyOrder() {
        switch (order){
            case 0:
                create_time_rb.setChecked(true);
                break;
            case 1:
                create_time_rb.setChecked(false);
                break;
            case 2:
                update_time_rb.setChecked(true);
                break;
            case 3:
                update_time_rb.setChecked(false);
                break;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOrder(int order) {
        this.order = order;
        notifyOrder();
    }

    @Override
    protected int getLayout() {
        return R.layout.view_time_sort;
    }

    public interface OnItemClickListener {
        void onItemClick(int type);
    }
}
