package com.chant.chanttest.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CircleLayoutManager extends CustomLayoutManager {

    private static int INTERVAL_ANGLE = 60;// The default interval angle between each items
    private static float DISTANCE_RATIO = 10f; // Finger swipe distance divide alibaba_vlayout_item rotate angle

    private int mRadius;

    public CircleLayoutManager(Context context) {
        super(context);
    }

    public CircleLayoutManager(Context context, boolean isClockWise) {
        super(context, isClockWise);
    }

    @Override
    protected float setInterval() {
        return INTERVAL_ANGLE;
//        return mDecoratedChildWidth;
    }

    @Override
    protected void setUp() {
        mRadius = getHeight();
    }

    @Override
    protected float maxRemoveOffset() {
        return 90;
//        return super.maxRemoveOffset();
    }

    @Override
    protected float minRemoveOffset() {
        return -90;
//        return super.minRemoveOffset();
    }

    @Override
    protected int calItemLeftPosition(float targetOffset) {
        return (int) (mRadius * Math.sin(Math.toRadians(targetOffset)));
//        return super.calItemLeftPosition(targetOffset);
    }

    @Override
    protected int calItemTopPosition(float targetOffset) {
        return (int) (mRadius - mRadius * Math.cos(Math.toRadians(targetOffset)));
//        return super.calItemTopPosition(targetOffset);
    }

    @Override
    protected void setItemViewProperty(View itemView, float targetOffset) {
        itemView.setRotation(targetOffset);
        //
    }

    @Override
    protected float propertyChangeWhenScroll(View itemView) {
        return itemView.getRotation();
//        return super.propertyChangeWhenScroll(itemView);
    }

    @Override
    protected float getDistanceRatio() {
        return DISTANCE_RATIO;
//        return 1f;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


}
