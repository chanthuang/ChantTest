package com.chant.chanttest.font;

import android.graphics.Typeface;
import android.util.Log;

import com.chant.chanttest.ChantTestApplication;

public class FontUtil {

    public static Typeface SourceHanSerifCNMedium;

    static {
        try {
            Typeface tmp = Typeface.createFromAsset(ChantTestApplication.sharedInstance().getAssets(), "SourceHanSerifCN-Medium.otf");
            SourceHanSerifCNMedium = Typeface.create(tmp, Typeface.NORMAL);
        } catch (Exception e) {
            Log.i("chant", e.getMessage());
        }
    }

    public static Typeface SourceHanSerifCNMedium_Modify;

    static {
        try {
            Typeface tmp = Typeface.createFromAsset(ChantTestApplication.sharedInstance().getAssets(), "SourceHanSerifCN-Medium_FDK_motify.otf");
            SourceHanSerifCNMedium_Modify = Typeface.create(tmp, Typeface.NORMAL);
        } catch (Exception e) {
            Log.i("chant", e.getMessage());
        }
    }

}
