package com.zyw.horrarndoo.swipeview.view.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.zyw.horrarndoo.swipeview.view.swipe.SwipeLayout.SwipeState;

/**
 * Created by Horrarndoo on 2017/3/17.
 */

public class ContentLayout extends LinearLayout {
    SwipeLayoutInterface mISwipeLayout;

    public ContentLayout(Context context) {
        super(context);
    }

    public ContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSwipeLayout(SwipeLayoutInterface iSwipeLayout) {
        this.mISwipeLayout = iSwipeLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e("ContentLayout", "-----onInterceptTouchEvent-----");
        if (mISwipeLayout.getCurrentState() == SwipeState.Close) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.e("ContentLayout", "-----onTouchEvent-----");
        if (mISwipeLayout.getCurrentState() == SwipeState.Close) {
            return super.onTouchEvent(ev);
        } else {
            if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
                mISwipeLayout.close();
            }
            return true;
        }
    }
}
