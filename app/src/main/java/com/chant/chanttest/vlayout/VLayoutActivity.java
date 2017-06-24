package com.chant.chanttest.vlayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.LayoutViewFactory;
import com.alibaba.android.vlayout.RecyclablePagerAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.VirtualLayoutManager.LayoutParams;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.FloatLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
//import com.alibaba.android.vlayout.layout.RangeGridLayoutHelper;
//import com.alibaba.android.vlayout.layout.RangeGridLayoutHelper.GridRangeStyle;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.chant.chanttest.R;

import java.util.LinkedList;
import java.util.List;

public class VLayoutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = new RecyclerView(this);
        setContentView(recyclerView);

        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 100);

        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        // TODO chant what does numberOfItems mean?
        // 纵向
        delegateAdapter.addAdapter(new RealAdapter(this, new LinearLayoutHelper(2, 2)));
        // Grid
        delegateAdapter.addAdapter(new RealAdapter(this, new GridLayoutHelper(2, 3)));
        // 瀑布流
        delegateAdapter.addAdapter(new RealAdapter(this, new StaggeredGridLayoutHelper(3, 20)));
        // 横向 column
        delegateAdapter.addAdapter(new RealAdapter(this, new ColumnLayoutHelper()));
        // 只有一个View
        delegateAdapter.addAdapter(new RealAdapter(this, new SingleLayoutHelper()));
        // 左边1右边n的布局,最多5个item
//        delegateAdapter.addAdapter(new RealAdapter(this, new OnePlusNLayoutHelper(2, 20, 20, 20, 100)));
        // Float(固定且可拖动) + 其他布局
        delegateAdapter.addAdapter(new RealAdapter(this, new FloatLayoutHelper()));
        delegateAdapter.addAdapter(new RealAdapter(this, new LinearLayoutHelper(2, 2)));
        // Sticky + 其他布局
        delegateAdapter.addAdapter(new RealAdapter(this, new StickyLayoutHelper(true)));
        delegateAdapter.addAdapter(new RealAdapter(this, new LinearLayoutHelper(2, 2)));

        recyclerView.setAdapter(delegateAdapter);

        layoutManager.setLayoutViewFactory(new LayoutViewFactory() {
            @Override
            public View generateLayoutView(@NonNull Context context) {
                return new ImageView(context);
            }
        });
    }

    private static class RealAdapter extends DelegateAdapter.Adapter<VH> {

        private LayoutHelper mLayoutHelper;

        public RealAdapter(Context context, LayoutHelper layoutHelper) {
            mLayoutHelper = layoutHelper;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(createTextView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.render(position);
        }

        @Override
        public int getItemCount() {
            return 40;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }
    }

    private static class VH extends RecyclerView.ViewHolder {

        private static int createTime = 0;

        public VH(View itemView) {
            super(itemView);
            createTime++;
            Log.i("chant", "createTime=" + createTime);
        }

        void render(int position) {
            ((TextView) itemView).setText(String.valueOf(position));
        }
    }

    private static TextView createTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setBackgroundColor(0x50ddccdd);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setPadding(40, 40, 40, 40);
        return textView;
    }

}

