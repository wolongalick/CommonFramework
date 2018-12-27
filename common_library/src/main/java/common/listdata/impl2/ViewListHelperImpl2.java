package common.listdata.impl2;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zeropercenthappy.decorationlibrary.NormalLLRVDecoration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import common.base.ViewHelperImpl;
import common.base.adapter.BasicRecyclerAdapter;
import common.base.viewholder.BaseViewHolder;
import common.listdata.api2.IViewListHelper2;
import common.ui.WrapContentLinearLayoutManager;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;
import common.ui.datacontent.SimpleGlobalFrameLayout2;
import common.utils.DataUtils;
import common.utils.DensityUtils;
import common.utils.LoadDataConfig;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class ViewListHelperImpl2<Model, Holder extends BaseViewHolder, Adapter extends BasicRecyclerAdapter<Model, Holder>> extends ViewHelperImpl implements IViewListHelper2<Model, Holder, Adapter> {
    private final int pageSize = getPageSize();
    private final int firstPage = getFirstPage();



    private int pageNum = LoadDataConfig.DEFAULT_FIRST_PAGE;
    private List<Model> allDataList;
    private Adapter adapter;

    protected SimpleGlobalFrameLayout2 simpleGlobalFrameLayout2;
    protected SmartRefreshLayout smartRefreshLayout;
    protected RecyclerView recyclerView;

    /**
     * 初始化列表数据
     */
    @Override
    public void initListData() {
        Type genericSuperclass = getViewClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type type = parameterizedType.getActualTypeArguments()[2];
        Class<Adapter> entityClass = (Class<Adapter>) type;
        Constructor<?>[] constructors = entityClass.getConstructors();
        try {
            allDataList=new ArrayList<>();
            adapter = (Adapter) constructors[0].newInstance(allDataList);
            adapter.setPageSize(pageSize);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected void initListener() {
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        if (!isCustomItemDecoration()) {
            //如果不需要自定义则使用默认的分割线
            addDefaultItemDecoration();
        }

        simpleGlobalFrameLayout2.setOnClickReloadButton(new IFailLayout.OnClickReloadButtonListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        simpleGlobalFrameLayout2.setOnClickEmptyLayoutListener( new IEmptyLayout.OnClickEmptyLayoutListener() {
            @Override
            public void onClick(View v) {
                onClickEmptyBtn();
            }
        });

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = firstPage;
                //明确指出:调用本类(实际是子类)的onRefresh()方法
                ViewListHelperImpl2.this.onRefresh(); //refresh data here
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ViewListHelperImpl2.this.onLoadMore(pageNum);
            }
        });
    }

    /**
     * 添加默认分割线
     */
    private void addDefaultItemDecoration() {
        recyclerView.addItemDecoration(new NormalLLRVDecoration(DensityUtils.dp2px(getContext(),12),getContext().getResources().getDrawable(R.drawable.divider_list_grey_12dp)));
    }


    /**
     * 更新列表数据
     *
     * @param dataList
     */
    @Override
    public void updateData(List<Model> dataList) {
        updateData(dataList,false);
    }

    @Override
    public void updateData(List<Model> dataList,boolean isWithoutAnimation) {
        int size = dataList != null ? dataList.size() : 0;
        //无论此时是刷新结束还是加载更多结束,都要重置swipeRefreshLayout的位置
        //1.下拉刷新
        if (pageNum == firstPage) {
            smartRefreshLayout.finishRefresh();
            //设置刷新成功
//            prl.refreshFinish(PullToRefreshLayout.SUCCEED);
            if (size == 0) {
                if(getAdapter().isSupportHeader()){
                    allDataList = dataList;
                    updateListView(true, size,isWithoutAnimation);
                }else {
                    //1.1.无数据显示空页面
                    showEmptyView();
                }
            } else {
                allDataList = dataList;
                updateListView(true, size,isWithoutAnimation);
            }

            //2.加载更多
        } else {
            allDataList.addAll(dataList);
            updateListView(false, size,isWithoutAnimation);
        }
    }

    /**
     * 更新列表空数据
     */
    @Override
    public void updateEmptyData() {
        //1.下拉刷新
        if (pageNum == firstPage) {
            smartRefreshLayout.finishRefresh();
            if (DataUtils.isEmpty(allDataList)) {
                showFailView();
            }
            //2.加载更多
        } else {
            smartRefreshLayout.finishLoadMore(LoadDataConfig.LOAD_MORE_FAIL_DELAY,false,false);
        }
    }

    @Override
    public Adapter getAdapter() {
        return adapter;
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public void resetPageNum() {
        pageNum = getFirstPage();
    }


    @Override
    public List<Model> getAllDataList() {
        return allDataList;
    }

    public SimpleGlobalFrameLayout2 getSimpleGlobalFrameLayout2() {
        return simpleGlobalFrameLayout2;
    }


    public SmartRefreshLayout getRefreshLayout() {
        return smartRefreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 更新列表
     * @param isRefresh
     * @param size
     */
    private void updateListView(boolean isRefresh, int size) {
        updateListView(isRefresh,size,false);
    }

    /**
     * 更新列表
     *
     * @param isRefresh
     * @param size
     */
    private void updateListView(boolean isRefresh, int size,boolean isWithoutAnimation) {
        adapter.setData(allDataList);
        adapter.notifyDataSetChanged();
        pageNum++;

        if(isRefresh){
            //刷新完成
            smartRefreshLayout.finishRefresh();
        }else {
            if(size < pageSize){
                //加载更多完成(没有更多数据了)
                smartRefreshLayout.finishLoadMoreWithNoMoreData();
            }else {
                //加载更多完成
                smartRefreshLayout.finishLoadMore();
            }
        }

        if(isWithoutAnimation){
            showRealViewWithoutAnimation();
        }else {
            if (isRefresh) {
                showRealView();
            } else {
                showRealViewWithoutAnimation();
            }
        }


    }
}
