package common.listdata.api1;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.socks.autoload.AutoLoadListView;

import java.util.List;

import common.base.BaseFragment_v4;
import common.base.BasicAdapter;
import common.base.IViewControl;
import common.listdata.impl.FragmentListHelperImpl;
import common.ui.datacontent.SimpleGlobalFrameLayout;
import common.utils.LoadDataConfig;


/**
 * Created by cxw on 2016/9/24.
 */
public abstract class BaseListFragment_v4<Model, Adapter extends BasicAdapter<Model>>
        extends BaseFragment_v4 implements IViewControl, IViewListHelper {
    private IFragmentListHelper iFragmentListHelper = new FragmentListHelperImpl<Model, Adapter>() {
        /**
         * 回调函数:通知client端刷新列表
         */
        @Override
        public void onRefresh() {
            BaseListFragment_v4.this.onRefresh();
        }

        /**
         * 回调函数:通知client端加载更多数据
         * @param pageNum
         */
        @Override
        public void onLoadMore(int pageNum) {
            BaseListFragment_v4.this.onLoadMore(pageNum);
        }

        /**
         * 钩子函数:从子类获取Class对象
         * @return
         */
        @Override
        public Class<?> getViewClass() {
            return BaseListFragment_v4.this.getViewClass();
        }

        /**
         * 从client端获取首页页码(默认为0)
         *
         * @return
         */
        @Override
        public int getFirstPage() {
            return BaseListFragment_v4.this.getFirstPage();
        }

        /**
         * 从client端获取每页最大数据量
         *
         * @return
         */
        @Override
        public int getPageSize() {
            return BaseListFragment_v4.this.getPageSize();
        }

        /**
         * 是否由client端决定是否为最后一页
         *
         * @return
         */
        @Override
        public boolean isLastPageDependClient() {
            return BaseListFragment_v4.this.isLastPageDependClient();
        }

        /**
         * 是否为最后一页
         *
         * @return
         */
        @Override
        public boolean isLastPage() {
            return BaseListFragment_v4.this.isLastPage();
        }

        /**
         * 获得视图
         *
         * @param id
         * @param clazz @return
         * @param控件 id
         */
        @Override
        public <T> T getView(int id, Class<T> clazz) {
            return BaseListFragment_v4.this.getView(id, clazz);
        }

        @Override
        public Context getContext() {
            return BaseListFragment_v4.this.getContext();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iFragmentListHelper.initListData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        iFragmentListHelper.initWidgets(rootView);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 更新列表数据
     *
     * @param dataList
     */
    @Override
    public void updateData(List dataList) {
        iFragmentListHelper.updateData(dataList);
    }

    /**
     * 更新列表空数据
     */
    @Override
    public void updateEmptyData() {
        iFragmentListHelper.updateEmptyData();
    }

    /**
     * 显示加载失败页面
     */
    @Override
    public void showFailView() {
        iFragmentListHelper.updateEmptyData();
    }

    /**
     * 初始化列表数据
     */
    @Override
    public void initListData() {
        iFragmentListHelper.initListData();
    }

    /**
     * 钩子函数:从子类获取Class对象
     *
     * @return
     */
    @Override
    public Class<?> getViewClass() {
        return BaseListFragment_v4.this.getClass();
    }

    /**
     * 从client端获取首页页码(默认为0)
     *
     * @return
     */
    @Override
    public int getFirstPage() {
        return LoadDataConfig.DEFAULT_FIRST_PAGE;
    }

    /**
     * 从client端获取每页最大数据量
     *
     * @return
     */
    @Override
    public int getPageSize() {
        return LoadDataConfig.DEFAULT_PAGE_SIZE;
    }

    /**
     * 是否由client端决定是否为最后一页
     *
     * @return
     */
    @Override
    public boolean isLastPageDependClient() {
        return false;
    }

    /**
     * 是否为最后一页
     *
     * @return
     */
    @Override
    public boolean isLastPage() {
        return false;
    }

    /**
     * 获取适配器
     *
     * @return
     */
    @Override
    public Adapter getAdapter() {
        //这里用到了向下转型
        return (Adapter) iFragmentListHelper.getAdapter();
    }

    /**
     * 获取当前页码
     *
     * @return
     */
    @Override
    public int getPageNum() {
        return iFragmentListHelper.getPageNum();
    }

    @Override
    public void resetPageNum() {
        iFragmentListHelper.resetPageNum();
    }

    /**
     * 获取下拉刷新控件
     *
     * @return
     */
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return iFragmentListHelper.getSwipeRefreshLayout();
    }

    /**
     * 获取全部集合数据
     *
     * @return
     */
    @Override
    public List<Model> getAllDataList() {
        return iFragmentListHelper.getAllDataList();
    }

    /**
     * 获取简单的空页面封装类
     *
     * @return
     */
    public SimpleGlobalFrameLayout getSimpleGlobalFrameLayout() {
        return iFragmentListHelper.getSimpleGlobalFrameLayout();
    }

    /**
     * 获取上拉加载更多控件
     *
     * @return
     */
    @Override
    public AutoLoadListView getAutoLoadListView() {
        return iFragmentListHelper.getAutoLoadListView();
    }


}
