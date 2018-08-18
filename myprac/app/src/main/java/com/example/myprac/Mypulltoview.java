package com.example.myprac;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class Mypulltoview extends ViewGroup {
    Scroller scroller;
    int mylastmove = 0;
    int myDown = 0;
    int mymove = 0;
    boolean isfirst = true;
    RefreshListener listener;
    int symbolline = 0;

    public void setListener(RefreshListener listener) {
        this.listener = listener;
    }


    public Mypulltoview(Context context) {
        super(context);
        scroller = new Scroller(context);
    }

    public Mypulltoview(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    public Mypulltoview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View view = getChildAt(0);
        view.layout(0, -view.getMeasuredHeight(), view.getMeasuredWidth(), 0);
        View view1 = getChildAt(1);
        view1.layout(0, 0, view1.getMeasuredWidth(), view1.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                myDown = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mymove = (int) ev.getRawY();
                boolean a = getChildAt(1).canScrollVertically(-1);
                Log.d("tag", "move");
                if (myDown - mymove < 0 && !a) {
                    //Log.d("tag",getChildAt(1).getTop()+"");
                    mylastmove=mymove;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("tag", "释放");
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mymove = (int) event.getRawY();
                int scrolly = mylastmove - mymove;
                symbolline += Math.abs(scrolly / 2);
                Log.d("tag", symbolline + "滑动距离");
                Log.d("tag", getChildAt(0).getMeasuredHeight() + "第一个view高度");
                scrollBy(0, scrolly / 2);
                if (symbolline > getChildAt(0).getMeasuredHeight() && isfirst) {
                    listener.startRefresh();
                    isfirst = false;
                }
                mylastmove = mymove;
                break;
            case MotionEvent.ACTION_UP:
                if (!isfirst) {
                    scroller.startScroll(0, getScrollY(), 0, -getScrollY() - getChildAt(0).getMeasuredHeight());
                    isfirst = true;
                    listener.startUpdate();
                } else {
                    scroller.startScroll(0, getScrollY(), 0, -getScrollY());
                }
                Log.d("tag", "up" + getScrollY());
                symbolline = 0;
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public void success() {
        scroller.startScroll(0, getScrollY(), 0, -getScrollY());
        Log.d("tag", "up" + getScrollY());
        invalidate();
    }
}
