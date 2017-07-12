package common.ui.datacontent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.common.R;

/**
 * Created by Alick on 2015/10/10.
 */
public class LoadingFrameLayout extends FrameLayout{

    public LoadingFrameLayout(Context context) {
        this(context, null);
    }

    public LoadingFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.custom_layout_loading,this);
    }


}
