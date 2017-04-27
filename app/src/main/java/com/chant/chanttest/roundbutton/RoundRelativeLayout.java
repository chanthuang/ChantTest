package com.chant.chanttest.roundbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RoundRelativeLayout extends RelativeLayout {

    public RoundRelativeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        RoundButtonDrawable bg = RoundButtonDrawable.fromAttributeSet(context, attrs);
        setBackgroundDrawable(bg);
    }
}
