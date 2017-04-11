package com.chant.chanttest.recyclerView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chant.chanttest.util.QMUIDisplayHelper;

public class RecyclerViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(this);
        TestLayoutManager layoutManager = new TestLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        setContentView(recyclerView);
    }

    class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }
    }

    class Adapter extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemView view = new ItemView(parent.getContext());
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            ItemView itemView = (ItemView) holder.itemView;
            itemView.render(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return 40;
        }
    }

    class ItemView extends FrameLayout {

        TextView mTextView;

        public ItemView(Context context) {
            super(context);
            int padding = QMUIDisplayHelper.dpToPx(10);
            setPadding(padding, padding, padding, padding);
            mTextView = new TextView(context);
            mTextView.setBackgroundColor(0xeeccaa);
            addView(mTextView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        void render(String text) {
            mTextView.setText(text);
        }
    }

}
