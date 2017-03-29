package com.chant.chanttest.nestedscroll.touchevent;

import android.animation.ValueAnimator;
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
        if (getChildCount() == 0) {
            return;
        }
        if (mChildView == null) {
            mChildView = getChildAt(0);
            if (mChildView instanceof DraggableChild) {
                mDraggableChild = (DraggableChild) mChildView;
            }
            if (mChildView != null && mDraggableChild == null) {
                throw new RuntimeException("子View没有实现DraggableChild接口");
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ensureChild();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public interface DraggableChild {
        boolean isDraggable(int x, int y);

        int dragMinHeight();

        /**
         * 返回-1代表 MATCH_PARENT
         */
        int dragMaxHeight();
    }

    private float mDownY;
    private float mLastY;
    private boolean mIsDragging = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mChildView == null || mDraggableChild == null) {
            return super.onInterceptTouchEvent(ev);
        }
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
        return mIsDragging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mChildView == null || mDraggableChild == null) {
            return super.onTouchEvent(event);
        }
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
        setChildHeight(newChildHeight, false);
        mLastY = event.getY();
    }

    private void setChildHeight(int height, boolean limitMinHeight) {
        if (limitMinHeight) {
            height = Math.max(height, mDraggableChild.dragMinHeight());
        }
        height = Math.min(height, getChildMaxHeight());
        mChildView.getLayoutParams().height = height;
        mChildView.setLayoutParams(mChildView.getLayoutParams());
    }

    private int getChildMaxHeight() {
        if (mDraggableChild != null) {
            if (mDraggableChild.dragMaxHeight() < 0) {
                return this.getHeight();
            } else {
                return mDraggableChild.dragMaxHeight();
            }
        }
        return 0;
    }

    private void stopDrag() {
        mIsDragging = false;
    }

    private void flingIfDragging() {
        if (mIsDragging) {
            mVelocityTracker.computeCurrentVelocity(1000,
                    ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity());
            float vy = mVelocityTracker.getYVelocity();
            int targetHeight;
            if (Math.abs(vy) >= ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity()) {
                // 达到速度,可以按照速度的方向决定滑动的目标位置
                targetHeight = vy < 0 ? getChildMaxHeight() : mDraggableChild.dragMinHeight();
            } else {
                // 根据当前位置决定目标位置
                targetHeight = mLastY - mDownY < 0 ? getChildMaxHeight() : mDraggableChild.dragMinHeight();
            }

            ValueAnimator animator = ValueAnimator.ofInt(mChildView.getHeight(), targetHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int val = (Integer) animation.getAnimatedValue();
                    setChildHeight(val, true);
                }
            });
            animator.setDuration(200);
            animator.start();
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
