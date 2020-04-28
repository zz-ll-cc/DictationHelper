package cn.edu.hebtu.software.listendemo.credit.component;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.edu.hebtu.software.listendemo.credit.Utils.Utils;
import cn.edu.hebtu.software.listendemo.credit.interf.CalendarViewAdapter;


//处理父布局(CoordinatorLayout)滑动手势
public class MonthPagerBehavior extends CoordinatorLayout.Behavior<MonthPager> {
    private int top = 0;
    private int touchSlop = 1;
    private int offsetY = 0;

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MonthPager child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, MonthPager child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        child.offsetTopAndBottom(top);
        return true;
    }

    private float downX, downY, lastY, lastTop;
    private boolean isVerticalScroll;
    private boolean directionUpa;

   // 类似于View的onTouchEvent()方法，可以在里面具体处理触摸事件的逻辑。
   /* @param parent 分发此次事件的CoordinatorLayout
    * @param child 和该Behavior关联的View
    * @param ev the 触摸事件
    * @return true表示自己消费掉了事件，就不会往后传递事件了。fasle表示自己不消费事件，默认返回false
    */
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, MonthPager child, MotionEvent ev) {
        if (downY > lastTop) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isVerticalScroll) {
                    if (ev.getY() > lastY) {
                        Utils.setScrollToBottom(true);
                        directionUpa = false;
                    } else {
                        Utils.setScrollToBottom(false);
                        directionUpa = true;
                    }

                    if (lastTop < child.getViewHeight() / 2 + child.getCellHeight() / 2) {
                        //这里表示本来是收缩状态
                        if (ev.getY() - downY <= 0 || Utils.loadTop() >= child.getViewHeight()) {
                            //向上滑或者已展开了
                            lastY = ev.getY();
                            return true;
                        }
                        if (ev.getY() - downY + child.getCellHeight() >= child.getViewHeight()) {
                            //将要滑过头了
                            saveTop(child.getViewHeight());
                            Utils.scrollTo(parent, (RecyclerView) parent.getChildAt(1), child.getViewHeight(), 10);
                            isVerticalScroll = false;
                        } else {
                            //正常下滑
                            saveTop((int) (child.getCellHeight() + ((ev.getY() - downY))));
                            Utils.scroll(parent.getChildAt(1), (int) (lastY - ev.getY()),
                                    child.getCellHeight(), child.getViewHeight());
                        }
                    } else {
                        if (ev.getY() - downY >= 0 || Utils.loadTop() <= child.getCellHeight()) {
                            lastY = ev.getY();
                            return true;
                        }

                        if (ev.getY() - downY + child.getViewHeight() <= child.getCellHeight()) {
                            //将要滑过头了
                            saveTop(child.getCellHeight());
                            Utils.scrollTo(parent, (RecyclerView) parent.getChildAt(1), child.getCellHeight(), 10);
                            isVerticalScroll = false;
                        } else {
                            //正常上滑
                            saveTop((int) (child.getViewHeight() + ((ev.getY() - downY))));
                            Utils.scroll(parent.getChildAt(1), (int) (lastY - ev.getY()),
                                    child.getCellHeight(), child.getViewHeight());
                        }
                    }

                    lastY = ev.getY();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isVerticalScroll) {

                    child.setScrollable(true);

                    CalendarViewAdapter calendarViewAdapter =
                            (CalendarViewAdapter) child.getAdapter();
                    if (calendarViewAdapter != null) {
                        if (directionUpa) {
                            Utils.setScrollToBottom(true);
                            calendarViewAdapter.switchToWeek(child.getRowIndex());
                            Utils.scrollTo(parent, (RecyclerView) parent.getChildAt(1), child.getCellHeight(), 300);
                        } else {
                            Utils.setScrollToBottom(false);
                            calendarViewAdapter.switchToMonth();
                            Utils.scrollTo(parent, (RecyclerView) parent.getChildAt(1), child.getViewHeight(), 300);
                        }
                    }

                    isVerticalScroll = false;
                    return true;
                }
                break;
        }
        return false;
    }

    private void saveTop(int top) { Utils.saveTop(top); }

    // 这个是在有触摸事件产生的时候，由CoordinatorLayout分发过来。由我们自己决定是否拦截。
    // 类似ViewGroup的onInterceptTouchEvent()方法。
    /* @param parent 分发此次事件的CoordinatorLayout
    * @param child 和该Behavior关联的View
    * @param ev the 触摸事件
     * @return true:表示要拦截事件，就将后续事件分发给onTouchEvent方法进行处理,fasle表示不进行拦截，默认返回false
     */
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, MonthPager child, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                lastTop = Utils.loadTop();
                lastY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (downY > lastTop) {
                    return false;
                }
                if (Math.abs(ev.getY() - downY) > 25 && Math.abs(ev.getX() - downX) <= 25
                        && !isVerticalScroll) {
                    isVerticalScroll = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isVerticalScroll) {
                    isVerticalScroll = false;
                    return true;
                }
                break;
        }
        return isVerticalScroll;
    }

    private int dependentViewTop = -1;

    /**
    * 在layoutDependsOn()方法产生关联(返回true)后，dependency的大小、位置等属性有变化，就会回调该方法。我们可以在这里进行相应的处理。比如跟随dependency上移而上移。
     * @param parent
    * @param child
    * @param dependency 所依赖的View
    * @return 如果child做出了相应的改变，返回true，否则返回false
    */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, MonthPager child, View dependency) {
        CalendarViewAdapter calendarViewAdapter = (CalendarViewAdapter) child.getAdapter();
        //dependency对其依赖的view(本例依赖的view是RecycleView)
        if (dependentViewTop != -1) {
            int dy = dependency.getTop() - dependentViewTop;
            int top = child.getTop();
            if (dy > touchSlop) {
                calendarViewAdapter.switchToMonth();
            } else if (dy < -touchSlop) {
                calendarViewAdapter.switchToWeek(child.getRowIndex());
            }

            if (dy > -top) {
                dy = -top;
            }

            if (dy < -top - child.getTopMovableDistance()) {
                dy = -top - child.getTopMovableDistance();
            }

            child.offsetTopAndBottom(dy);
            Log.e("ldf", "onDependentViewChanged = " + dy);

        }

        dependentViewTop = dependency.getTop();
        top = child.getTop();

        if (offsetY > child.getCellHeight()) {
            calendarViewAdapter.switchToMonth();
        }
        if (offsetY < -child.getCellHeight()) {
            calendarViewAdapter.switchToWeek(child.getRowIndex());
        }

        if (dependentViewTop > child.getCellHeight() - 24
                && dependentViewTop < child.getCellHeight() + 24
                && top > -touchSlop - child.getTopMovableDistance()
                && top < touchSlop - child.getTopMovableDistance()) {
            Utils.setScrollToBottom(true);
            calendarViewAdapter.switchToWeek(child.getRowIndex());
            offsetY = 0;
        }
        if (dependentViewTop > child.getViewHeight() - 24
                && dependentViewTop < child.getViewHeight() + 24
                && top < touchSlop
                && top > -touchSlop) {
            Utils.setScrollToBottom(false);
            calendarViewAdapter.switchToMonth();
            offsetY = 0;
        }

        return true;
        // TODO: 16/12/8 dy为负时表示向上滑动，dy为正时表示向下滑动，dy为零时表示滑动停止
    }
}
