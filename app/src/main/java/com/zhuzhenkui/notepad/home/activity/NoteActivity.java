package com.zhuzhenkui.notepad.home.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.shengdan.base_lib.base.BaseVMActivity;
import com.shengdan.base_lib.utils.ToastUtil;
import com.zhuzhenkui.notepad.R;
import com.zhuzhenkui.notepad.databinding.ActivityNoteBinding;
import com.zhuzhenkui.notepad.home.utils.AudioRecorder;
import com.zhuzhenkui.notepad.home.utils.PictureUtils;
import com.zhuzhenkui.notepad.home.vm.NotePadViewModel;

import java.io.File;
import java.io.IOException;

public class NoteActivity extends BaseVMActivity<NotePadViewModel, ActivityNoteBinding> {

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getBinding().setVm(mViewModel);
        mViewModel.saveNoteData.observe(this,this::resSaveData);
    }

    private void resSaveData(Boolean aBoolean) {
        finish();
    }

    @Override
    protected void getIntentData(@Nullable Intent intent) {
        super.getIntentData(intent);
        int noteId = intent.getIntExtra("noteId", -1);
        String noteTitle = intent.getStringExtra("noteTitle");

        Log.d(TAG, "getIntentData: " + noteId+" "+noteTitle);
        mViewModel.setNoteId(noteId);
        mViewModel.setNoteTitle(noteTitle);

    }

    @Nullable
    @Override
    protected NotePadViewModel getViewModel() {
        return new ViewModelProvider(this).get(NotePadViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_note;
    }

    @Override
    protected boolean isCanGoBack() {
        return true;
    }

    @Override
    protected int toolbarTitleRes() {
        return R.string.app_name;
    }

    @Nullable
    @Override
    public View getToolRight() {
        TextView tv = new TextView(this);
        tv.setHeight(60);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        tv.setText("保存");
        tv.setOnClickListener(v -> {
                mViewModel.toSave();
        });
        return tv;
    }

    @Override
    public int getTitleGravity() {
        return Gravity.LEFT;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            ////录音倒计时结束 ,移除msg
            if (--nowTime != 0) handler.sendEmptyMessageDelayed(1, 1000);
            mViewModel.setDownCount(nowTime);
            if (nowTime == 0) {
                //录音倒计时结束 ,关闭录音，复原当前秒数
                if (audioRecorder != null) audioRecorder.stopRecording();
                mViewModel.setDownCount(-1);
                nowTime = maxTimeLen;
            }

            return false;
        }
    });

    AudioRecorder audioRecorder;
    final int maxTimeLen = 60;
    int nowTime = maxTimeLen;

    public void onAudioRecordClick(View view) {
        if (handler.hasMessages(1)) {
            //当前正在录音，所以要关闭
            audioRecorder.stopRecording();
            mViewModel.setDownCount(-1);
            handler.removeMessages(1);
            nowTime = maxTimeLen;
            addAudio(audioRecorder.getAudioPath());
        } else {
            //开启录音
            audioRecorder = new AudioRecorder(this, mViewModel.getNoteId() + "_" + System.currentTimeMillis()+".3gp");
            audioRecorder.startRecording();
            handler.sendEmptyMessageDelayed(1, 1);
        }
    }

    public void onPlayClick(View view) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getExternalFilesDir("/audio/").getPath() + File.separator + "myRecording.3gp"); // 设置录音文件路径
            mediaPlayer.prepare(); // 准备播放
            mediaPlayer.start(); // 开始播放
        } catch (IOException e) {
            e.printStackTrace();
            // 处理错误
        }
    }

    File photoFile = null;

    public void onStartCameraClick(View view) {
        photoFile = PictureUtils.dispatchTakePictureIntent(this, mViewModel.getNoteId() + "_" + System.currentTimeMillis() + ".jpg");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == PictureUtils.CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // 拍照成功，显示照片
                Log.d(TAG, "onActivityResult: success " + photoFile.getAbsolutePath());
                mViewModel.addImageNote(photoFile.getAbsolutePath());
            } else {
                ToastUtil.showNormal("拍照取消");
            }
        }
        if (requestCode == PictureUtils.PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                Log.d(TAG, "onActivityResult: " + getPathFromUri(this,selectedImageUri));
                mViewModel.addImageNote(getPathFromUri(this,selectedImageUri));

            } else {
                ToastUtil.showNormal("相册取消");
            }
        }
    }

    public String getPathFromUri(Context context, Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return path;
    }

    void addAudio(String audioPath){
        mViewModel.addAudioNote(audioPath);
    }
    public void onGalleryClick(View view) {
//        chooseImageFromGalleryUsingActivityResultApi();
        PictureUtils.chooseImageFromGallery(this);
    }
}