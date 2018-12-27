package common.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.R;

import java.util.List;

import common.base.viewholder.BaseViewHolder;
import common.base.viewholder.FootViewHolder;
import common.ui.ICommonOnItemClickListener;
import common.utils.LoadDataConfig;

/**
 * 通用的RecyclerAdapter
 * Created by cxw on 2017/2/15.
 */

public abstract class BasicRecyclerAdapter<Model, Holder extends BaseViewHolder> extends RecyclerView.Adapter<Holder> {

    private List<Model> data;
    private Context context;
    private ICommonOnItemClickListener<Holder, Model> iCommonOnItemClickListener;
    private LayoutInflater layoutInflater;

    private Holder footHolder;

    private static final int TYPE_ITEM = -1;
    private static final int TYPE_FOOTER = -2;
    private static final int TYPE_HEADER = -3;
    private boolean isSupportLoadMore;
    private boolean isNoMoreData;               //是否没有过更多数据了
    private boolean isHideNoMoreDataView;       //是否隐藏"没有更多数据"的页脚

    private int pageNo = 0;
    private int pageSize= LoadDataConfig.PAGE_SIZE_10;
    private boolean isSupportHeader;

    private OnLoadMoreFinishCallback onLoadMoreFinishCallback;



    public interface OnLoadMoreFinishCallback {
        RecyclerView getRecyclerView();
    }

    public interface OnClickReLoadMoreListener{
        void onClickReLoadMore();

        RecyclerView.ViewHolder getViewHolder(int position);
    }


    public BasicRecyclerAdapter(List<Model> data) {
        this.data = data;
    }

    @Override
    public final int getItemViewType(int position) {
        if(isHeaderView(position)){
            return TYPE_HEADER;
        }else if (isFooterView(position)) {
            return TYPE_FOOTER;
        } else {
            if(isSupportHeader){
                position--;
            }
            return getItemViewTypeDelay(position);
        }
    }

    protected int getItemViewTypeDelay(int position) {
        return TYPE_ITEM;
    }

    /**
     * 此方法唯一目的就是为了从parent对象中获取context,从而不需要在子类实例化时传入context了,
     * 并用final修饰此方法,禁止子类重写本方法
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public final Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        layoutInflater = LayoutInflater.from(context);
        if(isSupportHeader && viewType==TYPE_HEADER){
            Holder headerHolder = onDelayCreateHeaderViewHolder(parent);
            if(headerHolder==null){
                headerHolder=createDefaultHeadViewHolder(parent);
            }
            return headerHolder;
        }else if (isSupportLoadMore && viewType == TYPE_FOOTER) {
            Holder footHolder = onDelayCreateFootViewHolder(parent);

            if (footHolder == null) {
                //holder为null说明子类没有重写父类的onDelayCreateFootViewHolder方法,因此需要由父类(BasicRecyclerAdapter类)自己创建默认的holder
                footHolder= createDefaultFootViewHolder(parent);
            }
            this.footHolder=footHolder;

            if(isNoMoreData){
//                noMoreData();
            }

            return footHolder;
        } else {
            return onDelayCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        if(isHeaderView(position)){
            onFillHeaderView(holder);
        }else if (!isSupportLoadMore || position < getItemCount() - 1) {
            //1.不支持加载更多时,直接按常规流程填充子类holder
            onFillViewByModel(holder, getData().get(isSupportHeader ? position-1 :position), position);
        }else {
            View ll_loadingView = holder.itemView.findViewById(R.id.ll_loading);
            View noMoreDataView = holder.itemView.findViewById(R.id.ll_noMoreData);
            if(isNoMoreData){
                //隐藏loading布局
                ll_loadingView.setVisibility(View.GONE);
                if(!isHideNoMoreDataView){
                    if(onLoadMoreFinishCallback !=null){

                        RecyclerView recyclerView = onLoadMoreFinishCallback.getRecyclerView();
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                        int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                        if(firstCompletelyVisibleItemPosition!=0){
                            noMoreDataView.setVisibility(View.VISIBLE);
                        }else {
                            noMoreDataView.setVisibility(View.GONE);
                        }
                    }
                }else {
                    noMoreDataView.setVisibility(View.GONE);
                }

                //如果需要隐藏"没有更多数据"的布局或当前布局中真实数据的数量不足一页,则隐藏"没有更多数据"布局
//                noMoreDataView.setVisibility(isHideNoMoreDataView || getRealDataCount()<pageSize ? View.GONE : View.VISIBLE);

            }else {
                ll_loadingView.setVisibility(View.VISIBLE);
                noMoreDataView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 延迟创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract Holder onDelayCreateViewHolder(ViewGroup parent, int viewType);

    public Holder onDelayCreateHeaderViewHolder(ViewGroup parent) {

        return null;
    }
    public Holder onDelayCreateFootViewHolder(ViewGroup parent) {

        return null;
    }

    private Holder createDefaultHeadViewHolder(ViewGroup parent){
        return (Holder) new FootViewHolder(layoutInflater.inflate(R.layout.item_head,parent,false));
    }

    private Holder createDefaultFootViewHolder(ViewGroup parent){
        return (Holder) new FootViewHolder(layoutInflater.inflate(R.layout.item_foot,parent,false));
    }

    /**
     * 通过数据模型填充视图
     *
     * @param holder
     * @param model
     * @param position
     */
    public abstract void onFillViewByModel(final Holder holder, final Model model, final int position);

