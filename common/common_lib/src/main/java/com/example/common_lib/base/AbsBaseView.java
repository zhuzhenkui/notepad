package com.example.common_lib.base;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author : ChenShengDan
 * date   : 2023/2/24
 * desc   :
 */
public abstract class AbsBaseView extends FrameLayout {
    protected String TAG = getClass().getSimpleName();
    public AbsBaseView(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(getLayout(), this, true);
        if (view != null) initView(view);
    }

    public AbsBaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(getLayout(), this, true);
        if (view != null) initView(view);
    }

    public AbsBaseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(getLayout(), this, true);
        if (view != null) initView(view);
    }


    protected abstract void initView(View view);

    protected abstract int getLayout();

    protected int dp2px(float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getContext().getResources().getDisplayMetrics()) + 0.5f);
    }
}
