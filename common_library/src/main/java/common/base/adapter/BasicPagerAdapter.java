package common.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2017/9/5
 * 备注:
 */
public abstract class BasicPagerAdapter<Model> extends PagerAdapter {
    private List<Model> data;
    private Context context;
    private int mChildCount = 0;


    public BasicPagerAdapter(List<Model> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data!=null ? data.size() : 0;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    protected abstract Object onInstantiateItem(ViewGroup container,Model model, int position);

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        context=container.getContext();
        return onInstantiateItem(container, data.get(position), position);
    }

    public Context getContext() {
        return context;
    }


    /*@Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();

    }

    @Override
    public int getItemPosition(Object object)   {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }*/




}
