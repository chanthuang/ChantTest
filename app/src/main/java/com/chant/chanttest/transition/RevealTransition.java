package com.chant.chanttest.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import java.lang.annotation.Target;

/**
 * @author chantchen
 * @date 2017-03-13
 */

public class RevealTransition extends Visibility {
    private final Point mEpicenter;
    private final int mSmallRadius;
    private final int mBigRadius;
    private final long mDuration;

    public RevealTransition(Point epicenter, int smallRadius, int bigRadius, long duration) {
        mEpicenter = epicenter;
        mSmallRadius = smallRadius;
        mBigRadius = bigRadius;
        mDuration = duration;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        Animator animator = ViewAnimationUtils.createCircularReveal(view, mEpicenter.x, mEpicenter.y,
                mSmallRadius, mBigRadius);
        animator.setDuration(mDuration);
        return animator;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        Animator animator = ViewAnimationUtils.createCircularReveal(view, mEpicenter.x, mEpicenter.y,
                mBigRadius, mSmallRadius);
        animator.setDuration(mDuration);
        return animator;
    }
}
