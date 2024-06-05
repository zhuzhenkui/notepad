package com.shengdan.ui_lib.title;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.shengdan.ui_lib.R;


/**
 * description：Toolbar 。
 *
 * @date 2019-11-18
 */
public class CenterTitleToolbar extends Toolbar {
    private TextView mTitleTextView;

    private CharSequence mTitleText;

    private int mTitleTextColor;
    private int mTitleTextAppearance;
    private int titleGravity = Gravity.CENTER;

    public CenterTitleToolbar(Context context) {
        super(context);
        resolveAttribute(context, null, androidx.appcompat.R.attr.toolbarStyle);
    }

    public CenterTitleToolbar(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        resolveAttribute(context, attrs, androidx.appcompat.R.attr.toolbarStyle);
    }

    public CenterTitleToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttribute(context, attrs, defStyleAttr);
    }

    @SuppressLint("RestrictedApi")
    private void resolveAttribute(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        // Need to use getContext() here so that we use the themed context
        context = getContext();
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                androidx.appcompat.R.styleable.Toolbar, defStyleAttr, 0);
        try {
            // Toolbar中先获取titleTextAppearance，接着是title，最后是titleTextColor
            // 字体颜色优先级：mTitleTextColor > mTitleTextAppearance中的字体颜色
            // 考虑到有可能会Toolbar本身会带Title，mTitleTextView有可能不能正确显示样式
            // 所以这里要再设置mTitleTextAppearance和mTitleTextColor
            @SuppressLint("RestrictedApi") final int titleTextAppearance = a.getResourceId(androidx.appcompat.R.styleable.Toolbar_titleTextAppearance, 0);
            if (titleTextAppearance != 0) {
                setTitleTextAppearance(context, titleTextAppearance);
            }
            if (a.hasValue(androidx.appcompat.R.styleable.Toolbar_titleTextColor)) {
                setTitleTextColor(a.getColor(androidx.appcompat.R.styleable.Toolbar_titleTextColor, 0xffffffff));
            }

        } finally {
            a.recycle();
        }
    }

    @Override
    public CharSequence getTitle() {
        return mTitleText;
    }


    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        super.setNavigationIcon(icon);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            // 懒加载
            if (mTitleTextView == null) {
                final Context context = getContext();
                mTitleTextView = new TextView(context);
                mTitleTextView.setSingleLine();
                mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                if (mTitleTextAppearance != 0) {
                    mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
                }
                if (mTitleTextColor != 0) {
                    mTitleTextView.setTextColor(mTitleTextColor);
                } else {
                    mTitleTextView.setTextColor(Color.parseColor("#ffffff"));
                }
            }
            if (mTitleTextView.getParent() != this) {
                if (titleGravity == Gravity.CENTER){
                    addCenterView(mTitleTextView);
                }else if (titleGravity == Gravity.LEFT){
                    addLeftView(mTitleTextView);
                }else {
                    addRightView(mTitleTextView);
                }
            }
            // 当title为空时，remove
        } else if (mTitleTextView != null && mTitleTextView.getParent() == this) {
            removeView(mTitleTextView);
        }
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
        mTitleText = title;
    }
    public void setRightIcon(View view){
        if (view == null)return;
        addRightView(view);
    }

    private void addCenterView(View v) {
        final ViewGroup.LayoutParams vlp = v.getLayoutParams();
        final LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }

        lp.gravity = Gravity.CENTER;
        addView(v, lp);
    }
    private void addLeftView(View v) {
        final ViewGroup.LayoutParams vlp = v.getLayoutParams();
        final LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }
        Log.d("TAG", "addLeftView: ");
        lp.gravity = Gravity.LEFT;
        addView(v, lp);
    }
    private void addRightView(View v) {
        final ViewGroup.LayoutParams vlp = v.getLayoutParams();
        final LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }
        lp.rightMargin = 20;
        lp.gravity = Gravity.RIGHT;
        addView(v, lp);
    }



    @Override
    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        mTitleTextAppearance = resId;
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    /**
     * 获取title TextView
     *
     * @return
     */
    public TextView getTextTitle() {
        return mTitleTextView;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean isTitleTruncated() {
        if (mTitleTextView == null) {
            return false;
        }

        final Layout titleLayout = mTitleTextView.getLayout();
        if (titleLayout == null) {
            return false;
        }

        final int lineCount = titleLayout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            if (titleLayout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 部分页面要求 完全定制View
     *
     * @param view
     */
    public void setCustomView(View view) {
        this.setTitle(null);
        this.removeAllViews();
        this.addView(view);
    }

    public void setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
    }
}
