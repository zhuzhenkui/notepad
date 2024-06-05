package com.zhuzhenkui.notepad.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final int THREAD_COUNT = 4; // 根据需要调整线程数
    private static final ExecutorService diskIO = Executors.newFixedThreadPool(THREAD_COUNT);
    private AppExecutors(){

    }
    public static ExecutorService getDiskIO() {
        return diskIO;
    }
}