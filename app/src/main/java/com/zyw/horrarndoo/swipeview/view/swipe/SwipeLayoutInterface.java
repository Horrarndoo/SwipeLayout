package com.zyw.horrarndoo.swipeview.view.swipe;

import static com.zyw.horrarndoo.swipeview.view.swipe.SwipeLayout.*;

/**
 * Created by Horrarndoo on 2017/3/17.
 * SwipeLayout公共接口
 */

public interface SwipeLayoutInterface {

    SwipeState getCurrentState();

    void open();

    void close();
}
