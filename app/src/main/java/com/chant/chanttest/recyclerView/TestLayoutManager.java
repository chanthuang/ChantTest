package com.chant.chanttest.recyclerView;

import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.chant.chanttest.util.QMUIDisplayHelper;

import java.util.HashSet;
import java.util.Set;

public class TestLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {

    private SparseArray<Float> mAllItemsAngle = new SparseArray<>();
    private SparseBooleanArray mIsItemsAttached = new SparseBooleanArray();

    private float mCurrentAngle = 0f; // 360度制
    private Builder mBuilder;

    public TestLayoutManager(Builder builder) {
        mBuilder = builder;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            mCurrentAngle = 0;
            return;
        }

        for (int i = 0; i < getItemCount(); i++) {
            mIsItemsAttached.put(i, false);
            mAllItemsAngle.put(i, i * mBuilder.mIntervalAngle);
        }
        detachAndScrapAttachedViews(recycler);
        fill(recycler, state, false);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state, boolean addViewToFirst) {
        if (state.isPreLayout()) {
            return; // 先不做绘制动画时的处理
        }

        // 把多余的View移除回收
        Set<View> removeChildren = new HashSet<>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int position = getPosition(view);
            if (isChildRangeOutOfRange(position)) { // 注意这里要用 position 去判断,不能用 childIndex
                mIsItemsAttached.put(position, false);
//                removeAndRecycleView(view, recycler); // 不能在遍历的过程remove,否则下标混乱
                removeChildren.add(view);
            }
        }
        for (View v : removeChildren) {
            removeAndRecycleView(v, recycler);
        }

        // 把View add上
        for (int i = 0; i < getItemCount(); i++) {
            if (!isChildRangeOutOfRange(i)) {
                if (!mIsItemsAttached.get(i)) {
                    View view = recycler.getViewForPosition(i);
                    measureChildWithMargins(view, 0, 0);
                    if (addViewToFirst) {
                        addView(view, 0);
                    } else {
                        addView(view);
                    }
                    layoutChild(view, i);
                    mIsItemsAttached.put(i, true);
                }
            }
        }
    }

    private void layoutChild(View child, int position) {
        int width = getDecoratedMeasuredWidth(child);
        int height = getDecoratedMeasuredHeight(child);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2 + mBuilder.mRadius;
        double angleInPI = Math.toRadians(mAllItemsAngle.get(position) - mCurrentAngle);
        int viewCenterX = centerX + (int) (Math.sin(angleInPI) * mBuilder.mRadius);
        int viewCenterY = centerY - (int) (Math.cos(angleInPI) * mBuilder.mRadius);
        layoutDecorated(child, viewCenterX - width / 2, viewCenterY - height / 2, viewCenterX + width / 2, viewCenterY + height / 2);

        rotateChild(position, child);
    }

    private void rotateChild(int position, View child) {
        child.setRotation(position * mBuilder.mIntervalAngle - mCurrentAngle);
    }

    private boolean isChildRangeOutOfRange(int position) {
        float childAngle = mAllItemsAngle.get(position) - mCurrentAngle;
        boolean result = childAngle < minAngleToShow() || childAngle > maxAngleToShow();
//        Log.i("chant", "[isChildRangeOutOfRange] index=" + index + ", result=" + result);
        return result;
    }

    protected float maxAngleToShow() {
        return 60;
    }

    protected float minAngleToShow() {
        return -60;
    }

    protected float minAngle() {
        return 0;
    }

    protected float maxAngle() {
        return getItemCount() > 0 ? (getItemCount() - 1) * mBuilder.mIntervalAngle : 0;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        float targetAngel = mCurrentAngle + offsetToAngle(dx);
        int realScrollX;
        if (targetAngel < minAngle()) {
            realScrollX = angleToOffset(minAngle() - targetAngel);
            mCurrentAngle = minAngle();
        } else if (targetAngel > maxAngle()) {
            realScrollX = angleToOffset(mCurrentAngle - maxAngle());
            mCurrentAngle = maxAngle();
        } else {
            realScrollX = dx;
            mCurrentAngle = targetAngel;
        }

        //re-calculate the rotate x,y of each items
        for (int i = 0; i < getChildCount(); i++) {
            View scrap = getChildAt(i);
            int position = getPosition(scrap);
            layoutChild(scrap, position);
        }

        fill(recycler, state, dx < 0);

        return realScrollX;
    }

    // 每拖动这么多距离就旋转一周
    private int oneCircleDistance() {
        return QMUIDisplayHelper.dpToPx(200);
    }

    private float offsetToAngle(int offset) {
        int oneCircleDistance = oneCircleDistance();
        return (float) offset / oneCircleDistance * 360;
    }

    private int angleToOffset(float angle) {
        int oneCircleDistance = oneCircleDistance();
        return (int) (angle / 360 * oneCircleDistance);
    }

    // smoothScroll 相关

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final float targetAngle = position * mBuilder.mIntervalAngle;
        final boolean isScrollLeft = targetAngle < mCurrentAngle;
        LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int getHorizontalSnapPreference() {
                // 要覆盖 super，否则当 targetView 处于屏幕内又不在目标位置时，super会返回 SNAP_TO_ANY，导致没有执行动画
//                return super.getHorizontalSnapPreference();
                return isScrollLeft ? SNAP_TO_END : SNAP_TO_START;
            }
        };
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    @Override
    public void scrollToPosition(int position) {
        if (position < 0 || position > getItemCount() - 1) {
            return;
        }
        float targetAngle = position * mBuilder.mIntervalAngle;
        if (targetAngle == mCurrentAngle) {
            return;
        }
        mCurrentAngle = targetAngle;
        if (mCurrentAngle > maxAngle()) {
            mCurrentAngle = maxAngle();
        } else if (mCurrentAngle < minAngle()) {
            mCurrentAngle = minAngle();
        }
        requestLayout();
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        float targetAngle = targetPosition * mBuilder.mIntervalAngle;
        int targetOffset = angleToOffset(targetAngle);
        // 返回1或-1代表滚动到目标位置所需的方向向量
        // 还要乘以一个比例，是因为我们之前将水平滑动的距离映射到旋转的角度上，现在旋转到目标所需的滑动距离也需要映射回去。
        float ratio = (float) targetOffset / oneCircleDistance();
        return new PointF(
                ratio * (targetAngle > mCurrentAngle ? 1 : -1), 0
        );
    }

    // end smoothScroll 相关

    // builder

    public static class Builder {
        float mIntervalAngle; // 每个Child之间的角度,360度制
        int mRadius; // 半径

        public Builder() {
        }

        public Builder setIntervalAngle(int intervalAngle) {
            mIntervalAngle = intervalAngle;
            return this;
        }

        public Builder setRadius(int radius) {
            mRadius = radius;
            return this;
        }
    }
}
