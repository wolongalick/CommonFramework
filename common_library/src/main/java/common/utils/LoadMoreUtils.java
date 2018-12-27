package common.utils;

import java.util.List;

import common.base.adapter.BasicRecyclerAdapter;
import common.base.viewholder.BaseViewHolder;
import common.ui.LoadMoreScrollView;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/4/15
 * 备注:
 */
public class LoadMoreUtils {

    public static <Model, Holder extends BaseViewHolder> void updateOnLoadMore(
            LoadMoreScrollView loadMoreScrollView,
            BasicRecyclerAdapter<Model, Holder> adapter,
            List<Model> allDataList,
            List<Model> newDataList) {
        updateOnLoadMore(loadMoreScrollView,adapter,allDataList,newDataList,false);
    }

    public static <Model, Holder extends BaseViewHolder> void updateOnLoadMore(
            LoadMoreScrollView loadMoreScrollView,
            BasicRecyclerAdapter<Model, Holder> adapter,
            List<Model> allDataList,
            List<Model> newDataList,boolean isDrawLastItemDecoration) {
        if (DataUtils.isEmpty(newDataList)) {
            adapter.noMoreData();
            loadMoreScrollView.noMoreData();
            if(!DataUtils.isEmpty(allDataList)){
                adapter.notifyItemRangeChanged(allDataList.size()-1,2);
            }
        } else {
            allDataList.addAll(newDataList);
            int size = allDataList.size();
            int newSize = newDataList.size();
            adapter.notifyItemRangeChanged(size - newSize-(isDrawLastItemDecoration ? 1 : 0), newSize);

            if (newSize < LoadDataConfig.PAGE_SIZE_10) {
                adapter.noMoreData();
                loadMoreScrollView.noMoreData();
            } else {
                adapter.addPageNo();
                loadMoreScrollView.loadingComplete();
            }
        }
    }
}
