package com.zhuzhenkui.notepad.splash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.common_lib.permission.PermissionUtils;
import com.shengdan.base_lib.base.BaseScreenVMActivity;
import com.shengdan.base_lib.base.BaseViewModel;
import com.shengdan.base_lib.utils.ToastUtil;
import com.shengdan.base_lib.utils.TokenManager;
import com.zhuzhenkui.notepad.R;
import com.zhuzhenkui.notepad.home.activity.MainActivity;
import com.zhuzhenkui.notepad.login.activity.LoginActivity;

import java.util.Arrays;

public class SplashActivity extends BaseScreenVMActivity {
    private static final int PERMISSION_CODE_FIRST = 1;
    Handler handler = new Handler();
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onStart() {
        super.onStart();
        boolean checkPermissionFirst;
        // 检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13及以上

            checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, PERMISSION_CODE_FIRST,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_AUDIO
                    });

        } else {
            checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, PERMISSION_CODE_FIRST,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    });
        }

        if (checkPermissionFirst) {
            checkFirst();
        }

    }


    private void checkFirst() {
        boolean isLogin = TokenManager.getInstance().isLogin();
        if (isLogin){
            handler.postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }, 1000);
        }else {
            handler.postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, 1000);
        }

    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages("");
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);
        Log.d(TAG, "onRequestPermissionsResult: " + Arrays.toString(permissions));
        Log.d(TAG, "onRequestPermissionsResult: " + Arrays.toString(grantResults));

        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showNormal("请允许相关权限");
                return;
            }
        }
        checkFirst();
    }
}