package com.chant.chanttest.svg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.chant.chanttest.R;
import com.tencent.mm.svg.graphics.SVGCompat;

public class SVGImageView extends ImageView {

    public SVGImageView(Context context) {
        this(context, null);
    }

    public SVGImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SVGImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SVGImageView, 0, 0);
        int resId = typedArray.getResourceId(R.styleable.SVGImageView_svgSrc, 0);
        if (resId > 0) {
            try {
                Drawable d = SVGCompat.getSVGDrawable(getResources(), resId);
                setImageDrawable(d);
            } catch (Exception e) {
                Log.e("chant", e.getMessage());
            }
        }
        typedArray.recycle();
    }

}
