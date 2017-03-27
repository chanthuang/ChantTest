package com.chant.chanttest.coordinator;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.chant.chanttest.util.QMUIDisplayHelper;

public class BottomPanelLayout extends CoordinatorLayout {

//    private FrameLayout mContentContainer;

    public BottomPanelLayout(Context context) {
        super(context);
        init();
    }

    public BottomPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomPanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        mContentContainer = new FrameLayout(getContext());
//        mContentContainer.setBackgroundColor(0x80ff0000);
//        addView(mContentContainer, createLayoutParams());
    }

    private LayoutParams createLayoutParams() {
        int peekHeight = QMUIDisplayHelper.dpToPx(200);
        LayoutParams containerLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        BottomPanelBehavior<View> bottomSheetBehavior = new BottomPanelBehavior<>();
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(peekHeight);
        containerLp.setBehavior(bottomSheetBehavior);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
////                Log.i("chant", "[onStateChanged] newState=" + newState);
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
////                Log.i("chant", "[onSlide] slideOffset=" + slideOffset);
//            }
//        });
        return containerLp;
    }

    public void setContentView(View contentView) {
//        mContentContainer.removeAllViews();
//        mContentContainer.addView(contentView, FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setBackgroundColor(0x80ff0000);
        addView(contentView, createLayoutParams());
    }

    // 在 BottomSheetBehavior 的基础上做自己的修改
    private static class BottomPanelBehavior<V extends View> extends BottomSheetBehavior {

        private boolean mCanDrag = false;

        private boolean 要不要在EXPAND状态下当列表处于顶部时拖动任何地方都可以收起 = true;

        // Touch start
        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            Log.i("chant", "[onInterceptTouchEvent] event=(" + event.getAction() + ", " + x + ", " + y + ")");

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // 计算可滚动的区域
                Rect rectCanScroll = new Rect();
                rectCanScroll.set(0, 0, child.getWidth(), child.getHeight());
                parent.offsetDescendantRectToMyCoords(child, rectCanScroll);
                int childDraggableHeight = QMUIDisplayHelper.dpToPx(40); // RecyclerView的上面一部分可以拖动
                rectCanScroll.top += childDraggableHeight;
                boolean isPointInScrollArea = rectCanScroll.contains(x, y);
                Log.i("chant", "rectCanScroll=" + rectCanScroll + "isPointInScrollArea=" + isPointInScrollArea);

                mCanDrag = !isPointInScrollArea;

                if (要不要在EXPAND状态下当列表处于顶部时拖动任何地方都可以收起 && getState() == STATE_EXPANDED) {
                    if (child instanceof RecyclerView) {
                        boolean isScrollToTop = false;
                        RecyclerView recyclerView = (RecyclerView) child;
                        View firstChild = recyclerView.getChildAt(0);
                        if (firstChild != null) {
                            int adapterPosition = recyclerView.getChildAdapterPosition(firstChild);
                            if (adapterPosition == 0 && firstChild.getTop() == 0) {
                                isScrollToTop = true;
                            }
                            if (isScrollToTop) {
                                mCanDrag = true;
                            }
                        }
                    }
                }
            }

            return super.onInterceptTouchEvent(parent, child, event);
        }

        @Override
        public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
            // 为了在RecyclerView以外的区域滑动时不触发Drag或scroll
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Rect childRect = new Rect();
                childRect.set(0, 0, child.getWidth(), child.getHeight());
                parent.offsetDescendantRectToMyCoords(child, childRect);
                if (childRect.contains((int) event.getX(), (int) event.getY())) {
                    return super.onTouchEvent(parent, child, event);
                } else {
                    return false;
                }
            } else {
                return super.onTouchEvent(parent, child, event);
            }
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
            return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
            if (mCanDrag) {
                // 走super回执行offsetTopAndBottom并将consumed置为消耗的距离
                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
            }
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            // BottomSheetBehavior 没有实现
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }

        @Override
        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
            if (mCanDrag) {
                // 走super回执行offsetTopAndBottom并将消耗速度
                return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
            } else {
                return false;
            }
        }
    }

}
