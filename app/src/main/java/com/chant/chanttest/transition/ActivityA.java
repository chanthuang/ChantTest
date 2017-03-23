package com.chant.chanttest.transition;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.chant.chanttest.R;

public class ActivityA extends FragmentActivity implements View.OnClickListener {

    private ViewGroup mRootView;
    private View mRedBox, mGreenBox, mBlueBox, mBlackBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_transition_main);

        mRootView = (ViewGroup) findViewById(R.id.layout_root_view);
        mRootView.setOnClickListener(this);

        mRedBox = findViewById(R.id.red_box);
        mGreenBox = findViewById(R.id.green_box);
        mBlueBox = findViewById(R.id.blue_box);
        mBlackBox = findViewById(R.id.black_box);
    }

    @Override
    public void onClick(View v) {
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        //退出时使用
        getWindow().setExitTransition(explode);
        //第一次进入时使用
        getWindow().setEnterTransition(explode);
        //再次进入时使用
        getWindow().setReenterTransition(explode);
        Intent intent = new Intent(this, ActivityB.class);
//        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        View firstSharedView = findViewById(R.id.red_box);
        Pair first = new Pair<>(firstSharedView, ViewCompat.getTransitionName(firstSharedView));
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, first).toBundle());


//        if (Build.VERSION.SDK_INT >= 19) {
//            TransitionManager.beginDelayedTransition(mRootView, new Fade());
//        }
//        toggleVisibility(mRedBox, mGreenBox, mBlueBox, mBlackBox);
    }

    private static void toggleVisibility(View... views) {
        for (View view : views) {
            boolean isVisible = view.getVisibility() == View.VISIBLE;
            view.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
