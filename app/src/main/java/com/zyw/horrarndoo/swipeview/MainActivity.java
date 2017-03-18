package com.zyw.horrarndoo.swipeview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.zyw.horrarndoo.swipeview.utils.ToastUtils;
import com.zyw.horrarndoo.swipeview.utils.UIUtils;
import com.zyw.horrarndoo.swipeview.view.swipe.SwipeAdapter;
import com.zyw.horrarndoo.swipeview.view.swipe.SwipeAdapter.OnSwipeControlListener;
import com.zyw.horrarndoo.swipeview.view.swipe.SwipeLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnSwipeControlListener {
    private ListView listView;
    private List<String> list = new ArrayList<String>();
    private SwipeLayoutManager swipeLayoutManager;
    private SwipeAdapter swipeAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            list.add("content - " + i);
        }
    }

    private void initView() {
        swipeLayoutManager = SwipeLayoutManager.getInstance();
        swipeAdapter = new SwipeAdapter(this);
        swipeAdapter.setList(list);

        listView = (ListView) findViewById(R.id.list_view);

        listView.setAdapter(swipeAdapter);
        listView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                swipeLayoutManager.closeUnCloseSwipeLayout();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        swipeAdapter.setOnSwipeControlListener(this);
    }

    @Override
    public void onOverhead(int position, String itemTitle) {
        setItemOverhead(position, itemTitle);
    }

    @Override
    public void onDelete(int position, String itemTitle) {
        removeItem(position, itemTitle);
    }

    /**
     * 设置item置顶
     *
     * @param position
     * @param itemTitle
     */
    private void setItemOverhead(int position, String itemTitle) {
        // ToastUtils.showToast("position : " + position + " overhead.");
        ToastUtils.showToast("overhead ---" + itemTitle + "--- success.");
        String newTitle = itemTitle;
        list.remove(position);//删除要置顶的item
        list.add(0, newTitle);//根据adapter传来的Title数据在list 0位置插入title字符串，达到置顶效果
        swipeAdapter.setList(list);//重新给Adapter设置list数据并更新
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(0);//listview选中第0项item
            }
        });
    }

    /**
     * 删除item
     *
     * @param position
     * @param itemTitle
     */

    private void removeItem(int position, String itemTitle) {
        //        ToastUtils.showToast("position : " + position + " delete.");
        ToastUtils.showToast("delete ---" + itemTitle + "--- success.");
        list.remove(position);
        swipeAdapter.setList(list);//重新给Adapter设置list数据并更新
    }
}
