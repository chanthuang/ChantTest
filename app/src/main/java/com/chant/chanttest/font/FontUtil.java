package com.chant.chanttest.font;

import android.graphics.Typeface;
import android.util.Log;

import com.chant.chanttest.ChantTestApplication;

public class FontUtil {

    public static Typeface TYPEFACE_SOURCE_HANSERIF;

    static {
        try {
            Typeface tmp = Typeface.createFromAsset(ChantTestApplication.sharedInstance().getAssets(), "SourceHanSerif-Medium.ttc");
            TYPEFACE_SOURCE_HANSERIF = Typeface.create(tmp, Typeface.NORMAL);
        } catch (Exception e) {
            Log.i("chant", e.getMessage());
        }
    }

}
