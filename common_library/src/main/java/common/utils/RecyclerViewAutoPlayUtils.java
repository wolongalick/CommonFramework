package common.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2017/12/11
 * 备注:
 */
public class  RecyclerViewAutoPlayUtils {

    public interface IAutoPlayFilter<T>{
        boolean isCanPlay();

        void beginPlay();
    }

    public static <T> void autoPlay(RecyclerView recyclerView, List<T> list, IAutoPlayFilter iAutoPlayFilter){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();



        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();


    }

}
