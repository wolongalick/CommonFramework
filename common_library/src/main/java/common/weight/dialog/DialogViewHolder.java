package common.weight.dialog;

import android.view.View;
import android.widget.TextView;

import com.common.R;

import common.base.viewholder.BaseViewHolder;

public class DialogViewHolder extends BaseViewHolder {

    TextView tv_option;

    public DialogViewHolder(View itemView) {
        super(itemView);
        tv_option = itemView.findViewById(R.id.tv_option);
    }
}