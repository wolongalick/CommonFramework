package common.listdata.impl;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.common.R;
import com.socks.autoload.AutoLoadListView;
import com.socks.autoload.LoadingFooter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import common.base.BasicAdapter;
import common.base.ViewHelperImpl;
import common.listdata.api1.IViewListHelper;
import common.ui.datacontent.IFailLayout;
import common.ui.datacontent.SimpleGlobalFrameLayout;
import common.utils.LoadDataConfig;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class ViewListHelperImpl<Model, Adapter extends BasicAdapter<Model>> extends ViewHelperImpl implements IViewListHelper<Model, Adapter> {
//    public final static int DEFAULT_FIRST_PAGE=1;//默认首页索引
//    public final static int DEFAULT_PAGE_SIZE=20;//默认每页最多数据量
    private final int pageSize = getPageSize();
    private final int firstPage=getFirstPage();

    private int pageNum = LoadDataConfig.DEFAULT_FIRST_PAGE;
    private List<Model> allDataList;
    private Adapter adapter;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected AutoLoadListView autoLoadListView;
    protected SimpleGlobalFrameLayout simpleGlobalFrameLayout;


    /**
     * 初始化列表数据
     */
    @Override
    public void initListData(){
//        allDataList = new ArrayList<>();
        Class <Adapter> entityClass = (Class <Adapter>) ((ParameterizedType) getViewClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Constructor<?>[] constructors = entityClass.getConstructors();
        try {
            adapter = (Adapter) constructors[0].newInstance(getContext(), allDataList);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected void initListener(){
        swipeRefreshLayout= (SwipeRefreshLayout) simpleGlobalFrameLayout.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color
                .holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        autoLoadListView.setAdapter(adapter);

        simpleGlobalFrameLayout.setOnClickReloadButton(new IFailLayout.OnClickReloadButtonListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum=firstPage;
                //明确指出:调用本类(实际是子类)的onRefresh()方法
                ViewListHelperImpl.this.onRefresh();
            }
        });

        autoLoadListView.setOnLoadNextListener(new AutoLoadListView.OnLoadNextListener() {
            @Override
            public void onLoadNext() {
                pageNum++;
                onLoadMore(pageNum);
            }
        });
    }



    /**
     * 更新列表数据
     * @param dataList
     */
    @Override
    public void updateData(List<Model> dataList){
        int size = dataList!=null ? dataList.size() : 0;
        //无论此时是刷新结束还是加载更多结束,都要重置swipeRefreshLayout的位置
        swipeRefreshLayout.setRefreshing(false);
        //1.下拉刷新
        if(pageNum==firstPage){
            if(size==0){
                //1.1.无数据显示空页面
                showEmptyView();
            }else{
                allDataList=dataList;
                updateListView(true,size);
            }
        //2.加载更多
        }else {
            allDataList.addAll(dataList);
            updateListView(false,size);
        }
    }

    /**
     * 更新列表空数据
     */
    @Override
    public void updateEmptyData() {
        swipeRefreshLayout.setRefreshing(false);
        //1.下拉刷新
        if(pageNum==firstPage){
            if(allDataList==null ){
                showFailView();
            }else if(allDataList.isEmpty()){
                //1.1.无数据显示空页面
                showEmptyView();
            }else{
                showRealView();
            }
        //2.加载更多
        }else {
            autoLoadListView.setState(LoadingFooter.State.Idle);
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
        pageNum=getFirstPage();
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public List<Model> getAllDataList() {
        return allDataList;
    }

    public SimpleGlobalFrameLayout getSimpleGlobalFrameLayout() {
        return simpleGlobalFrameLayout;
    }

    @Override
    public AutoLoadListView getAutoLoadListView() {
        return autoLoadListView;
    }

    /**
     * 更新列表
     * @param isRefresh
     * @param size
     */
    private void updateListView(boolean isRefresh, int size) {
        adapter.updateAll(allDataList);

        //是否由client端决定是否为最后一页
        if(isLastPageDependClient()){
            //1.是由client端决定
            if(isLastPage()){
                autoLoadListView.setState(LoadingFooter.State.TheEnd);
            }else{
                autoLoadListView.setState(LoadingFooter.State.Idle);
            }
        }else{
            //2.不是由client端决定,那么采用默认逻辑:当次获取的数据量小于请求的量,则认为是最后一页
            if(size <pageSize){
                autoLoadListView.setState(LoadingFooter.State.TheEnd);
            }else{
                autoLoadListView.setState(LoadingFooter.State.Idle);
            }
        }

        if(isRefresh){
            showRealView();
        }else{
            showRealViewWithoutAnimation();
        }
    }
}
