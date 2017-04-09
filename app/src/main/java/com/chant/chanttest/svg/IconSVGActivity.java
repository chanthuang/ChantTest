package com.chant.chanttest.svg;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chant.chanttest.ChantTestResource;
import com.chant.chanttest.R;

public class IconSVGActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long st = System.currentTimeMillis();
        View baseView = LayoutInflater.from(this).inflate(R.layout.activity_svg_icon, null, false);
        long et = System.currentTimeMillis();
        Log.i("chant", "SVG " + "inflate time = " + (et - st));
        setContentView(baseView);
        TraceLinearLayout traceLinearLayout = (TraceLinearLayout) findViewById(R.id.activity_main);
        traceLinearLayout.setTag("SVG");
    }

    @Override
    public Resources getResources() {
        return ChantTestResource.getInstance(super.getResources());
    }

}
