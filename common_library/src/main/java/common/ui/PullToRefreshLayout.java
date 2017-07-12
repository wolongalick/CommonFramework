package common.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.common.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的
 */
public class PullToRefreshLayout extends RelativeLayout {
    public static final String TAG = "PullToRefreshLayout";
    // 初始状态
    public static final int INIT = 0;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int REFRESHING = 2;
    // 释放加载
    public static final int RELEASE_TO_LOAD = 3;
    // 正在加载
    public static final int LOADING = 4;
    // 操作完毕
    public static final int DONE = 5;
    // 是否正在加载
    private boolean loading = false;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    // 当前状态
    private int state = INIT;
    // 刷新回调接口
    private OnRefreshListener mListener;
    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;
    //刷新成功，但是木有数据
    public static final int NO_DATA_SUCCEED = 101;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY;

    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
    public float pullUpY = 0;

    // 释放刷新的距离
    private float refreshDist = 200;
    // 释放加载的距离
    private float loadmoreDist = 200;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;

    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    private AnimationDrawable frameAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    // 下拉头
    private View refreshView;
    // 下拉的动画视图
    private ImageView iv_anim;

    // 实现了Pullable接口的View
    private View pullableView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;

    private int top;
    private int posi;


    // 上拉头
    private View loadmoreView;
    // 上拉的箭头
    private View pullUpView;
    // 正在加载的图标
    private View loadingView;
    // 加载结果图标
    private View loadStateImageView;
    // 加载结果：成功或失败
    private TextView loadStateTextView;

    private boolean canPullUp = true;

    private boolean doPullUp = false;

    private boolean doPullDown = false;
    private List<View> touchViewList = new ArrayList<>();

    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8
                    + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                    pullUpY = -loadmoreDist;
                    timer.cancel();
                }

            }

            if (pullDownY == 0 && pullUpY == 0) {
                changeState(INIT);
            }

            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                playing = false;
                frameAnimation.stop();
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
            }
            if (pullUpY > 0) {
                // 已完成回弹
                pullUpY = 0;
                pullUpView.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
            }

            // 刷新布局,会自动调用onLayout
            requestLayout();
        }

    };

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void loadmoreFinish(int refreshResult) {
        loading = false;
        if (state != LOADING) {
            return;
        }
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:
                // 加载成功
                getLoadStateImageView().setVisibility(View.VISIBLE);
                loadStateTextView.setText("加载成功");
                getLoadStateImageView().setBackgroundResource(R.drawable.load_succeed);
                break;
            case NO_DATA_SUCCEED:
                //加载成功但是木有数据
                getLoadStateImageView().setVisibility(View.VISIBLE);
                loadStateTextView.setText("没有更多内容");
                getLoadStateImageView().setBackgroundResource(R.drawable.load_succeed);
                break;
            case FAIL:
            default:
                // 加载失败
                getLoadStateImageView().setVisibility(View.VISIBLE);
                loadStateTextView.setText("加载失败");
                getLoadStateImageView().setBackgroundResource(R.drawable.load_failed);
                break;
        }
