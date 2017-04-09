package com.chant.chanttest;

import android.app.Application;
import android.content.res.Resources;

import com.tencent.mm.svg.graphics.SVGResourceLoader;

public class ChantTestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SVGResourceLoader.load(this);
    }

    @Override
    public Resources getResources() {
        return ChantTestResource.getInstance(super.getResources());
    }
}
