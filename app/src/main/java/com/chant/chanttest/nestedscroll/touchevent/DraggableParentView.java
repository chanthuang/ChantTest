package com.chant.chanttest.nestedscroll.touchevent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class DraggableParentView extends FrameLayout {

    public interface DraggableChild {
        /**
         * @param parentView parent
         * @param x 相对于 child 的坐标
         * @param y 相对于 child 的坐标
         * @return 在这个坐标上是否开始可以拖动
         */
        boolean isPointDraggable(DraggableParentView parentView, int x, int y);
    }

    public interface DraggingChangedListener {
        /**
         * Child 的高度改变时触发
         *
         * @param height      child 的实时高度
         * @param slideOffset 滚动的偏移量
         */
        void onChildHeightChanged(int height, float slideOffset);
    }

    public interface OnStateChangedListener {
        /**
         * 状态改变时触发
         */
        void onStateChanged(@State int state);

        void onStateSettling(@State int currentState, @State int toState);
    }

    private int mTouchSlop;

    /**
     * 拖动过程中
     */
    public static final int STATE_DRAGGING = 1;
    /**
     * 展开状态
     */
    public static final int STATE_EXPANDED = 2;
    /**
     * 自动滑动过程中
     */
    public static final int STATE_SETTLING = 3;
    /**
     * 收起状态
     */
    public static final int STATE_COLLAPSED = 4;

    @IntDef({STATE_EXPANDED, STATE_COLLAPSED, STATE_SETTLING, STATE_DRAGGING})
    public @interface State {
    }

    @State
    private int mState = STATE_COLLAPSED;

    private int mChildMaxHeight = -1;
    private int mPeekHeight = 0;
    private float mDownY;
    private float mLastY;
    private boolean mIsDragging = false;
    private boolean mIsDownInDraggableArea = false;

    private View mChildView;
    private DraggableChild mDraggableChild;
    private DraggingChangedListener mDraggingChangedListener;
    private OnStateChangedListener mOnStateChangedListener;

    public DraggableParentView(Context context) {
        super(context);
        init();
    }

    public DraggableParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mChildView == null || mDraggableChild == null) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 重置状态 && 记录值
                mIsDragging = false;
                mDownY = ev.getY();
                mLastY = ev.getY();
                // 按下时记录是否处于可拖动区域
                mIsDownInDraggableArea = mDraggableChild.isPointDraggable(
                        this,
                        (int) ev.getX() - mChildView.getLeft(),
                        (int) ev.getY() - mChildView.getTop());
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = ev.getY();
                mIsDragging = motionShouldStartDrag(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return mIsDragging;
    }

    private boolean motionShouldStartDrag(MotionEvent event) {
        if (!mIsDownInDraggableArea) {
            return false;
        }
        final float deltaY = event.getY() - mDownY;
        return deltaY > mTouchSlop || deltaY < -mTouchSlop;
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
                if (mIsDragging) {
                    drag(event);
                }
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
        mLastY = event.getY();
        setStateInternal(STATE_DRAGGING);
    }

    public void setPeekHeight(int peekHeight) {
        mPeekHeight = Math.max(0, peekHeight);
        // update child height
        if (mState == STATE_COLLAPSED) {
            ensureChild();
            if (mChildView != null) {
                mChildView.getLayoutParams().height = peekHeight;
                mChildView.setLayoutParams(mChildView.getLayoutParams());
            }
        }
    }

    public int getPeekHeight() {
        return mPeekHeight;
    }

    public void setChildMaxHeight(int maxHeight) {
        mChildMaxHeight = maxHeight;
    }

    public int getChildMaxHeight() {
        if (mChildMaxHeight < 0) {
            mChildMaxHeight = getHeight();
        }
        return mChildMaxHeight;
    }

    private void setChildHeight(int height) {
        // 最小值保护
        height = Math.max(height, 0);
        // 不能往下拖到小于 peekHeight
        boolean limitMinHeight = true;
        if (limitMinHeight) {
            height = Math.max(height, getPeekHeight());
        }
        height = Math.min(height, getChildMaxHeight());
        mChildView.getLayoutParams().height = height;
        mChildView.setLayoutParams(mChildView.getLayoutParams());
        if (mDraggingChangedListener != null) {
            mDraggingChangedListener.onChildHeightChanged(height, (float) (height - getPeekHeight()) / (getChildMaxHeight() - getPeekHeight()));
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
            int state;
            if (Math.abs(vy) >= ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity()) {
                // 达到速度,可以按照速度的方向决定滑动的目标位置
                state = vy < 0 ? STATE_EXPANDED : STATE_COLLAPSED;
            } else {
                // 根据当前位置决定目标位置
                state = mLastY - mDownY < 0 ? STATE_EXPANDED : STATE_COLLAPSED;
            }
            setState(state);
        }
    }

    private ValueAnimator mAnimator;

    private void animateToState(final int state) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        int targetHeight;
        if (state == STATE_EXPANDED) {
            targetHeight = getChildMaxHeight();
        } else {
            targetHeight = mPeekHeight;
        }
        ValueAnimator animator = ValueAnimator.ofInt(mChildView.getHeight(), targetHeight);
        animator.setInterpolator(PathInterpolatorCompat.create(.19f, 1f, .22f, 1f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                setChildHeight(val);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setStateInternal(state);
            }
        });
        animator.setDuration(480);
        animator.start();
        mAnimator = animator;
    }

    public void setState(@State int state) {
        if (state == mState) {
            return;
        }
        if (state != STATE_COLLAPSED && state != STATE_EXPANDED) {
            throw new IllegalArgumentException("Illegal state argument: " + state + ". setState(int) 只能传入 STATE_COLLAPSED 或 STATE_EXPANDED");
        }

        if (mChildView == null) {
            mState = state;
        } else {
            setStateInternal(STATE_SETTLING);
            if (mOnStateChangedListener != null) {
                mOnStateChangedListener.onStateSettling(STATE_SETTLING, state);
            }
            animateToState(state);
        }
    }

    @State
    public final int getState() {
        return mState;
    }

    private void setStateInternal(@State int state) {
        if (mState == state) {
            return;
        }
        mState = state;
        if (mOnStateChangedListener != null) {
            mOnStateChangedListener.onStateChanged(state);
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

    public void setDraggingChangedListener(DraggingChangedListener draggingChangedListener) {
        mDraggingChangedListener = draggingChangedListener;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        mOnStateChangedListener = onStateChangedListener;
    }

}
