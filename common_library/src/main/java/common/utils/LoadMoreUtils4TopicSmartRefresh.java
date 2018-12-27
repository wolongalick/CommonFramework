package common.utils;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import common.base.adapter.BasicRecyclerAdapter;
import common.base.viewholder.BaseViewHolder;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/4/15
 * 备注:
 */
public class LoadMoreUtils4TopicSmartRefresh {

    public static <Model, Holder extends BaseViewHolder> void updateOnLoadMore(
            RefreshLayout refreshLayout,
            BasicRecyclerAdapter<Model, Holder> adapter,
            List<Model> allDataList,
            List<Model> newDataList) {
        updateOnLoadMore(refreshLayout,adapter,allDataList,newDataList,false);
    }

    private static <Model, Holder extends BaseViewHolder> void updateOnLoadMore(
            RefreshLayout refreshLayout,
            BasicRecyclerAdapter<Model, Holder> adapter,
            List<Model> allDataList,
            List<Model> newDataList,boolean isDrawLastItemDecoration) {
        if (DataUtils.isEmpty(newDataList)) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            if(!DataUtils.isEmpty(allDataList)){
                adapter.notifyItemRangeChanged(allDataList.size()-1,2);
            }
        } else {
            allDataList.addAll(newDataList);
            int size = allDataList.size();
            int newSize = newDataList.size();
            adapter.notifyItemRangeChanged(size - newSize-(isDrawLastItemDecoration ? 1 : 0), newSize);

            if (newSize < LoadDataConfig.TOPIC_JOKE_LIST_PAGE_SIZE_30) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                adapter.addPageNo();
                refreshLayout.finishLoadMore();
            }
        }
    }
}
