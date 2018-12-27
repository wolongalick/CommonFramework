package common.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.common.R;

import java.util.ArrayList;
import java.util.List;

import common.utils.ScreenUtils;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/1/16
 * 备注:
 */
public class CustomDialog {

    private Context context;
    private Dialog dialog;
    private View rootView;
    private RecyclerView rv_Options;
    private DialogAdapter dialogAdapter;
    private List<DialogModel> dialogModels;

    public CustomDialog(Context context) {
        this.context = context;
        dialog=new Dialog(context);
        rootView= LayoutInflater.from(context).inflate(R.layout.dialog_style1,null);
        rv_Options=rootView.findViewById(R.id.rv_Options);
        rv_Options.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        rootView.setMinimumWidth((int)((float)ScreenUtils.getScreenWidth(context)*3/4));

        dialogModels=new ArrayList<>();
        dialogAdapter=new DialogAdapter(dialogModels);
        rv_Options.setAdapter(dialogAdapter);

        dialog.setContentView(rootView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void setOptions(String[] options){
        dialogModels.clear();
        for (String option:options) {
            dialogModels.add(new DialogModel(option));
        }
    }

    public void showDialog(){
        dialogAdapter.notifyDataSetChanged();
        dialog.show();
    }


}
