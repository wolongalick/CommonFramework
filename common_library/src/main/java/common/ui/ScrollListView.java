package common.ui;

/**
 * 解决和Scrollview滑动
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ScrollListView extends ListView {
    public ScrollListView(Context context) {
        super(context);
    }
    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ScrollListView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     21.     * 重写该方法，达到使ListView适应ScrollView的效果
     22.     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
