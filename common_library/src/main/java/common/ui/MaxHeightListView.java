package common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;


import com.common.R;

import common.utils.BLog;

/**
 * 能够设置最大高度的listview
 */
public class MaxHeightListView extends ListView {

    /**
     * listview高度
     */
    private int listViewHeight;

    public MaxHeightListView(Context context) {
        this(context,null);
    }

    public MaxHeightListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MaxHeightListView);
        int max_height= typedArray.getDimensionPixelSize(R.styleable.MaxHeightListView_max_height, 0);

        BLog.i("max_height="+max_height);
        this.listViewHeight= max_height;

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        if (listViewHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(listViewHeight,
                    MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置listview的最大高度
     * @param listViewHeight
     */
    public void setListViewMaxHeight(int listViewHeight) {
        this.listViewHeight = listViewHeight;
    }
}