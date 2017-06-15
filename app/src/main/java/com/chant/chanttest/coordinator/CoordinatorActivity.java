package com.chant.chanttest.coordinator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chant.chanttest.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        customCoordinator();
        plainCoordinator();
    }

    /**
     * 自定义 CoordinatorLayout 的 behavior
     */
    private void customCoordinator() {
        FrameLayout frameLayout = new FrameLayout(this);

        ListView simpleListView = new ListView(this);
        simpleListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, newList(0, 200)));
        frameLayout.addView(simpleListView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        BottomPanelLayout baseView = new BottomPanelLayout(this);
        frameLayout.addView(baseView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RecyclerView panelRecyclerView = new RecyclerView(this);
        panelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        panelRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, null, false);
                return new ViewHolder(textView);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(String.valueOf(position + 100));
            }

            @Override
            public int getItemCount() {
                return 100;
            }

            class ViewHolder extends RecyclerView.ViewHolder {

                ViewHolder(View itemView) {
                    super(itemView);
                }
            }
        });
        baseView.setContentView(panelRecyclerView);

        setContentView(frameLayout);
    }

    /**
     * 原生 CoordinatorLayout
     */
    private void plainCoordinator() {
        CoordinatorLayout container = new CoordinatorLayout(this);

        ListView simpleListView = new ListView(this);
        simpleListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, newList(0, 200)));
        container.addView(simpleListView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RecyclerView panelRecyclerView = new RecyclerView(this);
        panelRecyclerView.setBackgroundColor(Color.LTGRAY);
        panelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        panelRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, null, false);
                return new ViewHolder(textView);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(String.valueOf(position + 100));
            }

            @Override
            public int getItemCount() {
                return 100;
            }

            class ViewHolder extends RecyclerView.ViewHolder {

                ViewHolder(View itemView) {
                    super(itemView);
                }
            }
        });
        int peekHeight = QMUIDisplayHelper.dpToPx(200);
        CoordinatorLayout.LayoutParams panelLp = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        BottomSheetBehavior<View> bottomSheetBehavior = new BottomSheetBehavior<>();
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("chant", "offset=" + slideOffset);
            }
        });
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(peekHeight);
        panelLp.setBehavior(bottomSheetBehavior);
        container.addView(panelRecyclerView, panelLp);

        setContentView(container);
    }

    private List<Integer> newList(int from, int to) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = from; i < to; i++) {
            arrayList.add(i);
        }
        return arrayList;
    }
}

