package com.chant.chanttest.multishrinkscroller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.chant.chanttest.R;

public class MultiShrinkScrollerActivity extends Activity {

    private MultiShrinkScroller mScroller;
    private boolean mHasAlreadyBeenOpened;
    private int mExtraMode;
    private boolean mIsExitAnimationInProgress;
    public static final int MODE_FULLY_EXPANDED = 4;
    private static final int DEFAULT_SCRIM_ALPHA = 0xC8;
    private static final int SCRIM_COLOR = Color.argb(DEFAULT_SCRIM_ALPHA, 0, 0, 0);

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_shrink_scroller);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        mScroller = (MultiShrinkScroller) findViewById(R.id.multiscroller);
        mScroller.initValue(this, mMultiShrinkScrollerListener);
        mWindowScrim = new ColorDrawable(SCRIM_COLOR);
        mWindowScrim.setAlpha(0);
        // showActivity();
        getWindow().setBackgroundDrawable(mWindowScrim);
        final View transparentView = findViewById(R.id.transparent_view);
        if (mScroller != null) {
            transparentView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mScroller.scrollOffBottom();
                }
            });
        }
//		mScroller.showActivity();
//		mScroller.setBackGroundAlpha(false);
    }

    @Override
    public void onBackPressed() {
        if (mScroller != null) {
            if (!mIsExitAnimationInProgress) {
                mScroller.scrollOffBottom();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void showActivity() {
        if (mScroller != null) {
            mScroller.setVisibility(View.VISIBLE);
            SchedulingUtils.doOnPreDraw(mScroller, /* drawNextFrame = */ false, new Runnable() {

                @Override
                public void run() {
                    runEntranceAnimation();
                }
            });
        }
    }

    private void runEntranceAnimation() {
        if (mHasAlreadyBeenOpened) {
            return;
        }
        mHasAlreadyBeenOpened = true;
        mScroller.scrollUpForEntranceAnimation(mExtraMode != MODE_FULLY_EXPANDED);
    }

    private ColorDrawable mWindowScrim;
    final MultiShrinkScroller.MultiShrinkScrollerListener mMultiShrinkScrollerListener = new MultiShrinkScroller.MultiShrinkScrollerListener() {

        private boolean mIsEntranceAnimationFinished;

        @Override
        public void onScrolledOffBottom() {
//            finish();
        }

        @Override
        public void onEnterFullscreen() {
            // updateStatusBarColor();
        }

        @Override
        public void onExitFullscreen() {
            // updateStatusBarColor();
        }

        @Override
        public void onStartScrollOffBottom() {
            mIsExitAnimationInProgress = true;
        }

        @Override
        public void onEntranceAnimationDone() {
            mIsEntranceAnimationFinished = true;
        }

        @Override
        public void onTransparentViewHeightChange(float ratio) {
            if (mIsEntranceAnimationFinished) {
                mWindowScrim.setAlpha((int) (0xFF * ratio));
            }
        }
    };

}
