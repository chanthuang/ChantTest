package com.chant.chanttest.recyclerView;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

public class TestLayoutManager extends RecyclerView.LayoutManager {

    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    private SparseBooleanArray hasAttachedItems = new SparseBooleanArray();

    public TestLayoutManager() {
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        fill(recycler);
    }

    private void fill(RecyclerView.Recycler recycler) {
        int offsetY = 0;
        totalHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            Rect frame = allItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            frame.set(0, offsetY, width, offsetY + height);
            allItemFrames.put(i, frame);

            layoutDecorated(view, 0, offsetY, width, offsetY + height);
            offsetY += height;
            totalHeight += height;
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    private int verticalScrollOffset;
    private int totalHeight;

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int travel = dy;

        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }

        verticalScrollOffset += travel;

        offsetChildrenVertical(-travel);
        Log.i("chant", "child count=" + getChildCount());
        return travel;
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
