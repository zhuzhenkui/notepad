package com.example.common_lib.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 修改备注：
 */
public class GlideRoundTransform extends BitmapTransformation {

    private static final float DEFAULT_RADIUS = 20f;
    private float radius = DEFAULT_RADIUS;
    private int borderResId = 0;
    private Context mContext;


    public GlideRoundTransform(Context context) {
        this(context, DEFAULT_RADIUS);
        this.mContext = context;
    }


    public GlideRoundTransform(Context context, float radius) {
        this.radius = radius;
        this.mContext = context;
    }

    public GlideRoundTransform(Context context, float radius, int borderResId) {
        this.radius = radius;
        this.borderResId = borderResId;
        this.mContext = context;
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }


    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap
                    .createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        if (borderResId > 0) {
            Paint borderPaint = new Paint();
            borderPaint.setColor(ContextCompat.getColor(mContext, borderResId));
            borderPaint.setStyle(Paint.Style.STROKE);
            RectF rectFBorder = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            borderPaint.setStrokeWidth(1);
            canvas.drawRoundRect(rectFBorder, radius, radius, borderPaint);
        }
        return result;
    }


    //
    //
    //@Override
    //public String getId() {
    //    return getClass().getName() + Math.round(radius);
    //}


    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
