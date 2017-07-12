package common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by cxw on 2015/11/5.
 */
public class ReMeasureGridView extends GridView{
    public ReMeasureGridView(Context context) {
        this(context, null);
    }

    public ReMeasureGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0) ;
    }

    public ReMeasureGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
