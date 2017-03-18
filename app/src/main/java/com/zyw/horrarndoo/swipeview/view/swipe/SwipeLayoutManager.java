package com.zyw.horrarndoo.swipeview.view.swipe;

import java.util.HashSet;

/**
 * Created by Horrarndoo on 2017/3/16.
 */

public class SwipeLayoutManager {
    //记录打开的SwipeLayout集合
    private HashSet<SwipeLayout> mUnClosedSwipeLayouts = new HashSet<SwipeLayout>();

    private SwipeLayoutManager() {
    }

    private static SwipeLayoutManager mInstance = new SwipeLayoutManager();

    public static SwipeLayoutManager getInstance() {
        return mInstance;
    }

    /**
     * 将一个没有关闭的SwipeLayout加入集合
     * @param layout
     */
    public void add(SwipeLayout layout) {
        mUnClosedSwipeLayouts.add(layout);
    }

    /**
     * 将一个没有关闭的SwipeLayout移出集合
     * @param layout
     */
    public void remove(SwipeLayout layout){
        mUnClosedSwipeLayouts.remove(layout);
    }

    /**
     * 关闭已经打开的SwipeLayout
     */
    public void closeUnCloseSwipeLayout() {
        if(mUnClosedSwipeLayouts.size() == 0){
            return;
        }

        for(SwipeLayout l : mUnClosedSwipeLayouts){
            l.close(true);
        }
        mUnClosedSwipeLayouts.clear();
    }

    /**
     * 关闭已经打开的SwipeLayout
     */
    public void closeUnCloseSwipeLayout(boolean isSmooth) {
        if(mUnClosedSwipeLayouts.size() == 0){
            return;
        }

        for(SwipeLayout l : mUnClosedSwipeLayouts){
            l.close(isSmooth);
        }
        mUnClosedSwipeLayouts.clear();
    }
}
