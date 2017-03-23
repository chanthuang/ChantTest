package com.chant.chanttest.wavedrawable;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chant.chanttest.R;

public class DrawableActivity extends Activity {

    private ImageView mImageView1;
    private ImageView mImageView2;
    private TTSSoundWaveDrawable mDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View baseView = LayoutInflater.from(this)
                .inflate(R.layout.activity_drawable, null, false);
        setContentView(baseView);
        mImageView1 = (ImageView) findViewById(R.id.imageView1);
        mImageView2 = (ImageView) findViewById(R.id.imageView2);
        mDrawable = new TTSSoundWaveDrawable(this);
        mImageView1.setImageDrawable(mDrawable);

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawable.start();
            }
        });
        findViewById(R.id.stopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawable.stop();
            }
        });
        findViewById(R.id.showButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView1.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.hideButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView1.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.leftButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mImageView1.setImageDrawable(mDrawable);
//                mImageView2.setImageDrawable(null);
            }
        });
        findViewById(R.id.rightButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView2.setImageDrawable(mDrawable);
                mImageView1.setImageDrawable(null);
            }
        });
    }
}

