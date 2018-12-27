package common.ui.datacontent;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.R;

import common.utils.DensityUtils;

/**
 * Created by cxw on 2015/11/17.
 */
public class EmptyFrameLayout extends FrameLayout implements IEmptyLayout{
    private OnClickEmptyLayoutListener onClickEmptyLayoutListener;
    private LinearLayout ll_empty;
    private ImageView iv_emptyImg;
    private TextView tv_empty;
    private TextView tv_operate;
    private int marginBottom;
    private boolean isSetMargined;
    public EmptyFrameLayout(Context context) {
        this(context, null);
    }

    public EmptyFrameLayout(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public EmptyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        setId(R.id.dataContent_empty);
        LayoutInflater.from(getContext()).inflate(R.layout.custom_layout_empty_data, this);
        ll_empty=findViewById(R.id.ll_empty);
        iv_emptyImg = findViewById(R.id.iv_emptyImg);
        tv_empty = findViewById(R.id.tv_empty);
        tv_operate = findViewById(R.id.tv_operate);

        marginBottom= DensityUtils.dp2px(getContext(),100);

        /*ll_empty.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = ll_empty.getMeasuredHeight();

                int total = getMeasuredHeight();

                if(!isSetMargined && height!=0 && total!=0 && height<total){
                    LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0,(int) (total*0.23),0,0);
                    layoutParams.gravity= Gravity.CENTER_HORIZONTAL;
                    ll_empty.setLayoutParams(layoutParams);
                    isSetMargined=true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ll_empty.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }

            }
        });*/
    }

    @Override
    public void setEmptyView(View view) {
        removeAllViews();
        addView(view);
    }

    @Override
    public void setEmptyText(String buttonText) {
        if(!TextUtils.isEmpty(buttonText)){
            tv_empty.setText(buttonText);
            tv_empty.setVisibility(VISIBLE);
        }else{
            tv_empty.setText(buttonText);
            tv_empty.setVisibility(GONE);
        }
    }

    @Override
    public void setEmptyImg(@DrawableRes int emptyImgResId) {
        if(emptyImgResId!=0){
            iv_emptyImg.setImageResource(emptyImgResId);
            iv_emptyImg.setVisibility(VISIBLE);
        }else {
            iv_emptyImg.setVisibility(GONE);
        }
    }

    @Override
    public void setEmptyBtnText(String btnText) {
        if(!TextUtils.isEmpty(btnText)){
            tv_operate.setVisibility(VISIBLE);
            tv_operate.setText(btnText);
            tv_operate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickEmptyLayoutListener!=null){
                        onClickEmptyLayoutListener.onClick(v);
                    }
                }
            });
        }else {
            tv_operate.setVisibility(GONE);
        }
    }

    @Override
    public void setOnClickEmptyLayoutListener(OnClickEmptyLayoutListener onClickEmptyLayoutListener) {
        this.onClickEmptyLayoutListener = onClickEmptyLayoutListener;
    }

}
