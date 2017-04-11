package com.chant.chanttest.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.chant.chanttest.util.QMUIDisplayHelper;

import java.util.HashSet;
import java.util.Set;

public class TestLayoutManager extends RecyclerView.LayoutManager {

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

    private float offsetToAngle(int offset) {
        int oneCircleDistance = QMUIDisplayHelper.dpToPx(200); // 每拖动这么多距离就旋转一周
        return (float) offset / oneCircleDistance * 360;
    }

    private int angleToOffset(float angle) {
        int oneCircleDistance = QMUIDisplayHelper.dpToPx(200); // 每拖动这么多距离就旋转一周
        return (int) (angle / 360 * oneCircleDistance);
    }

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
