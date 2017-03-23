package com.chant.chanttest.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Debug;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TraceLinearLayout extends LinearLayout {

    public TraceLinearLayout(Context context) {
        super(context);
    }

    public TraceLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TraceLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        Debug.startMethodTracing("chanttest");
        super.dispatchDraw(canvas);
//        Debug.stopMethodTracing();
    }
}
