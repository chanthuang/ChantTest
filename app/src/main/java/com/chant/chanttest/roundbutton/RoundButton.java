package com.chant.chanttest.roundbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class RoundButton extends TextView {

    public RoundButton(Context context) {
        super(context);
        init(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        RoundButtonDrawable bg = RoundButtonDrawable.fromAttributeSet(context, attrs);
        setBackgroundDrawable(bg);
    }
}
