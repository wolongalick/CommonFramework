package common.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.SparseArray;

public class ProgressDialogManager {
    private static SparseArray<ProgressDialog> manager;

    public static final int QUEUE_ID_ONE = 1;
    public static final int QUEUE_ID_TWO = 2;
    public static final int QUEUE_ID_THREE = 3;
    public static final int QUEUE_ID_FOUR = 4;

    static{
        manager = new SparseArray<>();
    }

    public static void showProgressDialog(Context context, int queueId){
        ProgressDialog dialog = ProgressDialog.show(context,"","加载中...",false,true);
        dialog.setCanceledOnTouchOutside(false);
        manager.put(queueId,dialog);
    }

    public static void hideProgressDialog(int queueId){
        manager.get(queueId).hide();
        manager.remove(queueId);
    }
}
