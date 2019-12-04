package cn.edu.hebtu.software.listendemo.Untils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * 平滑滑动
 */
public class SmoothScrollLayoutManager extends LinearLayoutManager {

    private PagerSnapHelper pagerSnapHelper;
    private RecyclerView recyclerView;

    public SmoothScrollLayoutManager(Context context) {
        super(context);
        pagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()){
            //划过1px需要的ms
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 0.1f;
            }

        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);

    }


    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        pagerSnapHelper.attachToRecyclerView(view);
        this.recyclerView = view;
    }
}
