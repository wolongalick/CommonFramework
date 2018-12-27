package common.utils;

import android.graphics.Rect;
import android.view.View;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/6/26
 * 备注:
 */
public class ViewUtils {
    /**
     * 获取view的可见高度半分比<br/>
     * 说明:
     * (1)view在recyclerView上部分超出屏幕时:Rect(30, -1714 - 1051, -114)
     *
     * @param childView
     * @return
     */
    public static float getVisiblePercent(View childView){
        Rect childRect=new Rect();
        childView.getLocalVisibleRect(childRect);
//        BLog.i("子布局高度:"+childView.getMeasuredHeight()+",四边:"+childRect.toShortString());

        int measuredHeight = childView.getMeasuredHeight();
        if(childRect.bottom<0 || childRect.top<0 || childRect.bottom>measuredHeight || childRect.top>measuredHeight){
            return 0;
        }
        int visibleHeight = childRect.bottom - childRect.top;

        return ((float)visibleHeight)/measuredHeight;
    }
}
