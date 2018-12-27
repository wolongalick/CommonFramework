package common.ui;

import android.content.Context;
import android.icu.util.Measure;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import common.utils.ScreenUtils;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2017/8/5
 * 备注:
 */
public class StatusPlaceHolder extends View {


    public StatusPlaceHolder(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if(isInEditMode()){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                setVisibility(View.GONE);
            } else {
                getLayoutParams().height= ScreenUtils.getStatusHeight(context);
                setVisibility(View.VISIBLE);
            }
            return;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setVisibility(View.GONE);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setVisibility(View.VISIBLE);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),ScreenUtils.getStatusHeight(getContext()));
        }

    }
}
