package com.chant.chanttest.transition;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chant.chanttest.R;

import static com.chant.chanttest.transition.MyFragmentActivity.FRAGMENT_CONTAINER;

public class FragmentA extends Fragment implements View.OnClickListener {

    private View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_transition_main, container, false);
        mBaseView.setOnClickListener(this);
        return mBaseView;
    }

    @Override
    public void onClick(View v) {
        FragmentB fragment = new FragmentB();

        // fragment切换动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View sharedView = mBaseView.findViewById(R.id.red_box);
            int[] position = new int[2];
            sharedView.getLocationInWindow(position);
            RevealTransition enterExitTransition = new RevealTransition(
                    new Point(position[0] + sharedView.getWidth() / 2, position[1] + sharedView.getHeight() / 2),
                    20, 2000, 300);
            fragment.setSharedElementEnterTransition(new ChangeTransform());
            setExitTransition(new Fade());
            fragment.setEnterTransition(enterExitTransition);
            fragment.setExitTransition(enterExitTransition);
            fragment.setSharedElementReturnTransition(new ChangeTransform());
        }

        // sharedView
        String tagName = fragment.getClass().getSimpleName();
        View sharedView = mBaseView.findViewById(R.id.red_box);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(FRAGMENT_CONTAINER, fragment, tagName)
                .addToBackStack(tagName)
                .addSharedElement(sharedView, ViewCompat.getTransitionName(sharedView))
                .commit();
    }
}
