package common.utils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import common.annotation.LoadDataOperate;
import common.base.adapter.BasicRecyclerAdapter;
import common.base.viewholder.BaseViewHolder;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/4/15
 * 备注:
 */
public class LoadMoreUtils4SmartRefresh {

    public static <Model, Holder extends BaseViewHolder> void updateOnLoadMore(
            SmartRefreshLayout refreshLayout,
            BasicRecyclerAdapter<Model, Holder> adapter,
            List<Model> allDataList,
            List<Model> newDataList) {
        updateOnLoadMore(refreshLayout,adapter,allDataList,newDataList,false,CommonConstant.LoadDataOperate.LOADMORE);
    }

    public static <Model, Holder extends BaseViewHolder> void updateOnLoadMore(
            SmartRefreshLayout refreshLayout,
            BasicRecyclerAdapter<Model, Holder> adapter,
            List<Model> allDataList,
            List<Model> newDataList,
            boolean isDrawLastItemDecoration,
            @LoadDataOperate String loadDataOperate) {


        if (DataUtils.isEmpty(newDataList)) {
            //1.无更多数据
            if(!DataUtils.isEmpty(allDataList)){
                adapter.notifyItemRangeChanged(allDataList.size()-1,2);
            }
            if(loadDataOperate.equals(CommonConstant.LoadDataOperate.LOADMORE)){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }else if(loadDataOperate.equals(CommonConstant.LoadDataOperate.PRE_LOADMORE)){
                refreshLayout.finishPreLoadMoreWithNoMoreData();
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        } else {
            //2.有更多数据
            allDataList.addAll(newDataList);
            int size = allDataList.size();
            int newSize = newDataList.size();
            adapter.notifyItemRangeChanged(size - newSize-(isDrawLastItemDecoration ? 1 : 0), newSize);

            if (newSize < LoadDataConfig.PAGE_SIZE_10) {
                if(loadDataOperate.equals(CommonConstant.LoadDataOperate.LOADMORE)){
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }else if(loadDataOperate.equals(CommonConstant.LoadDataOperate.PRE_LOADMORE)){
                    refreshLayout.finishPreLoadMoreWithNoMoreData();
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            } else {
                adapter.addPageNo();
                if(loadDataOperate.equals(CommonConstant.LoadDataOperate.LOADMORE)){
                    refreshLayout.finishLoadMore();
                }else if(loadDataOperate.equals(CommonConstant.LoadDataOperate.PRE_LOADMORE)){
                    refreshLayout.finishPreLoadMore();
                }
            }
        }
    }
}
