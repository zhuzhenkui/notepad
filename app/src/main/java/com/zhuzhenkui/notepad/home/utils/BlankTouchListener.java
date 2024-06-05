package com.zhuzhenkui.notepad.home.utils;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BlankTouchListener implements View.OnTouchListener {
    final RecyclerView recyclerview;

    public BlankTouchListener(RecyclerView recyclerview) {
        this.recyclerview = recyclerview;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            View childView = recyclerview.findChildViewUnder(event.getX(), event.getY());
            if (childView == null) { // 点击的是空白区域
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerview.getLayoutManager();
                int lastPosition = layoutManager.getItemCount() - 1;
                ViewGroup lastItem = (ViewGroup) layoutManager.findViewByPosition(lastPosition);

                if (lastItem != null) {
                    View view =  lastItem.getChildAt(0);
                    if (view instanceof EditText){
                        view.requestFocus();
                        ((EditText)view).setSelection(((EditText)view).getText().length());
                        InputMethodManager imm = (InputMethodManager) recyclerview.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }
            }
        }
        return false;
    }
}
