package com.chant.chanttest.nestedscroll.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class DragOrScrollParentView extends FrameLayout {

    private View mChildView;
    private DraggableChild mDraggableChild;
    private int mChildMaxHeight = -1; // -1表示最大是MATCH_PARENT

    public DragOrScrollParentView(Context context) {
        super(context);
    }

    public DragOrScrollParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragOrScrollParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void ensureChild() {
        if (mChildView == null) {
            mChildView = getChildAt(0);
            if (mChildView instanceof DraggableChild) {
                mDraggableChild = (DraggableChild) mChildView;
            }
            if (mChildView == null || mDraggableChild == null) {
                throw new RuntimeException("没有子View或子View没有实现DraggableChild接口");
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ensureChild();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface DraggableChild {
        boolean isDraggable(int x, int y);

        int dragMinHeight();
    }

    private float mDownY;
    private float mLastY;
    private boolean mIsDragging = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mLastY = ev.getY();
                mIsDragging = mDraggableChild.isDraggable((int) ev.getX() - mChildView.getLeft(), (int) ev.getY() - mChildView.getTop());
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
//        return super.onInterceptTouchEvent(ev);
        return mIsDragging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        acquireVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                drag(event);
                break;
            case MotionEvent.ACTION_UP:
                flingIfDragging();
                stopDrag();
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                stopDrag();
                releaseVelocityTracker();
                break;
        }
        return mIsDragging;
    }

    private void drag(MotionEvent event) {
        float dy = event.getY() - mLastY;
        int newChildHeight = (int) (mChildView.getHeight() - dy);
        setChildHeight(newChildHeight);
        if (dy < 0) {
            // 往上
        } else {
            // 往下
        }
        mLastY = event.getY();
    }

    private void setChildHeight(int height) {
        height = Math.max(height, mDraggableChild.dragMinHeight());
        if (mChildMaxHeight == -1) {
            // MATCH_PARENT
            height = Math.min(height, this.getHeight());
        } else {
            height = Math.min(height, mChildMaxHeight);
        }
        mChildView.getLayoutParams().height = height;
        mChildView.setLayoutParams(mChildView.getLayoutParams());
    }

    private void stopDrag() {
        mIsDragging = false;
    }

    private void flingIfDragging() {
        if (mIsDragging) {
            mVelocityTracker.computeCurrentVelocity(1000,
                    ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity());
            float vy = mVelocityTracker.getYVelocity();
        }
    }

    private VelocityTracker mVelocityTracker;

    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

}
