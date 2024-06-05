package com.zhuzhenkui.notepad.home.utils;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioPlayer {

    private static volatile AudioPlayer instance;
    private MediaPlayer mediaPlayer;

    // 私有构造方法，确保外界无法直接实例化
    private AudioPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    // 获取单例实例
    public static AudioPlayer getInstance() {
        if (instance == null) {
            synchronized (AudioPlayer.class) {
                if (instance == null) {
                    instance = new AudioPlayer();
                }
            }
        }
        return instance;
    }

    // 根据路径播放音频
    public void playAudio(String path) {
        try {
            // 如果MediaPlayer当前正在播放，先停止并释放资源
            if (mediaPlayer!=null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            mediaPlayer = new MediaPlayer();

            // 设置音频源
            mediaPlayer.setDataSource(path);
            // 准备播放
            mediaPlayer.prepare();

            // 开始播放
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常，比如文件找不到等情况
        }
    }
    public long getAudioDurationWithMediaPlayer(String audioPath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            return mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // 返回-1表示获取时长失败
        } finally {
            mediaPlayer.release();
        }
    }

    // 其他控制方法，如暂停、继续播放等...
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // 当不再需要MediaPlayer时，释放资源
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}