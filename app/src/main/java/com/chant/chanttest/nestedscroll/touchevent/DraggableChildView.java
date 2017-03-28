package com.chant.chanttest.nestedscroll.touchevent;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.chant.chanttest.R;
import com.chant.chanttest.util.QMUIDisplayHelper;

public class DraggableChildView extends LinearLayout implements DragOrScrollParentView.DraggableChild {

    private View mHeaderView;

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
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.panel_header);
    }

    @Override
    public boolean isDraggable(int x, int y) {
        Rect rect = new Rect();// 可以改成成员变量
        rect.set(0, 0, mHeaderView.getWidth(), mHeaderView.getHeight());
        offsetDescendantRectToMyCoords(mHeaderView, rect);
        return rect.contains(x, y);
    }

    @Override
    public int dragMinHeight() {
        return mHeaderView.getHeight() + QMUIDisplayHelper.dpToPx(152);
    }
}
