package common.ui.datacontent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.common.R;

/**
 * 简单的空页面类
 * Created by Alick on 2016/9/25.
 */
public class SimpleGlobalFrameLayout2 extends GlobalFrameLayout {

    public SimpleGlobalFrameLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getRealContent() {
        //注意末尾参数是false
        return LayoutInflater.from(getContext()).inflate(R.layout.simple_global_layout2, this, false);
    }


}