    public void onFillHeaderView(Holder footHolder){

    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        if (isSupportHeader) {
            count++;
        }

        if (isSupportLoadMore) {
            count++;
        }
        return count;
    }

    /**
     * 获取真实内容的数量
     * @return
     */
    public int getRealDataCount() {
        return (data==null || data.size() == 0) ? 0 : data.size();
    }

    public boolean isLastItem(int position){
        return data.size()-1==position;
    }

    public List<Model> getData() {
        return data;
    }

    public void setData(List<Model> data) {
        this.data = data;
    }

    public Context getContext() {
        return context;
    }


    public void setiCommonOnItemClickListener(ICommonOnItemClickListener<Holder, Model> iCommonOnItemClickListener) {
        this.iCommonOnItemClickListener = iCommonOnItemClickListener;
    }

    protected void onItemClick(Holder holder, Model model, int position) {
        if (iCommonOnItemClickListener != null) {
            iCommonOnItemClickListener.onItemClick(holder, model, position);
        }
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void setSupportLoadMore(boolean supportLoadMore) {
        isSupportLoadMore = supportLoadMore;
    }

    public boolean isSupportLoadMore() {
        return isSupportLoadMore;
    }

    public void noMoreData(){
        isNoMoreData=true;
        if(footHolder!=null){
            footHolder.itemView.findViewById(R.id.ll_loading).setVisibility(View.GONE);
            footHolder.itemView.findViewById(R.id.ll_noMoreData).setVisibility(isHideNoMoreDataView || getRealDataCount()<pageSize ? View.GONE : View.VISIBLE);
        }
    }

    public void reset(){
        isNoMoreData=false;
        if(footHolder!=null){
            footHolder.itemView.findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
            footHolder.itemView.findViewById(R.id.ll_noMoreData).setVisibility(View.GONE);
        }
    }

    public void loadDataFail(final OnClickReLoadMoreListener onClickReLoadMoreListener){
        RecyclerView.ViewHolder footHolder=onClickReLoadMoreListener.getViewHolder(getRealDataCount());
        if(footHolder!=null){
            final View llLoading = footHolder.itemView.findViewById(R.id.ll_loading);
            llLoading.setVisibility(View.GONE);
            final View llReload = footHolder.itemView.findViewById(R.id.ll_reload);
            llReload.setVisibility(View.VISIBLE);
            llReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llReload.setVisibility(View.GONE);
                    llLoading.setVisibility(View.VISIBLE);
                    onClickReLoadMoreListener.onClickReLoadMore();
                }
            });
        }
    }

    public void loadDataSuccess(){
        if(footHolder!=null){
            footHolder.itemView.findViewById(R.id.ll_loading).setVisibility(View.GONE);
        }
    }

    public void setHideNoMoreDataView(boolean hideNoMoreDataView) {
        isHideNoMoreDataView = hideNoMoreDataView;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void addPageNo(){
        pageNo++;
    }

    public void setSupportHeader(boolean supportHeader) {
        isSupportHeader = supportHeader;
    }

    public boolean isSupportHeader() {
        return isSupportHeader;
    }

    private boolean isHeaderView(int position) {
        return isSupportHeader && position == 0;
    }

    private boolean isFooterView(int position) {
        return isSupportLoadMore && position == getItemCount() - 1;
    }

    public boolean isHideNoMoreDataView() {
        return isHideNoMoreDataView;
    }

    public void setOnLoadMoreFinishCallback(OnLoadMoreFinishCallback onLoadMoreFinishCallback) {
        this.onLoadMoreFinishCallback = onLoadMoreFinishCallback;
    }
}
