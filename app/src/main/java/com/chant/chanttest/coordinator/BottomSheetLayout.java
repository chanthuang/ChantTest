package com.chant.chanttest.coordinator;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chant.chanttest.util.QMUIDisplayHelper;

public class BottomSheetLayout extends CoordinatorLayout {

    private FrameLayout mContentContainer;

    public BottomSheetLayout(Context context) {
        super(context);
        init();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContentContainer = new FrameLayout(getContext());
        mContentContainer.setBackgroundColor(0x80ff0000);
        addView(mContentContainer, createLayoutParams());
    }

    private LayoutParams createLayoutParams() {
        LayoutParams containerLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        BottomPanelBehavior<View> bottomSheetBehavior = new BottomPanelBehavior<>();
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(QMUIDisplayHelper.dpToPx(200));
        containerLp.setBehavior(bottomSheetBehavior);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Log.i("chant", "[onStateChanged] newState=" + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                Log.i("chant", "[onSlide] slideOffset=" + slideOffset);
            }
        });
        return containerLp;
    }

    public void setContentView(View contentView) {
        mContentContainer.removeAllViews();
        mContentContainer.addView(contentView, FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        addView(contentView, createLayoutParams());
    }

    // 在 BottomSheetBehavior 的基础上做自己的修改
    private static class BottomPanelBehavior<V extends View> extends BottomSheetBehavior {

        private boolean mCanScroll = false;

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            Log.i("chant", "[onInterceptTouchEvent] event=(" + event.getAction() + ", " + x + ", " + y + ")");

            // 整个child不能拖
//            boolean isPointInChildBounds = parent.isPointInChildBounds(child, x, y);
//            Log.i("chant", "isPointInChildBounds = " + isPointInChildBounds);

            // child 的上面一部分也能拖
//            Rect rect = new Rect();
//            rect.set(0, 0, child.getWidth(), child.getHeight());
//            parent.offsetDescendantRectToMyCoords(child, rect);
//            rect.offset(child.getScrollX(), child.getScrollY());
//            int childDraggableHeight = child.getHeight() / 2;
//            rect.top += childDraggableHeight;
//            boolean isPointInChildBounds = rect.contains(x, y);
//            mCanScroll = isPointInChildBounds;
//            return false;

            return super.onInterceptTouchEvent(parent, child, event);
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
//            if (!mCanScroll) {
//                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//            }
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        }

        @Override
        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
//            if (!mCanScroll) {
//                return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
//            }
//            return false;
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        }

        @Override
        public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
//            Log.i("chant", "[onTouchEvent] event=(" + event.getAction() + ", " + event.getX() + ", " + event.getY() + ")");
            return super.onTouchEvent(parent, child, event);
        }
    }

}
