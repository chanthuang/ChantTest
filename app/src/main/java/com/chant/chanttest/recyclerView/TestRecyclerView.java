package com.chant.chanttest.recyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class TestRecyclerView extends RecyclerView {

    public TestRecyclerView(Context context) {
        super(context);
    }

    public TestRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Paint mPaint;

    private Paint ensurePaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(0x80ff0000);
            mPaint.setStyle(Paint.Style.FILL);
        }
        return mPaint;
    }

    @Override
    public void onDraw(Canvas c) {
        drawTest(c);
        super.onDraw(c);
    }

    private void drawTest(Canvas c) {
        int radius = getWidth() / 2;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2 + radius;
        int angle = 60;
        c.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                -90 - angle, angle * 2, true, ensurePaint());
    }
}
