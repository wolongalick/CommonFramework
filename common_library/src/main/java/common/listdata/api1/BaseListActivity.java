package common.listdata.api1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.socks.autoload.AutoLoadListView;

import java.util.List;

import common.base.BaseActivity;
import common.base.BasicAdapter;
import common.base.IViewControl;
import common.listdata.impl.ActivityListHelperImpl;
import common.ui.datacontent.SimpleGlobalFrameLayout;
import common.utils.LoadDataConfig;

/**
 * Created by Alick on 2015/10/2.
 */
public abstract class BaseListActivity<Model, Adapter extends BasicAdapter<Model>>  extends BaseActivity implements IViewControl, IActivityListHelper {

    private IActivityListHelper iActivityListHelper =new ActivityListHelperImpl<Model, Adapter>(){
        /**
         * 回调函数:通知client端刷新列表
         */
        @Override
        public void onRefresh() {
            BaseListActivity.this.onRefresh();
        }
        /**
         * 回调函数:通知client端加载更多数据
         * @param pageNum
         */
        @Override
        public void onLoadMore(int pageNum) {
            BaseListActivity.this.onLoadMore(pageNum);
        }

        /**
         * 钩子函数:从子类获取Class对象
         * @return
         */
        @Override
        public Class<?> getViewClass() {
            return BaseListActivity.this.getViewClass();
        }

        /**
         * 从client端获取首页页码(默认为0)
         *
         * @return
         */
        @Override
        public int getFirstPage() {
            return BaseListActivity.this.getFirstPage();
        }

        /**
         * 从client端获取每页最大数据量
         *
         * @return
         */
        @Override
        public int getPageSize() {
            return BaseListActivity.this.getPageSize();
        }

        /**
         * 是否由client端决定是否为最后一页
         *
         * @return
         */
        @Override
        public boolean isLastPageDependClient() {
            return BaseListActivity.this.isLastPageDependClient();
        }

        /**
         * 是否为最后一页
         *
         * @return
         */
        @Override
        public boolean isLastPage() {
            return BaseListActivity.this.isLastPage();
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
            return BaseListActivity.this.getView(id,clazz);
        }

        @Override
        public Context getContext() {
            return BaseListActivity.this.getContext();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void supplementParams(Bundle savedInstanceState) {
        iActivityListHelper.initListData();
        iActivityListHelper.initWidgets(this);
    }

    /**
     * 更新列表数据
     * @param dataList
     */
    @Override
    public void updateData(List dataList) {
        iActivityListHelper.updateData(dataList);
    }

    /**
     * 更新列表空数据
     */
    @Override
    public void updateEmptyData(){
        iActivityListHelper.updateEmptyData();
    }

    /**
     * 显示加载失败页面
     */
    @Override
    public void showFailView() {
        iActivityListHelper.updateEmptyData();
    }

    /**
     * 初始化列表数据
     */
    @Override
    public void initListData() {
        iActivityListHelper.initListData();
    }

    @Override
    public void initWidgets(Activity activity) {
        iActivityListHelper.initWidgets(activity);
    }

    /**
     * 钩子函数:从子类获取Class对象
     * @return
     */
    @Override
    public Class<?> getViewClass() {
        return BaseListActivity.this.getClass();
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
        return (Adapter) iActivityListHelper.getAdapter();
    }

    /**
     * 获取当前页码
     * @return
     */
    @Override
    public int getPageNum() {
        return iActivityListHelper.getPageNum();
    }

    @Override
    public void resetPageNum() {
        iActivityListHelper.resetPageNum();
    }

    /**
     * 获取下拉刷新控件
     * @return
     */
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return iActivityListHelper.getSwipeRefreshLayout();
    }

    /**
     * 获取全部集合数据
     * @return
     */
    @Override
    public List<Model>   getAllDataList() {
        return iActivityListHelper.getAllDataList();
    }

    /**
     * 获取简单的空页面封装类
     * @return
     */
    public SimpleGlobalFrameLayout getSimpleGlobalFrameLayout() {
        return iActivityListHelper.getSimpleGlobalFrameLayout();
    }

    /**
     * 获取上拉加载更多控件
     * @return
     */
    @Override
    public AutoLoadListView getAutoLoadListView() {
        return iActivityListHelper.getAutoLoadListView();
    }
}
