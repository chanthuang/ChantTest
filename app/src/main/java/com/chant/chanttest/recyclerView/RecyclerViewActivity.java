package com.chant.chanttest.recyclerView;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chant.chanttest.util.QMUIDisplayHelper;

public class RecyclerViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = new FrameLayout(this);

        final TestRecyclerView recyclerView = new TestRecyclerView(this);
        frameLayout.addView(recyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

//        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
//        pagerSnapHelper.attachToRecyclerView(recyclerView);

//        recyclerView.setOnScrollListener(new CenterScrollListener());

        TestLayoutManager.Builder builder = new TestLayoutManager.Builder();
        builder.setIntervalAngle(20);
        builder.setRadius(QMUIDisplayHelper.getScreenWidth(this) / 2);
        TestLayoutManager layoutManager = new TestLayoutManager(builder);
//        CircleLayoutManager layoutManager = new CircleLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        {
            Button button = new Button(this);
            button.setText("addItem");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.add();
                }
            });
            FrameLayout.LayoutParams buttonLp1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayout.addView(button, buttonLp1);
        }

        {
            Button button = new Button(this);
            button.setText("scroll to last");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.smoothScrollToPosition(
                            adapter.getItemCount() - 1
                    );
                }
            });
            FrameLayout.LayoutParams buttonLp1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonLp1.topMargin = QMUIDisplayHelper.dpToPx(40);
            frameLayout.addView(button, buttonLp1);
        }

        setContentView(frameLayout);
    }

    class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }
    }

    class Adapter extends RecyclerView.Adapter<VH> {

        private int mCount = 20;

        public void add() {
            mCount++;
            notifyDataSetChanged();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//            Log.i("chant", "[onCreateViewHolder] " + create++);
            ItemView view = new ItemView(parent.getContext());
            view.setLayoutParams(new RecyclerView.LayoutParams(QMUIDisplayHelper.dpToPx(40), QMUIDisplayHelper.dpToPx(80)));
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
//            Log.i("chant", "[onBindViewHolder] position=" + position);
            ItemView itemView = (ItemView) holder.itemView;
            itemView.render("d:" + String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    class ItemView extends FrameLayout {

        TextView mTextView;
        private String mText;
        private String mDebug = "--";

        public ItemView(Context context) {
            super(context);
            mTextView = new TextView(context);
            mTextView.setGravity(Gravity.CENTER);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(0xFFaaaaaa);
            drawable.setStroke(2, 0xFF333333);
            addView(mTextView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            this.setBackgroundDrawable(drawable);
        }

        void render(String text) {
            mText = text;
            updateText();
        }

        void setDebugText(String debug) {
            mDebug = debug;
            updateText();
        }

        void updateText() {
            mTextView.setText(mText + "\n" + mDebug);
        }

        String getText() {
            return mText;
        }

    }

}
