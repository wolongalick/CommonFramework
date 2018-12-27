package common.ui.datacontent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.R;

/**
 * Created by Alick on 2015/10/10.
 */
public class FailFrameLayout extends FrameLayout implements View.OnClickListener,IFailLayout {
    private View loadingView;
    private OnClickReloadButtonListener onClickReloadButtonListener;
    private ImageView iv_fail;
    private TextView tv_reload;
    private View ll_loadFailed;

    public FailFrameLayout(Context context) {
        this(context, null);
    }

    public FailFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FailFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView(){
        setId(R.id.dataContent_fail);
        LayoutInflater.from(getContext()).inflate(R.layout.custom_layout_fail, this);
        iv_fail = findViewById(R.id.iv_fail);
        tv_reload= findViewById(R.id.tv_reload);
        ll_loadFailed=findViewById(R.id.ll_loadFailed);
        tv_reload.setOnClickListener(this);
        ll_loadFailed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_reload) {
            if (onClickReloadButtonListener != null) {
                loadingView.setVisibility(VISIBLE);
                onClickReloadButtonListener.onClick(v);
            }
        }else if(i==R.id.ll_loadFailed){
            if (onClickReloadButtonListener != null) {
                loadingView.setVisibility(VISIBLE);
                onClickReloadButtonListener.onClick(v);
            }
        }
    }

    @Override
    public void setOnClickReloadButton(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener) {
        this.onClickReloadButtonListener=onClickReloadButtonListener;
    }

    @Override
    public void setFailView(int failImageResId) {
        iv_fail.setImageResource(failImageResId);
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }
}
