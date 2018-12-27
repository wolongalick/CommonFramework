package common.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的适配器模版BasicAdapter<T>
 */
public abstract class BasicAdapter<T> extends BaseAdapter {
    public int selectPosition;
    private List<T> data;
    private LayoutInflater inflater;
    private Context context;

    public BasicAdapter(Context context, List<T> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.data == null ? 0 : this.data.size();
    }

    /**
     * 获取数据集合
     *
     * @return List<T>
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 删除全部数据
     */
    public void removeAll() {
        if (this.data != null) {
            this.data.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 根据位置删除数据中的数据项
     *
     * @param position 位置
     */
    public void remove(int position) {
        if (this.data != null && position >= 0 && position < this.data.size()) {
            this.data.remove(position);
            notifyDataSetChanged();
        }
    }

    /**
     * 更新一条数据
     *
     * @param position
     * @param data
     */
    public void update(int position, T data) {
        if (this.data == null) {
            this.data = new ArrayList<T>();
        }
        this.data.set(position, data);
        notifyDataSetChanged();
    }

    /**
     * 更新全部数据，数据是追加的
     *
     * @param data
     */
    public void updateAllAppend(List<T> data) {
        if (data == null)
            return;
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 更新全部数据
     *
     * @param data
     */
    public void updateAll(List<T> data) {
        if (data == null)
            return;
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * 更新全部数据
     *
     * @param data
     */
    public void updateAll(List<T> data, boolean isNotifyDataSetChanged) {
        if (data == null)
            return;
        this.data = data;
        if (isNotifyDataSetChanged)
            notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        return (this.data == null || position < 0 || position >= this.data.size()) ? null : this.data.get
                (position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取LayoutInflater实例
     *
     * @return
     */
    public LayoutInflater getLayoutInflater() {
        return inflater;
    }

    /**
     * 获取当前上下文
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    public T getSelectedItem() {
        return data.get(selectPosition);
    }

    public void updateItemView(T t) {

    }


}
