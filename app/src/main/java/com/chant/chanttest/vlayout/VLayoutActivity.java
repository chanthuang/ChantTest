package com.chant.chanttest.vlayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.LayoutViewFactory;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.BaseLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.chant.chanttest.R;

public class VLayoutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = new RecyclerView(this);
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);

        TestAdapter realAdapter = new TestAdapter();
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        for (int i = 0; i < 10; i++) {
            delegateAdapter.addAdapter(realAdapter);
        }
        recyclerView.setAdapter(delegateAdapter);

        layoutManager.setLayoutViewFactory(new LayoutViewFactory() {
            @Override
            public View generateLayoutView(@NonNull Context context) {
                return new ImageView(context);
            }
        });

        setContentView(recyclerView);
    }

    private static class TestAdapter extends DelegateAdapter.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setBackgroundColor(0x50ddccdd);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setPadding(40, 40, 40, 40);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return 40;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
//            LinearLayoutHelper layoutHelper = new LinearLayoutHelper(2, 3);
            GridLayoutHelper layoutHelper = new GridLayoutHelper(3, 2, 20);
            layoutHelper.setLayoutViewBindListener(new BaseLayoutHelper.LayoutViewBindListener() {
                @Override
                public void onBind(View layoutView, BaseLayoutHelper baseLayoutHelper) {
                    ImageView imageView = (ImageView) layoutView;
                    imageView.setImageResource(R.drawable.article_list_empty_image);
                }
            });
            layoutHelper.setLayoutViewUnBindListener(new BaseLayoutHelper.LayoutViewUnBindListener() {
                @Override
                public void onUnbind(View layoutView, BaseLayoutHelper baseLayoutHelper) {
                    ImageView imageView = (ImageView) layoutView;
                    imageView.setImageResource(0);
                }
            });
            return layoutHelper;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}

