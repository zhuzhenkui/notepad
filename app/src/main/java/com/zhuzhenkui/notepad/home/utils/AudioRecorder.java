package com.zhuzhenkui.notepad.home.utils;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class AudioRecorder {
    private static final String TAG = "AudioRecorder";
    private MediaRecorder recorder = null;
    private String outputFile = null;

    public AudioRecorder(Context context, String fileName) {
        outputFile = context.getExternalFilesDir("/audio/").getPath() + File.separator + fileName;
        prepareRecorder();
    }
    public String getAudioPath(){
        return outputFile;
    }
    private void prepareRecorder() {
        if (recorder == null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(outputFile);

            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "prepare() failed");
            }
        }
    }

    public void startRecording() {
        recorder.start();
    }

    public void stopRecording() {
        if (null != recorder) {
            recorder.stop();
            recorder.release();
            recorder = null;

        }
    }
}