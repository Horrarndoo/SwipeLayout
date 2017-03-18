package com.zyw.horrarndoo.swipeview.view.swipe;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by Horrarndoo on 2017/3/16.
 */

public class SwipeLayout extends FrameLayout implements SwipeLayoutInterface {

    private View contentView;// item内容区域的view
    private View hideView;// hide区域的view
    private int hideViewHeight;// hide区域的高度
    private int hideViewWidth;// hide区域的宽度
    private int contentWidth;// content区域的宽度
    private ViewDragHelper viewDragHelper;
    private SwipeLayoutManager swipeLayoutManager;
    private GestureDetectorCompat mGestureDetector;

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //        init();
    }

    public SwipeLayout(Context context) {
        this(context, null);
        //        init();
    }

    public enum SwipeState {
        Open, Swiping, Close;
    }

    private SwipeState currentState = SwipeState.Close;// 默认是关闭状态

    private void init(Context context) {
        viewDragHelper = ViewDragHelper.create(this, callback);
        mGestureDetector = new GestureDetectorCompat(context, mOnGestureListener);
        swipeLayoutManager = SwipeLayoutManager.getInstance();
    }

    private SimpleOnGestureListener mOnGestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 当横向移动距离大于等于纵向时，返回true
            return Math.abs(distanceX) >= Math.abs(distanceY);
        }
    };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalStateException("Must 2 views in SwipeLayout");
        }
        contentView = getChildAt(0);
        hideView = getChildAt(1);
        if (contentView instanceof ContentLayout)
            ((ContentLayout) contentView).setSwipeLayout(this);
        else {
            throw new IllegalStateException("content view must be an instanceof FrontLayout");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        hideViewHeight = hideView.getMeasuredHeight();
        hideViewWidth = hideView.getMeasuredWidth();
        contentWidth = contentView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        // super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0, 0, contentWidth, hideViewHeight);
        hideView.layout(contentView.getRight(), 0, contentView.getRight()
                + hideViewWidth, hideViewHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev) & mGestureDetector.onTouchEvent(ev);
        //        Log.e("SwipeLayout", "-----onInterceptTouchEvent-----");
        return result;
    }

    private float downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //        Log.e("SwipeLayout", "-----onTouchEvent-----");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 1.获取x和y方向移动的距离
                float moveX = event.getX();
                float moveY = event.getY();
                float delatX = moveX - downX;// x方向移动的距离
                float delatY = moveY - downY;// y方向移动的距离

                if (Math.abs(delatX) > Math.abs(delatY)) {
                    // 表示移动是偏向于水平方向，那么应该SwipeLayout应该处理，请求listview不要拦截
                    this.requestDisallowInterceptTouchEvent(true);
                }

                // 更新downX，downY
                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == hideView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return hideViewWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == contentView) {
                if (left > 0)
                    left = 0;
                if (left < -hideViewWidth)
                    left = -hideViewWidth;
            } else if (child == hideView) {
                if (left > contentWidth)
                    left = contentWidth;
                if (left < (contentWidth - hideViewWidth))
                    left = contentWidth - hideViewWidth;
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == contentView) {
                // 如果手指滑动deleteView，那么也要讲横向变化量dx设置给contentView
                hideView.offsetLeftAndRight(dx);
            } else if (changedView == hideView) {
                // 如果手指滑动contentView，那么也要讲横向变化量dx设置给deleteView
                contentView.offsetLeftAndRight(dx);
            }

            //            if (changedView == contentView) {
            //                // 手动移动deleteView
            //                hideView.layout(hideView.getLeft() + dx,
            //                        hideView.getTop() + dy, hideView.getRight() + dx,
            //                        hideView.getBottom() + dy);
            //            } else if (hideView == changedView) {
            //                // 手动移动contentView
            //                contentView.layout(contentView.getLeft() + dx,
            //                        contentView.getTop() + dy, contentView.getRight() + dx,
            //                        contentView.getBottom() + dy);
            //            }
            //实时更新当前状态
            updateSwipeStates();
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //根据用户滑动速度处理开关
            //xvel: x方向滑动速度
            //yvel: y方向滑动速度
            //            Log.e("tag", "currentState = " + currentState);
            //            Log.e("tag", "xvel = " + xvel);
            if (xvel < -200 && currentState != SwipeState.Open) {
                open();
                return;
            } else if (xvel > 200 && currentState != SwipeState.Close) {
                close();
                return;
            }

            if (contentView.getLeft() < -hideViewWidth / 2) {
                // 打开
                open();
            } else {
                // 关闭
                close();
            }
        }
    };

    private void updateSwipeStates() {
        SwipeState lastSwipeState = currentState;
        SwipeState swipeState = getCurrentState();

        if (listener == null) {
            try {
                throw new Exception("please setOnSwipeStateChangeListener first!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (swipeState != currentState) {
            currentState = swipeState;
            if (currentState == SwipeState.Open) {
                listener.onOpen(this);
                // 当前的Swipelayout已经打开，需要让Manager记录
                swipeLayoutManager.add(this);
            } else if (currentState == SwipeState.Close) {
                listener.onClose(this);
                // 说明当前的SwipeLayout已经关闭，需要让Manager移除
                swipeLayoutManager.remove(this);
            } else if (currentState == SwipeState.Swiping) {
                if (lastSwipeState == SwipeState.Open) {
                    listener.onStartClose(this);
                } else if (lastSwipeState == SwipeState.Close) {
                    listener.onStartOpen(this);
                    //hideView准备显示之前，先将之前打开的的SwipeLayout全部关闭
                    swipeLayoutManager.closeUnCloseSwipeLayout();
                    swipeLayoutManager.add(this);
                }
            }
        } else {
            currentState = swipeState;
        }
    }

    /**
     * 获取当前控件状态
     *
     * @return
     */
    public SwipeState getCurrentState() {
        int left = contentView.getLeft();
        //        Log.e("tag", "contentView.getLeft() = " + left);
        //        Log.e("tag", "hideViewWidth = " + hideViewWidth);
        if (left == 0) {
            return SwipeState.Close;
        }

        if (left == -hideViewWidth) {
            return SwipeState.Open;
        }
        return SwipeState.Swiping;
    }

    @Override
    public void open() {
        open(true);
    }

    @Override
    public void close() {
        close(true);
    }

    /**
     * 打开的方法
     *
     * @param isSmooth 是否通过缓冲动画的形式设定view的位置
     */
    public void open(boolean isSmooth) {
        if (isSmooth) {
            viewDragHelper.smoothSlideViewTo(contentView, -hideViewWidth,
                    contentView.getTop());
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        } else {
            contentView.offsetLeftAndRight(-hideViewWidth);//直接偏移View的位置
            hideView.offsetLeftAndRight(-hideViewWidth);//直接偏移View的位置
            //            contentView.layout(-hideViewWidth, 0, contentWidth - hideViewWidth, hideViewHeight);//直接通过坐标摆放
            //            hideView.layout(contentView.getRight(), 0, hideViewWidth, hideViewHeight);//直接通过坐标摆放
            invalidate();
        }
    }

    /**
     * 关闭的方法
     *
     * @param isSmooth true：通过缓冲动画的形式设定view的位置
     *                 false：直接设定view的位置
     */
    public void close(boolean isSmooth) {
        if (isSmooth) {
            viewDragHelper.smoothSlideViewTo(contentView, 0, contentView.getTop());
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        } else {
            contentView.offsetLeftAndRight(hideViewWidth);
            hideView.offsetLeftAndRight(hideViewWidth);
            invalidate();
            //contentView.layout(0, 0, contentWidth, hideViewHeight);//直接通过坐标摆放
            //hideView.layout(contentView.getRight(), 0, hideViewWidth, hideViewHeight);//直接通过坐标摆放
        }
    }


    @Override
    public void computeScroll() {
        // 这里判断动画是否需要继续执行。会在View.draw(Canvas mCanvas)之前执行。
        if (viewDragHelper.continueSettling(true)) {
            // 返回true，表示动画还没执行完，需要继续执行。
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private OnSwipeStateChangeListener listener;

    public void setOnSwipeStateChangeListener(
            OnSwipeStateChangeListener listener) {
        this.listener = listener;
    }

    public View getContentView() {
        return contentView;
    }

    public interface OnSwipeStateChangeListener {
        void onOpen(SwipeLayout swipeLayout);

        void onClose(SwipeLayout swipeLayout);

        void onStartOpen(SwipeLayout swipeLayout);

        void onStartClose(SwipeLayout swipeLayout);
    }
}