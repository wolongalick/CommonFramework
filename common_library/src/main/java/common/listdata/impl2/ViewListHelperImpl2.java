package common.listdata.impl2;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import common.base.BasicRecyclerAdapter;
import common.base.ViewHelperImpl;
import common.listdata.api2.IViewListHelper2;
import common.ui.PullToRefreshLayout;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;
import common.ui.datacontent.SimpleGlobalFrameLayout2;
import common.utils.DensityUtils;
import common.utils.LoadDataConfig;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class ViewListHelperImpl2<Model, Holder extends RecyclerView.ViewHolder, Adapter extends BasicRecyclerAdapter<Model, Holder>> extends ViewHelperImpl implements IViewListHelper2<Model, Holder, Adapter> {
    private final int pageSize = getPageSize();
    private final int firstPage = getFirstPage();

    private int pageNum = LoadDataConfig.DEFAULT_FIRST_PAGE;
    private List<Model> allDataList;
    private Adapter adapter;

    private PullToRefreshLayout prl;
    protected SimpleGlobalFrameLayout2 simpleGlobalFrameLayout2;
    protected RecyclerView recyclerView;

    /**
     * 初始化列表数据
     */
    @Override
    public void initListData() {
        Class<Adapter> entityClass = (Class<Adapter>) ((ParameterizedType) getViewClass().getGenericSuperclass()).getActualTypeArguments()[2];
        Constructor<?>[] constructors = entityClass.getConstructors();
        try {
            adapter = (Adapter) constructors[0].newInstance(allDataList);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected void initListener() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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

        simpleGlobalFrameLayout2.setOnClickEmptyLayoutListener(new IEmptyLayout.OnClickEmptyLayoutListener() {
            @Override
            public void onClick(View v) {
                onClickEmptyBtn();
            }
        });

        prl = PullToRefreshLayout.supportPull(recyclerView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pageNum = firstPage;
                //明确指出:调用本类(实际是子类)的onRefresh()方法
                ViewListHelperImpl2.this.onRefresh();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                pageNum++;
                ViewListHelperImpl2.this.onLoadMore(pageNum);
            }
        });
        prl.setDoPullDown(true);
        prl.setDoPullUp(!isDisableLoadMore());
    }

    /**
     * 添加默认分割线
     */
    private void addDefaultItemDecoration() {
        final int dividerLineHeight = DensityUtils.dp2px(getContext(),0.5f);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.set(0, dividerLineHeight, 0, dividerLineHeight);
                } else {
                    outRect.set(0, 0, 0, dividerLineHeight);
                }
            }
        });
    }


    /**
     * 更新列表数据
     *
     * @param dataList
     */
    @Override
    public void updateData(List<Model> dataList) {
        int size = dataList != null ? dataList.size() : 0;
        //1.下拉刷新
        if (pageNum == firstPage) {
            //设置刷新成功
            prl.refreshFinish(PullToRefreshLayout.SUCCEED);
            if (size == 0) {
                //1.1.无数据显示空页面
                showEmptyView();
            } else {
//                allDataList.clear();
//                allDataList.addAll(dataList);
                allDataList = dataList;
                updateListView(true, size);
            }
            //2.加载更多
        } else {
            allDataList.addAll(dataList);
            updateListView(false, size);
        }
    }

    /**
     * 更新列表空数据
     */
    @Override
    public void updateEmptyData() {
        //1.下拉刷新
        if (pageNum == firstPage) {
            prl.refreshFinish(PullToRefreshLayout.FAIL);
            if (allDataList == null) {
                showFailView();
            } else if (allDataList.isEmpty()) {
                //1.1.无数据显示空页面
                showEmptyView();
            } else {
                showRealView();
            }
            //2.加载更多
        } else {
            prl.loadmoreFinish(PullToRefreshLayout.FAIL);
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

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 更新列表
     *
     * @param isRefresh
     * @param size
     */
    private void updateListView(boolean isRefresh, int size) {
        adapter.setData(allDataList);
        adapter.notifyDataSetChanged();

        //是否由client端决定是否为最后一页
        if (isLastPageDependClient()) {
            //1.是由client端决定
            if (isLastPage()) {
                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
            } else {
                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                pageNum++;
            }
        } else {
            //2.不是由client端决定,那么采用默认逻辑:当次获取的数据量小于请求的量,则认为是最后一页
            if (size < pageSize) {
                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
            } else {
                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                pageNum++;
            }
        }

        if (isRefresh) {
            showRealView();
        } else {
            showRealViewWithoutAnimation();
        }
    }
}
