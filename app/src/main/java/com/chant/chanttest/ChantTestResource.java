package com.chant.chanttest;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.tencent.mm.svg.graphics.SVGCompat;
import com.tencent.mm.svg.util.PFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ChantTestResource extends Resources {
    static String TAG = "ChantTestResource";

    private static ChantTestResource sInstance;

    private Resources mOriginalResources;

    private SVGCompat svgCompat;

    private ChantTestResource(Resources oriResources) {
        super(oriResources.getAssets(), oriResources.getDisplayMetrics(), oriResources.getConfiguration());
        mOriginalResources = oriResources;
        svgCompat = new SVGCompat();
        disableCMTheme(oriResources.getAssets());
    }

    public static ChantTestResource getInstance(Resources oriResources) {
        if (sInstance == null) {
            sInstance = new ChantTestResource(oriResources);
        }
        return sInstance;
    }

    // MIUI7 svg
    boolean isPreloadOverlayed(int id) {
        return false;
    }

    @Nullable
    @Override
    public Drawable getDrawable(int id, Theme theme) throws NotFoundException {
        Drawable ret = super.getDrawable(id, theme);
        if (ret == null && svgCompat.isSVGDrawable(this, id)) {
            ret = SVGCompat.getSVGDrawable(this, id);
        }
        return ret;
    }

    @Nullable
    @Override
    public Drawable getDrawable(int id) throws NotFoundException {
        Drawable ret = super.getDrawable(id);
        if (ret == null && svgCompat.isSVGDrawable(this, id)) {
            ret = SVGCompat.getSVGDrawable(this, id);
        }
        return ret;
    }

    @Nullable
    @Override
    public Drawable getDrawableForDensity(int id, int density, Theme theme) {
        Drawable ret = super.getDrawableForDensity(id, density, theme);
        if (ret == null && svgCompat.isSVGDrawable(this, id)) {
            ret = SVGCompat.getSVGDrawable(this, id);
        }
        return ret;
    }

    @Nullable
    @Override
    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
        Drawable ret = super.getDrawableForDensity(id, density);
        if (ret == null && svgCompat.isSVGDrawable(this, id)) {
            ret = SVGCompat.getSVGDrawable(this, id);
        }
        return ret;
    }

    /**
     * Disable CyanogenMod self-defined theme.
     * @param assets
     */
    private void disableCMTheme(AssetManager assets) {
        try {
            PFactory<Object> field_mThemeCookies = new PFactory<>(assets, "mThemeCookies", null);
            if (field_mThemeCookies.found()) {
                try {
                    ArrayList<Integer> cookies = (ArrayList<Integer>) field_mThemeCookies.get();
                    cookies.clear();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
//            WRLog.log(Log.ERROR, TAG, "Error on disable cm theme:" + e);
        }
    }

    private void fixMIUI7_fuckLeiJun() {
        try {
            Class<?> miuiClz = Class.forName("android.content.res.MiuiResources");
            Field f = null;
            if (miuiClz != null) {
                Log.i("wuziyi", "fuck LeiJun");
                f = miuiClz.getDeclaredField("sMiuiThemeEnabled");
                if (f != null) {
                    f.setAccessible(true);
                    f.set(null, false);
                }
            }
        } catch (ClassNotFoundException e) {

        } catch (NoSuchFieldException e) {

        } catch (IllegalAccessException e) {

        } catch (IllegalArgumentException e) {

        }
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return mOriginalResources.getDisplayMetrics();
    }

    @Override
    public Configuration getConfiguration() {
        return mOriginalResources.getConfiguration();
    }
}
