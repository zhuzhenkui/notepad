package com.shengdan.base_lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.shengdan.base_lib.R;
import com.shengdan.base_lib.base.BaseApplication;

import java.util.concurrent.TimeUnit;


public class ToastUtil {
    /**
     * 快速点击时间差
     */
    private static final long FAST_CLICK_TIME = 1200;
    /**
     * 这样可以防止Toast显示多个的问题
     */

    private static Toast toast;

    /**
     * 展示自定义吐司
     *
     * @param message
     */
    public static void showNormal(final String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            toastProcess(BaseApplication.getInstance().getApplicationContext(), message);
        }
    }


    /**
     * 自定义toast
     *
     * @param context  上下文对象
     * @param messages toast内容
     */
    @SuppressLint("WrongConstant")
    private static void toastProcess(Context context, String messages) {
        try {
            if (!TextUtils.isEmpty(messages)) {

                if (toast == null) {
                    toast = new Toast(context.getApplicationContext());
                } else {
                    toast.cancel();
                    toast = new Toast(context.getApplicationContext());
                }
                //setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
                toast.setGravity(Gravity.CENTER, 0, 0);
                //setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
                toast.setDuration(Toast.LENGTH_SHORT);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.toast, null);
                TextView text = view.findViewById(R.id.message);
                LinearLayout linearLayout = view.findViewById(R.id.root);
                linearLayout.getBackground().setAlpha(200);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    text.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);
                }
                //toast内容
                text.setText(messages);
                //添加视图文件
                toast.setView(view);
                toast.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showNormal(final String message, final long duration) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            show(message, duration);
        }
    }


    private static void show(String message, long duration) {
        Context context = BaseApplication.getInstance().getApplicationContext();
        //if (toast == null) {
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        //}
        View view = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
        view.setBackgroundResource(R.drawable.bg_toast_black);
        TextView tvToast = view.findViewById(R.id.tv_toast);
        tvToast.setText(message);

        toast.setView(view);

        //toast.show();
    }


    private static class ToastThread implements Runnable {
        private long duration;
        private boolean needCancel = false;


        public ToastThread(long duration) {
            this.duration = duration;
        }


        @Override
        public void run() {
            if (duration == 0) {
                toast.cancel();
            } else {
                long detectionDuration = 3_500;
                if (detectionDuration <= duration) {
                    duration -= detectionDuration;

                    toast.show();

                    // 检测间隔，因为使用了一个toast对象且默认显示时长是Toast.LENGTH_LONG，因此需要等到toast本次显示完成之后(比Toast.LENGTH_LONG时间略长的时间)再次show
                } else {
                    if (needCancel) {
                        toast.cancel();
                    } else {
                        needCancel = true;

                        toast.show();

                    }
                }
            }
        }
    }
}
