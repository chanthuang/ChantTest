package com.chant.chanttest.coordinator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);

        ListView simpleListView = new ListView(this);
        simpleListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, newList(0, 200)));
        frameLayout.addView(simpleListView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        BottomSheetLayout baseView = new BottomSheetLayout(this);
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
                textView.setText(String.valueOf(position));
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

    private List<Integer> newList(int from, int to) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = from; i < to; i++) {
            arrayList.add(i);
        }
        return arrayList;
    }
}

