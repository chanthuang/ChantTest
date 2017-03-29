package com.chant.chanttest.nestedscroll.touchevent;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.chant.chanttest.util.QMUIDisplayHelper;

public class DraggableChildView extends LinearLayout implements DragOrScrollParentView.DraggableChild {

    private Rect mRect;

    public DraggableChildView(Context context) {
        super(context);
    }

    public DraggableChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isDraggable(int x, int y) {
        if (mRect == null) {
            mRect = new Rect(0, 0, getWidth(), QMUIDisplayHelper.dpToPx(48));
        }
        return mRect.contains(x, y);
    }

    @Override
    public int dragMinHeight() {
        return QMUIDisplayHelper.dpToPx(152);
    }

    @Override
    public int dragMaxHeight() {
        return -1;
    }
}
