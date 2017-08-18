package com.chant.chanttest.font;

import android.graphics.Typeface;
import android.util.Log;

import com.chant.chanttest.ChantTestApplication;

public class FontUtil {

    public static Typeface Normal;

    static {
        try {
            Typeface tmp = Typeface.createFromAsset(ChantTestApplication.sharedInstance().getAssets(), "SourceHanSansCN-Normal.otf");
            Normal = Typeface.create(tmp, Typeface.NORMAL);
        } catch (Exception e) {
            Log.i("chant", e.getMessage());
        }
    }

    public static Typeface Regular;

    static {
        try {
            Typeface tmp = Typeface.createFromAsset(ChantTestApplication.sharedInstance().getAssets(), "SourceHanSansCN-Regular.otf");
            Regular = Typeface.create(tmp, Typeface.NORMAL);
        } catch (Exception e) {
            Log.i("chant", e.getMessage());
        }
    }

}
