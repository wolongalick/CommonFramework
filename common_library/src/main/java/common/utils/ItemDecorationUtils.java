package common.utils;

import android.support.v7.widget.RecyclerView;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/1/3
 * 备注:
 */
public class ItemDecorationUtils {
    public static void addItemDecoration(RecyclerView recyclerView,RecyclerView.ItemDecoration itemDecoration){
        try {
            if (recyclerView.getItemDecorationAt(0) == null) {
                //添加分割线
                recyclerView.addItemDecoration(itemDecoration);
            }
        } catch (Exception e) {
            recyclerView.addItemDecoration(itemDecoration);
        }

    }
}
