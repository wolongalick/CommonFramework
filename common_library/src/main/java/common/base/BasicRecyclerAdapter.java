package common.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import common.ui.ICommonOnItemClickListener;

/**
 * 通用的RecyclerAdapter
 * Created by cxw on 2017/2/15.
 */

public abstract class BasicRecyclerAdapter<Model, Holder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<Holder> {
    private List<Model> data;
    private Context context;
    private ICommonOnItemClickListener<Model> iCommonOnItemClickListener;
    private LayoutInflater layoutInflater;

    public BasicRecyclerAdapter(List<Model> data) {
        this.data = data;
    }

    /**
     * 此方法唯一目的就是为了从parent对象中获取context,从而不需要在子类实例化时传入context了,
     * 并用final修饰此方法,禁止子类重写本方法
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public final Holder onCreateViewHolder(ViewGroup parent, int viewType){
        context=parent.getContext();
        layoutInflater=LayoutInflater.from(context);
        return onDelayCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder,final int position){
        onFillViewByModel(holder,getData().get(position),position);
    }

    /**
     * 延迟创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    public abstract Holder onDelayCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 通过数据模型填充视图
     * @param holder
     * @param model
     * @param position
     */
    public abstract void onFillViewByModel(final Holder holder,final Model model, final int position);



    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public List<Model> getData() {
        return data;
    }

    public void setData(List<Model> data) {
        this.data = data;
    }

    public Context getContext() {
        return context;
    }


    public void setiCommonOnItemClickListener(ICommonOnItemClickListener<Model> iCommonOnItemClickListener) {
        this.iCommonOnItemClickListener = iCommonOnItemClickListener;
    }

    protected void onItemClick(Model model,int position){
        if(iCommonOnItemClickListener!=null){
            iCommonOnItemClickListener.onItemClick(model,position);
        }
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }
}
