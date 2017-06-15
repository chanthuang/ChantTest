package com.chant.chanttest.nestedscroll.touchevent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chant.chanttest.R;
import com.chant.chanttest.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.List;

public class TouchEventActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);

        final DraggableParentView parentView = (DraggableParentView) findViewById(R.id.parentView);
        parentView.setPeekHeight(QMUIDisplayHelper.dp2px(this, 200)); // 最小高度
        parentView.setChildMaxHeight(QMUIDisplayHelper.getScreenHeight(this) - QMUIDisplayHelper.dp2px(this, 200)); // 最大高度
        parentView.setDraggingChangedListener(new DraggableParentView.DraggingChangedListener() {
            @Override
            public void onChildHeightChanged(int height, float slideOffset) {
                // 高度变化回调
                Log.i("chant", "height=" + height + ", offset=" + slideOffset);
            }
        });

        findViewById(R.id.testButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentView.getState() == DraggableParentView.STATE_COLLAPSED) {
                    parentView.setState(DraggableParentView.STATE_EXPANDED);
                } else {
                    parentView.setState(DraggableParentView.STATE_COLLAPSED);
                }
                Log.i("chant", "Button OnClick");
                Toast.makeText(TouchEventActivity.this, "onClick", Toast.LENGTH_SHORT).show();
            }
        });
        
        findViewById(R.id.testButton).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("chant", "Button LongClick");
                Toast.makeText(TouchEventActivity.this, "LongClick", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ListView listView = (ListView) findViewById(R.id.testListView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, newList(0, 100)));
    }

    private List<Integer> newList(int from, int to) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = from; i < to; i++) {
            arrayList.add(i);
        }
        return arrayList;
    }
}
