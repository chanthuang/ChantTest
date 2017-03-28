package com.chant.chanttest.nestedscroll.touchevent;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class DragOrScrollParentView extends ViewGroup {

    private View mChildView;

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
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ensureChild();
        measureChild(mChildView, widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mChildView.layout(0, b - mChildView.getMeasuredHeight(), mChildView.getMeasuredWidth(), b);
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
                mIsDragging = !isEventInsideChild(ev);
                // 防止滚动过程中又触发新的滚动，会前后冲突
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
//        return super.onInterceptTouchEvent(ev);
        return mIsDragging;
    }

    private boolean isEventInsideChild(MotionEvent ev) {
        Rect childRect = new Rect();
        childRect.set(0, 0, mChildView.getWidth(), mChildView.getHeight());
        this.offsetDescendantRectToMyCoords(mChildView, childRect);
        return childRect.contains((int) ev.getX(), (int) ev.getY());
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
        if (dy < 0) {
            // 往上
            setChildHeight((int) (mChildView.getHeight() - dy));
        } else {
            // 往下
            setChildHeight((int) (mChildView.getHeight() - dy));
        }
        mLastY = event.getY();
    }

    private void setChildHeight(int height) {
        if (height >= 0) {
            mChildView.getLayoutParams().height = height;
            mChildView.setLayoutParams(mChildView.getLayoutParams());
        }
    }

    private void stopDrag() {
        mIsDragging = false;
    }

    private void flingIfDragging() {
        if (mIsDragging) {
            mVelocityTracker.computeCurrentVelocity(1000,
                    ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity());
            float vy = mVelocityTracker.getYVelocity();
            mScroller.fling(0, mChildView.getTop(), 0, (int) vy, 0, 0, 0, this.getHeight());
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

    private Scroller mScroller = new Scroller(getContext());

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currentY = mScroller.getCurrY();
            int childHeight = getHeight() - currentY;
            setChildHeight(childHeight);
            invalidate();
        }
    }
}
