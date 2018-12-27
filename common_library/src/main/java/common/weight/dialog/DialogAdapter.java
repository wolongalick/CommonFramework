package common.weight.dialog;

import android.view.ViewGroup;

import com.common.R;

import java.util.List;

import common.base.adapter.BasicRecyclerAdapter;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/1/16
 * 备注:
 */
public class DialogAdapter extends BasicRecyclerAdapter<DialogModel,DialogViewHolder>{

    public DialogAdapter(List<DialogModel> data) {
        super(data);

    }

    /**
     * 延迟创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public DialogViewHolder onDelayCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogViewHolder(getLayoutInflater().inflate(R.layout.item_dialog_option,parent,false));
    }

    /**
     * 通过数据模型填充视图
     *
     * @param holder
     * @param dialogModel
     * @param position
     */
    @Override
    public void onFillViewByModel(DialogViewHolder holder, DialogModel dialogModel, int position) {
        holder.tv_option.setText(dialogModel.getTitle());
    }
}
