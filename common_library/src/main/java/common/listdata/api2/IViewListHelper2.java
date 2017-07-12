package common.listdata.api2;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.socks.autoload.AutoLoadListView;

import java.util.List;

import common.base.BasicRecyclerAdapter;
import common.base.IViewHelper;
import common.ui.datacontent.SimpleGlobalFrameLayout;
import common.ui.datacontent.SimpleGlobalFrameLayout2;

/**
 * Created by Alick on 2016/9/25.
 */

public interface IViewListHelper2<Model,Holder extends RecyclerView.ViewHolder, Adapter extends BasicRecyclerAdapter<Model, Holder>> extends IViewHelper {
    /**
     * 更新列表数据
     *
     * @param dataList
     */
    void updateData(List<Model> dataList);

    /**
     * 更新列表空数据
     */
    void updateEmptyData();

    /**
     * 初始化列表数据
     */
    void initListData();

    /**
     * 回调函数:当点击空数据页面的按钮时触发
     */
    void onClickEmptyBtn();

    /**
     * 回调函数:通知client端刷新列表
     */
    void onRefresh();

    /**
     * 回调函数:通知client端加载更多数据
     *
     * @param pageNum
     */
    void onLoadMore(int pageNum);

    /**
     * 钩子函数:从子类获取Class对象
     *
     * @return
     */
    Class<?> getViewClass();

    /**
     * 从client端获取首页页码(默认为0)
     *
     * @return
     */
    int getFirstPage();

    /**
     * 从client端获取每页最大数据量
     *
     * @return
     */
    int getPageSize();

    /**
     * 是否由client端决定是否为最后一页
     *
     * @return
     */
    boolean isLastPageDependClient();

    /**
     * 是否禁用加载更多功能
     * @return
     */
    boolean isDisableLoadMore();

    /**
     * 是否自定义分割线
     * @return
     */
    boolean isCustomItemDecoration();

    /**
     * 是否为最后一页
     *
     * @return
     */
    boolean isLastPage();

    /**
     * 获取适配器
     *
     * @return
     */
    Adapter getAdapter();

    /**
     * 获取当前页码
     *
     * @return
     */
    int getPageNum();

    /**
     * 重置页码
     */
    void resetPageNum();


    /**
     * 获取全部集合数据
     *
     * @return
     */
    List<Model> getAllDataList();

    /**
     * 获取简单的空页面封装类
     *
     * @return
     */
    SimpleGlobalFrameLayout2 getSimpleGlobalFrameLayout2();

    /**
     * 获取RecyclerView
     * @return
     */
    RecyclerView getRecyclerView();


}
