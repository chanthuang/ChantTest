package com.chant.chanttest.svg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chant.chanttest.R;

public abstract class TraceIconActivity extends Activity {

    private int layoutResId = R.layout.activity_vector_icon;
    private String tag = "Vector";

    public TraceIconActivity(int layoutResId, String tag) {
        this.layoutResId = layoutResId;
        this.tag = tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long st = System.currentTimeMillis();
        View baseView = LayoutInflater.from(this).inflate(layoutResId, null, false);
        long et = System.currentTimeMillis();
        Log.i("chant", tag + " inflate time = " + (et - st));
        TraceLinearLayout traceLinearLayout = (TraceLinearLayout) baseView.findViewById(R.id.activity_main);
        traceLinearLayout.setTag(tag);
        setContentView(baseView);
    }

}
