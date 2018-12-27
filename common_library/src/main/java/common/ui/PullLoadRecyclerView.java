package common.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;

import common.ui.api.OnPullLoadListener;
import common.utils.BLog;


public class PullLoadRecyclerView extends RecyclerView {

    private static final String TAG="PullLoadRecyclerView";

    private OnPullLoadListener onPullLoadListener;
    private boolean isLoadMoring;

    private int state;
    public static final int MAYBE_HAVE_MORE_DATA =0;        //可能有更多数据
    public static final int NO_MORE_DATA_FIRST_PAGE =1;     //首页没有更多数据
    public static final int NO_MORE_DATA_NEXT_PAGE =2;      //次页没有更多数据


    private IOnLoadEndListener iOnLoadEndListener;

    private boolean isAdvanceLoadMore;                      //是否提前加载更多
    private int advanceLoadMoreIndex =2;                    //提前第几个加载更多数据

    public interface IOnLoadEndListener{
        void onLoadEnd();
    }

    public PullLoadRecyclerView(Context context) {
        super(context);
        closeDefaultAnimator();
    }

    public PullLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        closeDefaultAnimator();
    }

    public PullLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        closeDefaultAnimator();
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if(!isAdvanceLoadMore){
            if(!canScrollVertically(1)){
                tryLoadMore();
            }
        }else {
            int lastVisibleItemPosition = findLastVisibleItemPosition(getLayoutManager());
            int count=getAdapter().getItemCount();
            if(lastVisibleItemPosition!=-1){
                int diff = count - lastVisibleItemPosition;
                if(diff <= (advanceLoadMoreIndex +1)){
                    tryLoadMore();
                }
            }
        }
    }

    private void tryLoadMore() {
        if(!isLoadMoring){
            isLoadMoring=true;
            switch (state){
                case NO_MORE_DATA_FIRST_PAGE:
                    //如果在第一页就没有更多数据,则不做任何处理
                    setLoadMoreFinish();
                    break;
                case NO_MORE_DATA_NEXT_PAGE:
                    //如果在次页没有更多数据,则通知客户端UI,给用户提示
                    if(iOnLoadEndListener!=null){
                        iOnLoadEndListener.onLoadEnd();
                    }
                    setLoadMoreFinish();
                    break;
                default:
                    if(onPullLoadListener!=null){
                        onPullLoadListener.onPullLoad();
                    }
            }
        }else {
            BLog.i(TAG,"正在加载中");
        }
    }

    public void setPullLoadListener(OnPullLoadListener onPullLoadListener) {
        this.onPullLoadListener = onPullLoadListener;
    }

    public void setOnLoadEndListener(IOnLoadEndListener iOnLoadEndListener) {
        this.iOnLoadEndListener = iOnLoadEndListener;
    }

    public void setLoadMoreFinish() {
        isLoadMoring = false;
    }

    public void loadmoreFinish(int state) {
        this.state=state;
    }


    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    public void setAdvanceLoadMore(boolean isAdvanceLoadMore,int advanceLoadMoreCount) {
        this.isAdvanceLoadMore = isAdvanceLoadMore;
        this.advanceLoadMoreIndex =advanceLoadMoreCount;
    }

    /**
     * 打开默认局部刷新动画
     */
    public void openDefaultAnimator() {
        this.getItemAnimator().setAddDuration(120);
        this.getItemAnimator().setChangeDuration(250);
        this.getItemAnimator().setMoveDuration(250);
        this.getItemAnimator().setRemoveDuration(120);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(true);
    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator() {
        this.getItemAnimator().setAddDuration(0);
        this.getItemAnimator().setChangeDuration(0);
        this.getItemAnimator().setMoveDuration(0);
        this.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(true);
    }

    public void setAdvanceLoadMoreIndex(int advanceLoadMoreIndex) {
        this.advanceLoadMoreIndex = advanceLoadMoreIndex;
    }



}
