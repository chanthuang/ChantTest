package com.chant.chanttest.colorfilter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.chant.chanttest.R;
import com.chant.chanttest.util.QMUIDisplayHelper;

import java.util.ArrayList;

public class ColorFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView baseView = new ScrollView(this);
        baseView.setBackgroundColor(Color.WHITE);
        setContentView(baseView);

        LinearLayout contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
        baseView.addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        contentView.addView(row, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int white = Color.TRANSPARENT;
        int yellow = 0xFFF5EFDA;
        int green = 0xFFC0EDC6;
        int black = 0xFF1D1D1F;
        addImageView(row, white, false);
//        addImageView(row, yellow, true);
        addImageView(row, yellow, false);
        addImageView(row, green, false);
        addImageView(row, black, false);
    }

    private void addImageView(LinearLayout row, int color, boolean useSystem) {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, QMUIDisplayHelper.dpToPx(30));
        lp.weight = 1;
        row.addView(imageView, lp);

        int srcColor = 0x99C8955E;
        ColorDrawable colorD = new ColorDrawable(srcColor);
        if (useSystem) {
            colorD.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        } else {
            colorD.setColor(porterDuffModeMultiply(srcColor, color));
        }
        imageView.setImageDrawable(colorD);
    }

    private static int porterDuffModeMultiply(int srcColor, int dstColor) {
        int alpha = Color.alpha(srcColor) * Color.alpha(dstColor) / 255;
        int r = Color.red(srcColor) * Color.red(dstColor) / 255;
        int g = Color.green(srcColor) * Color.green(dstColor) / 255;
        int b = Color.blue(srcColor) * Color.blue(dstColor) / 255;
        return Color.argb(alpha, r, g, b);
    }

}