//        // 刷新结果停留1秒
        if (pullableView instanceof ListView && (((ListView) pullableView).getChildCount() > 0)) {
            posi = ((ListView) pullableView).getFirstVisiblePosition();
            top = ((ListView) pullableView).getChildAt(0).getTop() + pullableView.getTop();
        } else if (pullableView instanceof RecyclerView && (((RecyclerView) pullableView).getChildCount() > 0)) {
            posi = ((RecyclerView) pullableView).getChildAdapterPosition(((RecyclerView) pullableView).getChildAt(0));
            top = ((RecyclerView) pullableView).getChildAt(0).getTop() + pullableView.getTop();
        }
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                changeState(DONE);
                hide();
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public static PullToRefreshLayout supportPull(View lv, OnRefreshListener listener) {
        Activity context = (Activity) lv.getContext();
        PullToRefreshLayout v = (PullToRefreshLayout) context.getLayoutInflater().inflate(R.layout.ff_refresh_container, null, false);
        ViewGroup parent = (ViewGroup) lv.getParent();
        int index = parent.indexOfChild(lv);
        ViewGroup.LayoutParams layoutParams = lv.getLayoutParams();
        lv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        parent.removeView(lv);
        v.addView(lv, 1);
        parent.addView(v, index, layoutParams);
        v.setOnRefreshListener(listener);
        lv.setTag(R.id.ff_tag_imageLoader, v);
        return v;
    }

    private void hide() {
        timer.schedule(5);
    }

    public void addTouchView(View view) {
        if (view != null) {
            touchViewList.add(view);
        }
    }

    public void removeTouchView(View view) {
        if (touchViewList.contains(view)) {
            touchViewList.remove(view);
        }
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     *
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void refreshFinish(int refreshResult) {
        loading = false;
        if (state != REFRESHING) {
            return;
        }
        switch (refreshResult) {
            case SUCCEED:
                // 刷新成功
                break;
            case FAIL:
            default:
                // 刷新失败
                break;
        }
        changeState(INIT);
        hide();
    }

    public View getLoadStateImageView() {
        if (loadStateImageView == null) {
            refreshView = getChildAt(0);
            pullableView = getChildAt(1);
            loadmoreView = getChildAt(2);
            initView();
//            loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);
        }
        return loadStateImageView;
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT:
                // 上拉布局初始状态
                getLoadStateImageView().setVisibility(View.GONE);
                loadStateTextView.setText("上拉加载更多");
                pullUpView.clearAnimation();
                pullUpView.setVisibility(View.VISIBLE);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                break;
            case REFRESHING:
                // 正在刷新状态
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                loadStateTextView.setText("释放立即加载");
                pullUpView.startAnimation(rotateAnimation);
                break;
            case LOADING:
                // 正在加载状态
                pullUpView.clearAnimation();
                loadingView.setVisibility(View.VISIBLE);
                pullUpView.setVisibility(View.INVISIBLE);
                loadingView.startAnimation(refreshingAnimation);
                loadStateTextView.setText(R.string.loading);
                break;
            case DONE:
                // 刷新或加载完毕，啥都不做
                break;
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = doPullDown;
        canPullUp = doPullUp;
    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    boolean playing = false;

    private boolean isContainTouchView(MotionEvent ev) {
        if (touchViewList.size() > 0) {
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            int eventX = (int) ev.getRawX();
            int eventY = (int) ev.getRawY();
            if (!rect.contains(eventX, eventY)) {
                return false;
            }
            for (View touchView : touchViewList) {
                if (touchView != null && touchView.getVisibility() == View.VISIBLE) {
                    touchView.getGlobalVisibleRect(rect);
                    if (rect.contains(eventX, eventY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean downIs = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downIs = false;
                if (isContainTouchView(ev)) {
                    downIs = true;
                    return super.dispatchTouchEvent(ev);
                }
                downY = ev.getY();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (downIs) {
                    break;
                }
                if (mEvents == 0) {
                    if (pullDownY > 0 || (canPullDown(pullableView) && doPullDown && canPullDown && state != LOADING)) {
                        if (!playing) {
                            playing = true;
                            frameAnimation.start();
                        }
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
//						canPullUp = canUp;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    } else if (canPullUp(pullableView) && doPullUp && canPullUp && state != REFRESHING) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else
                    mEvents = 0;

                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
                requestLayout();
                if (pullDownY <= refreshDist && state == RELEASE_TO_REFRESH) {
                    // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                    changeState(INIT);
                }
                if (pullDownY >= refreshDist && state == INIT) {
                    // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                    changeState(RELEASE_TO_REFRESH);
                }
                // 下面是判断上拉加载的，同上，注意pullUpY是负值
                if (-pullUpY <= loadmoreDist && state == RELEASE_TO_LOAD) {
                    changeState(INIT);
                }
                if (-pullUpY >= loadmoreDist && state == INIT) {
                    changeState(RELEASE_TO_LOAD);
                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (downIs) {
                    break;
                }
                if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
                // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                {
                    isTouch = false;
                }
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);
                    // 刷新操作
                    if (mListener != null && !loading) {
                        loading = true;
                        mListener.onRefresh(this);
                    }
                } else if (state == RELEASE_TO_LOAD) {
                    changeState(LOADING);
                    // 加载操作
                    if (mListener != null && !loading) {
                        loading = true;
                        mListener.onLoadMore(this);
                    }
                }
                hide();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    public void setLoadingViewVisibility(int visibility) {
        View loadingView = findViewById(R.id.loadmore_view);
        loadingView.setVisibility(visibility);
    }

    /**
     * 是否上拉加载更多
     *
     * @param doPullUp
     */
    public void setDoPullUp(boolean doPullUp) {
        this.doPullUp = doPullUp;
        canPullUp = false;
    }

    /**
     * 是否下拉加载更多
     *
     * @param doPullDown
     */
    public void setDoPullDown(boolean doPullDown) {
        this.doPullDown = doPullDown;
        canPullDown = doPullDown;
    }

    /**
     * @author chenjing 自动模拟手指滑动的task
     */
    private class AutoRefreshAndLoadTask extends AsyncTask<Integer, Float, String> {

        @Override
        protected String doInBackground(Integer... params) {
            while (pullDownY < 4 / 3 * refreshDist) {
                pullDownY += MOVE_SPEED;
                publishProgress(pullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            changeState(REFRESHING);
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh(PullToRefreshLayout.this);
            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (pullDownY > refreshDist)
                changeState(RELEASE_TO_REFRESH);
            requestLayout();
        }

    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
        task.execute(20);
        frameAnimation.start();
    }


    private void initView(Context context) {

        frameAnimation = new AnimationDrawable();
        frameAnimation.setOneShot(false);
        int[] ids = new int[]{R.drawable.xxx01, R.drawable.xxx03, R.drawable.xxx05, R.drawable.xxx07, R.drawable.xxx09,
                R.drawable.xxx11, R.drawable.xxx13, R.drawable.xxx15, R.drawable.xxx17, R.drawable.xxx19,
                R.drawable.xxx22};

        for (int id : ids) {
            frameAnimation.addFrame(context.getResources().getDrawable(id), 150);
        }
        timer = new MyTimer(updateHandler);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void initView() {
        // 初始化下拉布局
        iv_anim = (ImageView) refreshView.findViewById(R.id.iv_anim);
        // 初始化上拉布局
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            iv_anim.setBackground(frameAnimation);
        } else {
            iv_anim.setBackgroundDrawable(frameAnimation);
        }
        // 初始化上拉布局
        pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        loadStateTextView = (TextView) loadmoreView.findViewById(R.id.loadstate_tv);
        loadingView = loadmoreView.findViewById(R.id.loading_icon);
        loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);
        int i = 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        Log.d("Test", "Test");
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            refreshView = getChildAt(0);
            pullableView = getChildAt(1);
            loadmoreView = getChildAt(2);
            isLayout = true;
            initView();
            refreshDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
            loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0).getMeasuredHeight();
        }

        int measuredHeight_pullableView = pullableView.getMeasuredHeight();
        int measuredHeight_refreshView = refreshView.getMeasuredHeight();
        int measuredHeight_loadmoreView = loadmoreView.getMeasuredHeight();


        int pullOffset = (int) (pullDownY + pullUpY);
        refreshView.layout(0, pullOffset - measuredHeight_refreshView,
                refreshView.getMeasuredWidth(), pullOffset);
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分

        //if 里面的判断不要写那么长，写的长了看不懂，代码以易读为优。拆分一下如下：
        boolean isListViewCount = (pullableView instanceof ListView && ((ListView) pullableView).getChildCount() != 0);
        boolean isRecyclerCount = (pullableView instanceof RecyclerView && ((RecyclerView) pullableView).getChildCount() != 0);

        if (state == DONE && pullDownY == 0 && (isListViewCount || isRecyclerCount)) {
            setPullOffset(pullOffset);
            isHas = true;
        } else {
            if (isHas) {
                setPullOffset(pullOffset);
                if (pullOffset == 0) {
                    isHas = false;
                }
            }
        }

        pullableView.layout(0, pullOffset, pullableView.getMeasuredWidth(),
                pullOffset + measuredHeight_pullableView);


        loadmoreView.layout(0, pullOffset + measuredHeight_pullableView,
                loadmoreView.getMeasuredWidth(),
                pullOffset + measuredHeight_pullableView + measuredHeight_loadmoreView);
    }

    private void setPullOffset(int pullOffset) {
        if (pullableView instanceof RecyclerView) {
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) pullableView).getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(posi, top - pullOffset);
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(posi, top - pullOffset);
            }
        } else {
            ((ListView) pullableView).setSelectionFromTop(posi, top - pullOffset);
        }
    }

    boolean isHas = false;

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

        /**
         * 加载操作
         */
        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);

    }

    public static boolean canPullDown(View v) {
        if (v instanceof ExpandableListView) {
            ExpandableListView vv = (ExpandableListView) v;
            if (vv.getCount() == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else if (vv.getFirstVisiblePosition() == 0 && vv.getChildAt(0).getTop() >= 0) {
                // 滑到顶部了
                return true;
            } else {
                return false;
            }
        } else if (v instanceof GridView) {
            GridView vv = (GridView) v;
            if (vv.getCount() == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else if (vv.getFirstVisiblePosition() == 0 && vv.getChildAt(0).getTop() >= 0) {
                // 滑到顶部了
                return true;
            } else
                return false;

        } else if (v instanceof ListView) {
            ListView vv = (ListView) v;
            if (vv.getCount() == 0 || vv.getChildCount() == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else if (vv.getFirstVisiblePosition() == 0 && vv.getChildCount() > 0 && vv.getChildAt(0).getTop() >= 0) {
                // 滑到ListView的顶部了
                return true;
            } else
                return false;

        } else if (v instanceof ScrollView) {
            ScrollView vv = (ScrollView) v;
            if (vv.getScrollY() == 0)
                return true;
            else
                return false;

        } else if (v instanceof WebView) {
            WebView vv = (WebView) v;

            if (vv.getScrollY() == 0)
                return true;
            else
                return false;
        } else if (v instanceof RecyclerView) {
            RecyclerView vv = (RecyclerView) v;

            if (vv.getAdapter() == null || vv.getAdapter().getItemCount() == 0 || vv.getChildCount() == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else if (vv.getChildAdapterPosition(vv.getChildAt(0)) == 0 && vv.getChildCount() > 0 && vv.getChildAt(0).getTop() >= 0) {
                // 滑到ListView的顶部了
                return true;
            } else
                return false;
        } else {
            return true;
        }

    }

    public static boolean canPullUp(View v) {
        if (v instanceof ExpandableListView) {
            ExpandableListView vv = (ExpandableListView) v;
            if (vv.getCount() == 0) {
                // 没有item的时候也可以上拉加载
                return true;
            } else if (vv.getLastVisiblePosition() == (vv.getCount() - 1)) {
                // 滑到底部了
                if (vv.getChildAt(vv.getLastVisiblePosition() - vv.getFirstVisiblePosition()) != null
                        && vv.getChildAt(vv.getLastVisiblePosition() - vv.getFirstVisiblePosition()).getBottom() <= vv.getMeasuredHeight())
                    return true;
            }
            return false;

        } else if (v instanceof GridView) {
            GridView vv = (GridView) v;

            if (vv.getCount() == 0) {
                // 没有item的时候也可以上拉加载
                return true;
            } else if (vv.getLastVisiblePosition() == (vv.getCount() - 1)) {
                // 滑到底部了
                if (vv.getChildAt(vv.getLastVisiblePosition() - vv.getFirstVisiblePosition()) != null
                        && vv.getChildAt(vv.getLastVisiblePosition() - vv.getFirstVisiblePosition()).getBottom() <= vv.getMeasuredHeight())
                    return true;
            }
            return false;

        } else if (v instanceof ListView) {
            ListView vv = (ListView) v;
            if (vv.getCount() == 0) {
                // 没有item的时候也可以上拉加载
                return true;
            } else if (vv.getLastVisiblePosition() == (vv.getCount() - 1)) {
                // 滑到底部了
                if (vv.getChildAt(vv.getLastVisiblePosition() - vv.getFirstVisiblePosition()) != null
                        && vv.getChildAt(vv.getLastVisiblePosition() - vv.getFirstVisiblePosition()).getBottom() <= vv.getMeasuredHeight())
                    return true;
            }
            return false;

        } else if (v instanceof ScrollView) {
            ScrollView vv = (ScrollView) v;
            if (vv.getScrollY() >= (vv.getChildAt(0).getHeight() - vv.getMeasuredHeight()))
                return true;
            else
                return false;

        } else if (v instanceof WebView) {
            WebView vv = (WebView) v;

            if (vv.getScrollY() >= vv.getContentHeight() * vv.getScaleY() - vv.getMeasuredHeight())
                return true;
            else
                return false;
        } else if (v instanceof RecyclerView) {
            RecyclerView vv = (RecyclerView) v;
            if (vv.getChildCount() == 0 || vv.getAdapter() == null) {
                // 没有item的时候也可以上拉加载
                return true;
            } else if (vv.getChildAdapterPosition(vv.getChildAt(vv.getChildCount() - 1)) == (vv.getAdapter().getItemCount() - 1)) {
                // 滑到底部了
                if (vv.getChildAt(vv.getChildCount() - 1) != null
                        && vv.getChildAt(vv.getChildCount() - 1).getBottom() <= vv.getMeasuredHeight())
                    return true;
            }
            return false;
        } else {
            return true;
        }
    }
}
