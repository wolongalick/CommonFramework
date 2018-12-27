package common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import static common.ui.LoadMoreScrollView.OnScrollListener.SCROLL_STATE_FLING;
import static common.ui.LoadMoreScrollView.OnScrollListener.SCROLL_STATE_IDLE;
import static common.ui.LoadMoreScrollView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;


public class LoadMoreScrollView extends ScrollView {
    private int flag = 0;    //并发控制标志位

    private OnLoadMoreListener onLoadMoreListener;

    private OnLPullListener onLPullListener;

    private OnScrollChangedListener onScrollChanged;

    public LoadMoreScrollView(Context context) {
        super(context);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadingBegin() {
        flag = 1;
    }

    //listview加载完毕，将并发控制符置为0
    public void loadingComplete() {
        flag = 0;
    }

    public void noMoreData() {
        flag = 2;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        /*用来监听加载更多的-begin*/
        if (onScrollChanged != null) {
            onScrollChanged.onScrollChanged(l, t, oldl, oldt);
        }

        View view = this.getChildAt(0);
        //如果scrollview滑动到底部并且并发控制符为0，回调接口向服务器端请求数据
        if (this.getHeight() + this.getScrollY() == view.getHeight() && flag == 0) {
            flag = 1;//一进来就将并发控制符置为1，虽然onScrollChanged执行多次，但是由于并发控制符的值为1，不满足条件就不会执行到这
            onLoadMoreListener.onLoadMore();
        }
        /*用来监听加载更多的-end*/


        /*用来监听滑动停止的-begin*/
        if (mIsTouched) {
            setScrollState(SCROLL_STATE_TOUCH_SCROLL);
        } else {
            setScrollState(SCROLL_STATE_FLING);
            restartCheckStopTiming();
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(this, mIsTouched, l, t, oldl, oldt);
        }
        /*用来监听滑动停止的-end*/
    }

    private boolean isPulling;

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY < 0 && scrollY == 0 && isTouchEvent) {
            isPulling = true;
        } else {
            isPulling = false;
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        handleUpEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //如果正在从顶部往下拉,且松手了,则触发阻尼下拉
                if (isPulling && onLPullListener != null) {
                    isPulling = false;
                    onLPullListener.onPull();
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }


    public interface OnLPullListener {
        void onPull();
    }

    public void setOnLPullListener(OnLPullListener onLPullListener) {
        this.onLPullListener = onLPullListener;
    }

    public void setOnScrollChanged(OnScrollChangedListener onScrollChanged) {
        this.onScrollChanged = onScrollChanged;
    }

    //    =================用来监听滑动停止的接口-begin========================
    public interface OnScrollListener {

        /**
         * The view is not scrolling. Note navigating the list using the trackball counts as
         * being in the idle state since these transitions are not animated.
         */
        int SCROLL_STATE_IDLE = 0;

        /**
         * The user is scrolling using touch, and their finger is still on the screen
         */
        int SCROLL_STATE_TOUCH_SCROLL = 1;

        /**
         * The user had previously been scrolling using touch and had performed a fling. The
         * animation is now coasting to a stop
         */
        int SCROLL_STATE_FLING = 2;

        void onScrollStateChanged(LoadMoreScrollView view, int scrollState);

        void onScroll(LoadMoreScrollView view, boolean isTouchScroll, int l, int t, int oldl, int oldt);
    }

    private static final boolean DEBUG = false;

    private static final int CHECK_SCROLL_STOP_DELAY_MILLIS = 80;
    private static final int MSG_SCROLL = 1;

    private boolean mIsTouched = false;
    private int mScrollState = SCROLL_STATE_IDLE;

    private OnScrollListener mOnScrollListener;

    private final Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {

        private int mLastY = Integer.MIN_VALUE;

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_SCROLL) {
                final int scrollY = getScrollY();
                if (!mIsTouched && mLastY == scrollY) {
                    mLastY = Integer.MIN_VALUE;
                    setScrollState(SCROLL_STATE_IDLE);
                } else {
                    mLastY = scrollY;
                    restartCheckStopTiming();
                }
                return true;
            }
            return false;
        }
    });

    private void restartCheckStopTiming() {
        mHandler.removeMessages(MSG_SCROLL);
        mHandler.sendEmptyMessageDelayed(MSG_SCROLL, CHECK_SCROLL_STOP_DELAY_MILLIS);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        handleDownEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    private void handleDownEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                log("handleEvent, action = " + ev.getAction());
                mIsTouched = true;
                break;
        }
    }

    private void handleUpEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                log("handleEvent, action = " + ev.getAction());
                mIsTouched = false;
                restartCheckStopTiming();
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    private void setScrollState(int state) {
        if (mScrollState != state) {
            mScrollState = state;
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(this, state);
            }
        }
    }

    private void log(String obj) {
        if (DEBUG) {
            Log.d(getClass().getSimpleName(), obj);
        }
    }
//    =================用来监听滑动停止的接口-end========================
}