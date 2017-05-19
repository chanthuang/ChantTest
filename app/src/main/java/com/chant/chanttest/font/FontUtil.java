package com.chant.chanttest.font;

import android.graphics.Typeface;
import android.util.Log;

import com.chant.chanttest.ChantTestApplication;

public class FontUtil {

    public static Typeface TYPEFACE_SONG_SAN;

    static {
        try {
            Typeface tmpSongSan = Typeface.createFromAsset(ChantTestApplication.sharedInstance().getAssets(), "SourceHanSerif-Medium.ttc");
            TYPEFACE_SONG_SAN = Typeface.create(tmpSongSan, Typeface.NORMAL);
        } catch (Exception e) {
            Log.i("chant", e.getMessage());
        }
    }

}
