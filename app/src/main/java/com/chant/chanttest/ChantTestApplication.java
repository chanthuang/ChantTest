package com.chant.chanttest;

import android.app.Application;
import android.content.res.Resources;

import com.tencent.mm.svg.graphics.SVGResourceLoader;

public class ChantTestApplication extends Application {

    private static ChantTestApplication _instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        SVGResourceLoader.load(this);
    }

    @Override
    public Resources getResources() {
        return ChantTestResource.getInstance(super.getResources());
    }

    public static ChantTestApplication sharedInstance() {
        if (_instance == null) {
            throw new IllegalStateException("not init");
        }
        return _instance;
    }
}
