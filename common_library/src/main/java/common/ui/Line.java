package common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.common.R;


/**
 * Created by cxw on 2015/11/20.
 */
public class Line extends ImageView{
    private int width;

    public Line(Context context,int width) {
        this(context,null,width);
    }

    public Line(Context context, AttributeSet attrs,int width) {
        this(context, attrs, 0, width);
    }

    public Line(Context context, AttributeSet attrs, int defStyleAttr,int width) {
        super(context, attrs, defStyleAttr);
        this.width=width;
        init();
    }


    private void init() {
        setBackgroundResource(R.color.hui_d8);
        setMaxWidth(width);
        setMinimumWidth(width);
        setMaxHeight(1);
        setMinimumHeight(1);
    }


}
