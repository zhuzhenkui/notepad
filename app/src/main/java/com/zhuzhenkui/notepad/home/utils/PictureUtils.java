package com.zhuzhenkui.notepad.home.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class PictureUtils {
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int PICK_IMAGE_REQUEST = 2;

    public static File dispatchTakePictureIntent(Activity activity, String fileName) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // 创建一个文件来保存图片
            photoFile = new File(activity.getExternalFilesDir("photo"), fileName);
            // 继续如果文件被成功创建
            Uri photoURI = FileProvider.getUriForFile(activity,
                    "com.your.package.name.fileprovider", // 这应该是你的应用的fileprovider的authorities
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
        return photoFile;
    }

    public static int[] getImageWidthHeight(String imagePath) {
        int[] dimensions = new int[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 不要加载图片，只读取宽高
        BitmapFactory.decodeFile(imagePath, options);
        int deg = getImageOrientation(imagePath);
        dimensions[0] = deg == 0 | deg == 180? options.outWidth : options.outHeight;
        dimensions[1] = deg == 0 | deg == 180? options.outHeight : options.outWidth;
        return dimensions;
    }

    private static int getImageOrientation(String imagePath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
            case ExifInterface.ORIENTATION_TRANSPOSE:
            case ExifInterface.ORIENTATION_TRANSVERSE:
            default:
                return 0; // 默认认为是正常方向
        }
    }

    // 启动图库的按钮点击事件或其他触发逻辑
    public static void chooseImageFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

}
