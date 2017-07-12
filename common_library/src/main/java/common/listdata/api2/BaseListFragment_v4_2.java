package common.listdata.api2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import common.base.BaseFragment_v4;
import common.base.BasicRecyclerAdapter;
import common.base.IViewControl;
import common.listdata.impl2.FragmentListHelperImpl2;
import common.ui.datacontent.SimpleGlobalFrameLayout2;
import common.utils.LoadDataConfig;


/**
 * Created by cxw on 2016/9/24.
 */
public abstract class BaseListFragment_v4_2<Model,Holder extends RecyclerView.ViewHolder, Adapter extends BasicRecyclerAdapter<Model,Holder>>
        extends BaseFragment_v4 implements IViewControl, IViewListHelper2 {
    private IFragmentListHelper2 iFragmentListHelper2 = new FragmentListHelperImpl2<Model,Holder, Adapter>() {
        /**
         * 回调函数:通知client端刷新列表
         */
        @Override
        public void onRefresh() {
            BaseListFragment_v4_2.this.onRefresh();
        }

        /**
         * 回调函数:通知client端加载更多数据
         * @param pageNum
         */
        @Override
        public void onLoadMore(int pageNum) {
            BaseListFragment_v4_2.this.onLoadMore(pageNum);
        }

        /**
         * 回调函数:当点击空数据页面的按钮时触发
         */
        @Override
        public void onClickEmptyBtn() {
            BaseListFragment_v4_2.this.onClickEmptyBtn();
        }

        /**
         * 钩子函数:从子类获取Class对象
         * @return
         */
        @Override
        public Class<?> getViewClass() {
            return BaseListFragment_v4_2.this.getViewClass();
        }

        /**
         * 从client端获取首页页码(默认为0)
         *
         * @return
         */
        @Override
        public int getFirstPage() {
            return BaseListFragment_v4_2.this.getFirstPage();
        }

        /**
         * 从client端获取每页最大数据量
         *
         * @return
         */
        @Override
        public int getPageSize() {
            return BaseListFragment_v4_2.this.getPageSize();
        }

        /**
         * 是否由client端决定是否为最后一页
         *
         * @return
         */
        @Override
        public boolean isLastPageDependClient() {
            return BaseListFragment_v4_2.this.isLastPageDependClient();
        }

        /**
         * 是否禁用加载更多功能
         *
         * @return
         */
        @Override
        public boolean isDisableLoadMore() {
            return BaseListFragment_v4_2.this.isDisableLoadMore();
        }

        /**
         * 是否自定义分割线
         * @return
         */
        @Override
        public boolean isCustomItemDecoration() {
            return BaseListFragment_v4_2.this.isCustomItemDecoration();
        }

        /**
         * 是否为最后一页
         *
         * @return
         */
        @Override
        public boolean isLastPage() {
            return BaseListFragment_v4_2.this.isLastPage();
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
            return BaseListFragment_v4_2.this.getView(id, clazz);
        }

        @Override
        public Context getContext() {
            return BaseListFragment_v4_2.this.getContext();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iFragmentListHelper2.initListData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        iFragmentListHelper2.initWidgets(rootView);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 更新列表数据
     *
     * @param dataList
     */
    @Override
    public void updateData(List dataList) {
        iFragmentListHelper2.updateData(dataList);
    }

    /**
     * 更新列表空数据
     */
    @Override
    public void updateEmptyData() {
        iFragmentListHelper2.updateEmptyData();
    }

    /**
     * 显示加载失败页面
     */
    @Override
    public void showFailView() {
        iFragmentListHelper2.updateEmptyData();
    }

    /**
     * 初始化列表数据
     */
    @Override
    public void initListData() {
        iFragmentListHelper2.initListData();
    }

    /**
     * 钩子函数:从子类获取Class对象
     *
     * @return
     */
    @Override
    public Class<?> getViewClass() {
        return BaseListFragment_v4_2.this.getClass();
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
     * 是否禁用加载更多功能
     * @return
     */
    @Override
    public boolean isDisableLoadMore() {
        return false;
    }

    /**
     * 是否自定义分割线
     * @return
     */
    @Override
    public boolean isCustomItemDecoration() {
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
        return (Adapter) iFragmentListHelper2.getAdapter();
    }

    /**
     * 获取当前页码
     *
     * @return
     */
    @Override
    public int getPageNum() {
        return iFragmentListHelper2.getPageNum();
    }

    @Override
    public void resetPageNum() {
        iFragmentListHelper2.resetPageNum();
    }

    /**
     * 获取全部集合数据
     *
     * @return
     */
    @Override
    public List<Model> getAllDataList() {
        return iFragmentListHelper2.getAllDataList();
    }

    /**
     * 获取简单的空页面封装类
     *
     * @return
     */
    public SimpleGlobalFrameLayout2 getSimpleGlobalFrameLayout2() {
        return iFragmentListHelper2.getSimpleGlobalFrameLayout2();
    }

    /**
     * 获取RecyclerView
     * @return
     */
    @Override
    public RecyclerView getRecyclerView() {
        return iFragmentListHelper2.getRecyclerView();
    }

    /**
     * 回调函数:当点击空数据页面的按钮时触发
     */
    @Override
    public void onClickEmptyBtn() {

    }
}
