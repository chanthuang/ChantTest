package com.chant.chanttest.roundbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RoundLinearLayout extends LinearLayout {

    public RoundLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        RoundButtonDrawable bg = RoundButtonDrawable.fromAttributeSet(context, attrs);
        setBackgroundDrawable(bg);
    }
}
