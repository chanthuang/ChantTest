package com.chant.chanttest.coordinator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.chant.chanttest.R;


public class CoordinatorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout baseView = (CoordinatorLayout) LayoutInflater.from(this)
                .inflate(R.layout.activity_coordinator, null, false);
        setContentView(baseView);
        View bottomView = findViewById(R.id.bottomView);
        bottomView.getLayoutParams().height = (int) (getDisplayMetrics(this).heightPixels * .9f);
        bottomView.setLayoutParams(bottomView.getLayoutParams());
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomView);
        bottomSheetBehavior.setPeekHeight((int) (getDisplayMetrics(this).heightPixels * .4f));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.i("chant", "[onStateChanged] newState=" + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("chant", "[onSlide] slideOffset=" + slideOffset);
            }
        });
    }
    /**
     * DisplayMetrics
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
}

