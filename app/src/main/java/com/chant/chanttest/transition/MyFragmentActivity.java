package com.chant.chanttest.transition;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.chant.chanttest.R;

public class MyFragmentActivity extends FragmentActivity {

    public static final int FRAGMENT_CONTAINER = R.id.content_parent;
    private FrameLayout mContentParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentParent = new FrameLayout(this);
        mContentParent.setId(FRAGMENT_CONTAINER);
        setContentView(mContentParent);

        FragmentA fragment = new FragmentA();
        getSupportFragmentManager()
                .beginTransaction()
                .add(FRAGMENT_CONTAINER, fragment, fragment.getClass().getSimpleName())
//                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }


}
