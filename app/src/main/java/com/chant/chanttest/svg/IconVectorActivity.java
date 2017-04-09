package com.chant.chanttest.svg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chant.chanttest.R;

public class IconVectorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long st = System.currentTimeMillis();
        View baseView = LayoutInflater.from(this).inflate(R.layout.activity_vector_icon, null, false);
        long et = System.currentTimeMillis();
        Log.i("chant", "VectorDrawable " + "inflate time = " + (et - st));
        setContentView(baseView);
        TraceLinearLayout traceLinearLayout = (TraceLinearLayout) findViewById(R.id.activity_main);
        traceLinearLayout.setTag("VectorDrawable");
    }

}
