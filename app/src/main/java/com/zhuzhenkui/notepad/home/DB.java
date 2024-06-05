package com.zhuzhenkui.notepad.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.zhuzhenkui.notepad.home.utils.AudioPlayer;
import com.zhuzhenkui.notepad.home.utils.PictureUtils;

public class DB {
    @BindingAdapter({"imageUrl"})
    public static void setImg(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) return;
        int fixWidth = imageView.getLayoutParams().width;
        int[] sizeArr = PictureUtils.getImageWidthHeight(url);
        float rate = (float) sizeArr[1] / sizeArr[0];
        imageView.getLayoutParams().height = (int) (rate * fixWidth);
//        Log.d("asdasd", "setImg: "+fixWidth+"  "+Arrays.toString(sizeArr)+"  "+rate+"  "+imageView.getLayoutParams().height);
        Uri uri = Uri.parse(url);
        imageView.setImageURI(uri);
    }

    @BindingAdapter({"imageUrl"})
    public static void setImg(EditText editText, String url) {
        if (TextUtils.isEmpty(url)) return;
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(url, options);
        int fixWidth = editText.getLayoutParams().width;
        int[] sizeArr = PictureUtils.getImageWidthHeight(url);
        float rate = (float) sizeArr[1] / sizeArr[0];
        int height = (int) (rate * fixWidth);
        bitmap = resizeImage(bitmap, fixWidth, height);

        ImageSpan imageSpan = new ImageSpan(editText.getContext(), bitmap);
        int startIndex = editText.getSelectionStart(); // 获取光标起始位置
        int endIndex = editText.getSelectionEnd(); // 获取光标结束位置

        SpannableString spannableString = new SpannableString(" "); // 用空格或其他占位符
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        Editable editable = editText.getText();
        editable.replace(startIndex, endIndex, spannableString);

        InputFilter filter = (source, start, end, dest, dstart, dend) -> "";
        editText.setFilters(new InputFilter[]{filter});

    }

    //图片缩放
    public static Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //计算宽、高缩放率
        float scanleWidth = (float) newWidth / width;
        float scanleHeight = (float) newHeight / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scanleWidth, scanleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    @BindingAdapter({"position"})
    public static void setImg(EditText editText, int position) {
        if (position == 0) {
            editText.setTextSize(26f);
            editText.setHint("请输入标题");
            editText.setTextColor(Color.BLACK);
        } else {
            editText.setTextSize(20f);
            editText.setHint("请输入内容");
        }
        editText.post(() -> editText.setSelection(editText.getText().length()));
    }

    @BindingAdapter({"voiceUrl"})
    public static void startVoice(TextView editText, String voiceUrl) {
        editText.setText(AudioPlayer.getInstance().getAudioDurationWithMediaPlayer(voiceUrl) / 1000 + "s");
        editText.setOnClickListener(v -> {
            AudioPlayer.getInstance().pause();
            AudioPlayer.getInstance().playAudio(voiceUrl);
        });
    }

    @BindingAdapter({"filters"})
    public static void startVoice(EditText editText, String filters) {
        InputFilter filter = (source, start, end, dest, dstart, dend) -> "";
        editText.setFilters(new InputFilter[]{filter});
    }
}
