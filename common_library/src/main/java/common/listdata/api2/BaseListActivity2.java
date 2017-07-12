package common.listdata.api2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import common.base.BaseActivity;
import common.base.BasicRecyclerAdapter;
import common.base.IViewControl;
import common.listdata.impl.ViewListHelperImpl;
import common.listdata.impl2.ActivityListHelperImpl2;
import common.ui.datacontent.SimpleGlobalFrameLayout2;
import common.utils.LoadDataConfig;

/**
 * Created by Alick on 2015/10/2.
 */
public abstract class BaseListActivity2<Model,Holder extends RecyclerView.ViewHolder, Adapter extends BasicRecyclerAdapter<Model,Holder>> extends BaseActivity implements IViewControl, IActivityListHelper2 {

    private IActivityListHelper2 iActivityListHelper2 =new ActivityListHelperImpl2<Model,Holder, Adapter>(){
        /**
         * 回调函数:当点击空数据页面的按钮时触发
         */
        @Override
        public void onClickEmptyBtn() {
            BaseListActivity2.this.onClickEmptyBtn();
        }

        /**
         * 回调函数:通知client端刷新列表
         */
        @Override
        public void onRefresh() {
            BaseListActivity2.this.onRefresh();
        }

        /**
         * 回调函数:通知client端加载更多数据
         * @param pageNum
         */
        @Override
        public void onLoadMore(int pageNum) {
            BaseListActivity2.this.onLoadMore(pageNum);
        }

        /**
         * 钩子函数:从子类获取Class对象
         * @return
         */
        @Override
        public Class<?> getViewClass() {
            return BaseListActivity2.this.getViewClass();
        }

        /**
         * 从client端获取首页页码(默认为0)
         *
         * @return
         */
        @Override
        public int getFirstPage() {
            return BaseListActivity2.this.getFirstPage();
        }

        /**
         * 从client端获取每页最大数据量
         *
         * @return
         */
        @Override
        public int getPageSize() {
            return BaseListActivity2.this.getPageSize();
        }

        /**
         * 是否由client端决定是否为最后一页
         *
         * @return
         */
        @Override
        public boolean isLastPageDependClient() {
            return BaseListActivity2.this.isLastPageDependClient();
        }

        /**
         * 是否禁用加载更多功能
         *
         * @return
         */
        @Override
        public boolean isDisableLoadMore() {
            return BaseListActivity2.this.isDisableLoadMore();
        }

        /**
         * 是否自定义分割线
         *
         * @return
         */
        @Override
        public boolean isCustomItemDecoration() {
            return BaseListActivity2.this.isCustomItemDecoration();
        }

        /**
         * 是否为最后一页
         *
         * @return
         */
        @Override
        public boolean isLastPage() {
            return BaseListActivity2.this.isLastPage();
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
            return BaseListActivity2.this.getView(id,clazz);
        }

        @Override
        public Context getContext() {
            return BaseListActivity2.this.getContext();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void supplementParams(Bundle savedInstanceState) {
        iActivityListHelper2.initListData();
        iActivityListHelper2.initWidgets(this);
    }

    /**
     * 更新列表数据
     * @param dataList
     */
    @Override
    public void updateData(List dataList) {
        iActivityListHelper2.updateData(dataList);
    }

    /**
     * 更新列表空数据
     */
    @Override
    public void updateEmptyData(){
        iActivityListHelper2.updateEmptyData();
    }

    /**
     * 显示加载失败页面
     */
    @Override
    public void showFailView() {
        iActivityListHelper2.updateEmptyData();
    }

    /**
     * 初始化列表数据
     */
    @Override
    public void initListData() {
        iActivityListHelper2.initListData();
    }

    @Override
    public void initWidgets(Activity activity) {
        iActivityListHelper2.initWidgets(activity);
    }

    /**
     * 钩子函数:从子类获取Class对象
     * @return
     */
    @Override
    public Class<?> getViewClass() {
        return BaseListActivity2.this.getClass();
    }

    /**
     * 从client端获取首页页码(默认为0)
     *
     * @return
     */
    @Override
    public int getFirstPage()    {
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
     * @return
     */
    @Override
    public boolean isLastPage() {
        return false;
    }

    /**
     * 获取适配器
     * @return
     */
    @Override
    public Adapter getAdapter() {
        //这里用到了向下转型
        return (Adapter) iActivityListHelper2.getAdapter();
    }

    /**
     * 获取当前页码
     * @return
     */
    @Override
    public int getPageNum() {
        return iActivityListHelper2.getPageNum();
    }

    @Override
    public void resetPageNum() {
        iActivityListHelper2.resetPageNum();
    }


    /**
     * 获取全部集合数据
     * @return
     */
    @Override
    public List<Model>   getAllDataList() {
        return iActivityListHelper2.getAllDataList();
    }

    /**
     * 获取简单的空页面封装类
     * @return
     */
    @Override
    public SimpleGlobalFrameLayout2 getSimpleGlobalFrameLayout2() {
        return iActivityListHelper2.getSimpleGlobalFrameLayout2();
    }

    /**
     * 获取RecyclerView
     * @return
     */
    @Override
    public RecyclerView getRecyclerView() {
        return iActivityListHelper2.getRecyclerView();
    }


    /**
     * 回调函数,非必须实现:当点击空数据页面的按钮时触发
     */
    @Override
    public void onClickEmptyBtn() {

    }

}
