package com.chant.chanttest.wavedrawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.AnimationUtils;

import com.chanthuang.util.QMUIDisplayHelper;

public class TTSSoundWaveDrawable extends Drawable implements Animatable {

    private Context mContext;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int mLineCount = 7;
    private int mLineStepWidth;
    private int mLineWidth;
    private int mLineMinHeight;
    private int mLineMaxHeight;
    private float[] mStartLineHeights;
    private float[] mCurrentLineHeights;
    private float[] mTargetLineHeights;

    private int mLoopDuration = 200;
    private long mLoopStartTime = 0;
    private boolean mRunnable = false; // runnable==true 时才有可能 running
    private boolean mIsRunning = false;

    public TTSSoundWaveDrawable(Context context) {
        mContext = context;
        mWidth = QMUIDisplayHelper.dp2px(context, 24);
        mHeight = QMUIDisplayHelper.dp2px(context, 48);
        mLineStepWidth = QMUIDisplayHelper.dp2px(mContext, 3);
        mLineWidth = QMUIDisplayHelper.dp2px(mContext, 1);
        mLineMinHeight = QMUIDisplayHelper.dp2px(mContext, 1);
        mLineMaxHeight = QMUIDisplayHelper.dp2px(mContext, 32);

        mStartLineHeights = new float[mLineCount];
        randomHeights(mStartLineHeights);
        mCurrentLineHeights = new float[mLineCount];
        System.arraycopy(mStartLineHeights, 0, mCurrentLineHeights, 0, mLineCount);
        mTargetLineHeights = new float[mLineCount];
        System.arraycopy(mStartLineHeights, 0, mTargetLineHeights, 0, mLineCount);

        setupPaint();
    }

    private void setupPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF1B88EE);
    }

    @Override
    public void draw(Canvas canvas) {
        // update
        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        float progress = progress(mLoopStartTime, currentTime, mLoopDuration);
        for (int i = 0; i < mLineCount; i++) {
            mCurrentLineHeights[i] = progressValue(mStartLineHeights[i], mTargetLineHeights[i], progress);
        }

        // draw
        final int totalLineWidth = mLineStepWidth * (mLineCount - 1) + mLineWidth;
        final float startX = ((float) mWidth - totalLineWidth) / 2;
        for (int i = 0; i < mLineCount; i++) {
            float x = startX + i * mLineStepWidth;
            float height = mCurrentLineHeights[i];
            float y = ((float) mHeight - height) / 2;
            canvas.drawRect(x, y, x + mLineWidth, y + height, mPaint);
        }

        // loop
        if (mIsRunning) {
            // check if need reset
            long timePassed = currentTime - mLoopStartTime;
            if (timePassed >= mLoopDuration) {
                mLoopStartTime = currentTime;
                prepareNextHeight();
            }
            invalidateSelf();
        }
    }

    private void prepareNextHeight() {
        System.arraycopy(mCurrentLineHeights, 0, mStartLineHeights, 0, mLineCount);
        randomHeights(mTargetLineHeights);
    }

    private void randomHeights(float[] heights) {
        for (int i = 0; i < heights.length; i++) {
            heights[i] = randomHeight(i);
        }
    }

    private float randomHeight(int index) {
        float height = progressValue(mLineMinHeight, mLineMaxHeight, Math.random());
        // 从中间往两边高度递减
        float heightFilter;
        switch (Math.abs(index - mLineCount / 2)) {
            case 0:
                heightFilter = 1;
                break;
            case 1:
                heightFilter = 0.75f;
                break;
            case 2:
                heightFilter = 0.5f;
                break;
            case 3:
                heightFilter = 0.25f;
                break;
            default:
                heightFilter = 0.25f;
        }
        return height * heightFilter;
    }

    private float progress(long startTime, long nowTime, long duration) {
        if (nowTime <= startTime) return 0f;
        if (nowTime >= startTime + duration) return 1f;
        return (float) (nowTime - startTime) / duration;
    }

    private float progressValue(float min, float max, double progress) {
        return (float) (min + (max - min) * progress);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public void start() {
        mRunnable = true;
        tryToStart();
    }

    private void tryToStart() {
        if (mRunnable && !mIsRunning) {
            mLoopStartTime = AnimationUtils.currentAnimationTimeMillis();
            mIsRunning = true;
            prepareNextHeight();
            invalidateSelf();
        }
    }

    @Override
    public void stop() {
        mRunnable = false;
        mIsRunning = false;
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    // 会在 ImageView 的 onAttach/onDetach/setVisibility 中被调用到
    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (visible) {
            if (changed || restart) {
                tryToStart();
            }
        } else {
            mIsRunning = false;
        }
        return changed;
    }
}
