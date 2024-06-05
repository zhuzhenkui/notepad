package com.zhuzhenkui.notepad.home.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;
import java.util.List;

public class ImageEditText extends AppCompatEditText {
    private SpannableStringBuilder spannableStringBuilder;
    private List<Drawable> drawables = new ArrayList<>();

    public ImageEditText(Context context) {
        super(context);
        init();
    }

    public ImageEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化SpannableStringBuilder用于存储文本和图片
        spannableStringBuilder = new SpannableStringBuilder();
        setText(spannableStringBuilder);
//        setKeyListener(null); // 禁止文字输入
        setFocusableInTouchMode(true); // 保持可聚焦，以便弹出软键盘
    }

    // 插入图片到光标位置的方法，这里简化处理，实际应用中需要更复杂的逻辑处理图片大小、位置等
    public void insertImage(Bitmap bitmap) {
                ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
        int startIndex = getSelectionStart(); // 获取光标起始位置
        int endIndex = getSelectionEnd(); // 获取光标结束位置

        SpannableString spannableString = new SpannableString(" "); // 用空格或其他占位符
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        Editable editable = getText();
        editable.replace(startIndex, endIndex, spannableString);
    }

    // 重写此方法处理退格键事件
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        Log.d("asdasdas", "onTextChanged: ");
        // 当长度减少，表示用户可能按了退格键
        if (lengthAfter < lengthBefore && start > 0) {
            char beforeChar = getText().charAt(start - 1);
            if (beforeChar == '\uFFFC') { // 如果退格键删除的是占位符，移除对应的图片
                int drawableIndex = drawables.size() - 1; // 假设最后一个插入的图片是最可能被删除的
                if (drawableIndex >= 0) {
                    drawables.remove(drawableIndex);
                    // 这里需要更精确地处理Span的删除，由于逻辑复杂，实际应用中需仔细调整
                    // 可以考虑使用getSpans方法获取ImageSpan并移除
                }
            }
        }
    }
}
