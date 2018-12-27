package common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.R;
import com.nineoldandroids.animation.ObjectAnimator;

import common.annotation.LeftMode;
import common.annotation.RightType;
import common.utils.ScreenUtils;

public class Topbar extends FrameLayout implements View.OnClickListener {
    private Context context;
    private View statusBarView;
    private RelativeLayout rl_title;
    private LinearLayout ll_leftOption;
    private LinearLayout ll_rightOption;
    private LinearLayout ll_title;

    private ImageView iv_left;
    private TextView tv_left;

    private TextView tv_title;
    private ImageView iv_arrow_down;

    private ImageView iv_right;
    private TextView tv_right;

    public static final int TOPBAR_TYPE_NONE = 0;
    public static final int TOPBAR_TYPE_IMG = 1;
    public static final int TOPBAR_TYPE_TEXT = 2;
    public static final int TOPBAR_TYPE_ANIM = 3;


    public static final int TOPBAR_LEFT_MODE_BACK = 0;
    public static final int TOPBAR_LEFT_MODE_MORE = 1;

    public static final int DEFAULT_TOPBAR_TEXT_COLOR = R.color.white;
    public static final int DEFAULT_LEFT_TYPE = TOPBAR_TYPE_NONE;
    private static final int DEFAULT_LEFT_IMAGE = R.drawable.back_white;

    private static final int DEFAULT_TITLE_TYPE = TOPBAR_TYPE_TEXT;

    public static final int DEFAULT_RIGHT_TYPE = TOPBAR_TYPE_NONE;
    private static final int DEFAULT_RIGHT_IMAGE = R.drawable.back_white;

    private static final int DEFAULT_TOPBAR_LEFT_MODE = TOPBAR_LEFT_MODE_BACK;

    private int topbarTextColor;
    private int leftType;
    private int leftImage;
    private String leftText;
    private int leftMode;

    private String titleText;
    private int titleImage;
    private int titleType;

    private int rightType;
    private int rightImage;
    private String rightText;

    private int topbarBg;
    private int statusBarBg;
    private boolean isExtendStatusBar;      //是否将topbar延伸至状态栏
    private boolean statusBarAsTopbarBg;    //状态栏是否采用与topbar相同的背景图片

    private OnClickTopbarLeftListener onClickTopbarLeftListener;
    private OnClickTopbarTitleListener onClickTopbarTitleListener;
    private OnClickTopbarRightListener onClickTopbarRightListener;
    private OnClickBackKeyListener onClickBackKeyListener;

    public interface OnClickBackKeyListener{
        void onClickBackKey();
    }

    public interface OnClickTopbarLeftListener {
        void onClickTopbarLeft();
    }

    public interface OnClickTopbarTitleListener {
        void onClickTopbarTitle();
    }

    public abstract static class OnOpenCloseTopbarTitleListener implements OnClickTopbarTitleListener {
        private boolean isOpened;

        @Override
        public final void onClickTopbarTitle(){
            if(!isOpened){
                isOpened=true;
                onOpen();
            }else {
                isOpened=false;
                onClose();
            }
        }

        public abstract void onOpen();

        public abstract void onClose();

        public boolean isOpened() {
            return isOpened;
        }

        public void setOpened(boolean opened) {
            isOpened = opened;
        }
    }

    public interface OnClickTopbarRightListener {
        void onClickTopbarRight();
    }




    public Topbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.layout_topbar, this);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Topbar);



        isExtendStatusBar = array.getBoolean(R.styleable.Topbar_topbarIsExtendStatusBar, false);
        statusBarAsTopbarBg = array.getBoolean(R.styleable.Topbar_statusBarAsTopbarBg, false);
        topbarBg = array.getResourceId(R.styleable.Topbar_topbarBg, context.getResources().getColor(R.color.topbar_background));
        statusBarBg = array.getResourceId(R.styleable.Topbar_statusBarBg, context.getResources().getColor(R.color.topbar_background));

        topbarTextColor=array.getColor(R.styleable.Topbar_topbarTextColor,context.getResources().getColor(DEFAULT_TOPBAR_TEXT_COLOR));

        leftType = array.getInt(R.styleable.Topbar_topbarLeftType, DEFAULT_LEFT_TYPE);
        leftImage = array.getResourceId(R.styleable.Topbar_topbarLeftImage, DEFAULT_LEFT_IMAGE);
        leftText = array.getString(R.styleable.Topbar_topbarLeftText);
        leftMode = array.getInt(R.styleable.Topbar_topbarLeftMode, DEFAULT_TOPBAR_LEFT_MODE);

        titleType = array.getInt(R.styleable.Topbar_topbarTitleType, DEFAULT_TITLE_TYPE);
        titleImage = array.getResourceId(R.styleable.Topbar_topbarTitleImage, 0);//默认中间标题栏没有图片
        titleText = array.getString(R.styleable.Topbar_topbarTitleText);


        rightType = array.getInt(R.styleable.Topbar_topbarRightType, DEFAULT_RIGHT_TYPE);
        rightImage = array.getResourceId(R.styleable.Topbar_topbarRightImage, DEFAULT_RIGHT_IMAGE);
        rightText = array.getString(R.styleable.Topbar_topbarRightText);

        initViews();
        initValues();
        setOnClickListeners();

        array.recycle();
    }

    public void initViews() {
        statusBarView = findViewById(R.id.statusBarView);
        rl_title = findViewById(R.id.rl_title);
        ll_leftOption = findViewById(R.id.ll_leftOption);
        ll_title = findViewById(R.id.ll_title);
        ll_rightOption = findViewById(R.id.ll_rightOption);

        iv_left = findViewById(R.id.iv_left);
        iv_right = findViewById(R.id.iv_right);

        tv_title = findViewById(R.id.tv_title);
        iv_arrow_down = findViewById(R.id.iv_arrow_down);
        tv_left = findViewById(R.id.tv_left);
        tv_right = findViewById(R.id.tv_right);

    }

    private void initValues() {
        controlStatusBar_titleBar();
        controlLeftWight();
        controlTitleWight();
        controlRightWight();
    }

    private void controlStatusBar_titleBar() {
        if (isExtendStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏与标题栏背景融为一体
            statusBarView.getLayoutParams().height = ScreenUtils.getStatusHeight(context);

            if(statusBarAsTopbarBg){
                statusBarView.setBackgroundResource(topbarBg);
            }else {
                statusBarView.setBackgroundResource(statusBarBg);
            }

            rl_title.setBackgroundColor(Color.TRANSPARENT);
            setBackgroundResource(topbarBg);
        } else {
            statusBarView.setVisibility(GONE);
            rl_title.setBackgroundResource(topbarBg);
        }
    }

    private void controlLeftWight() {
        switch (leftType) {
            case TOPBAR_TYPE_IMG:
                tv_left.setVisibility(GONE);
                iv_left.setImageResource(leftImage);
                break;
            case TOPBAR_TYPE_TEXT:
                iv_left.setVisibility(GONE);
                tv_left.setText(leftText);
                break;
            default:
                ll_leftOption.setVisibility(GONE);
        }
    }

    private void controlTitleWight() {
        switch (titleType) {
            case TOPBAR_TYPE_IMG:
                tv_title.setVisibility(GONE);
                iv_arrow_down.setVisibility(VISIBLE);
                iv_arrow_down.setImageResource(titleImage);
                break;
            case TOPBAR_TYPE_TEXT:
                tv_title.setTextColor(topbarTextColor);
                tv_title.setText(titleText);
                tv_title.setVisibility(VISIBLE);
                iv_arrow_down.setVisibility(GONE);
                break;
            case TOPBAR_TYPE_ANIM:
                tv_title.setTextColor(topbarTextColor);
                tv_title.setText(titleText);
                tv_title.setVisibility(VISIBLE);
                iv_arrow_down.setVisibility(VISIBLE);

                break;
        }
    }


    private void controlRightWight() {
        switch (rightType) {
            case TOPBAR_TYPE_IMG:
                ll_rightOption.setVisibility(VISIBLE);
                tv_right.setVisibility(GONE);
                iv_right.setImageResource(rightImage);
                iv_right.setVisibility(VISIBLE);
                break;
            case TOPBAR_TYPE_TEXT:
                ll_rightOption.setVisibility(VISIBLE);
                iv_right.setVisibility(GONE);
                tv_right.setText(rightText);
                tv_right.setVisibility(VISIBLE);
                tv_left.setTextColor(topbarTextColor);
                tv_right.setTextColor(topbarTextColor);
                break;
            default:
                ll_rightOption.setVisibility(GONE);
        }
    }


    private void setOnClickListeners() {
        iv_left.setOnClickListener(this);
        tv_left.setOnClickListener(this);
        ll_title.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == iv_left || v == tv_left) {
            switch (leftMode) {
                case TOPBAR_LEFT_MODE_MORE:
                    if (onClickTopbarLeftListener == null) {
                        throw new NullPointerException("请先给Topbar设置监听:onClickTopbarLeftListener()");
                    } else {
                        onClickTopbarLeftListener.onClickTopbarLeft();
                    }
                    break;
                default:
                    try {
                        Activity activity = (Activity) this.context;
                        if(onClickBackKeyListener!=null){
                            onClickBackKeyListener.onClickBackKey();
                        }
                        activity.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        } else if (v == iv_right || v == tv_right) {
            if (onClickTopbarRightListener == null) {
                throw new NullPointerException("请先给Topbar设置监听:setOnClickTopbarRightListener()");
            } else {
                onClickTopbarRightListener.onClickTopbarRight();
            }
        } else if(v==ll_title){
            switch (titleType){
                case TOPBAR_TYPE_ANIM:
                    if(onClickTopbarTitleListener==null){
                        throw new NullPointerException("请先给Topbar设置监听:setOnClickTopbarTitleListener()");
                    }else {
                        onClickTopbarTitleListener.onClickTopbarTitle();
                    }
                    break;
            }

        }
    }

    public void openTitleIcon(){
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(iv_arrow_down,"rotation",-180);
        objectAnimator.setDuration(getResources().getInteger(R.integer.topbar_titile_anim_duration));
        objectAnimator.start();
    }

    public void closeTitleIcon(){
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(iv_arrow_down,"rotation",0);
        objectAnimator.setDuration(getResources().getInteger(R.integer.topbar_titile_anim_duration));
        objectAnimator.start();
    }



    /*==========================set方法-begin==========================*/
    public void setOnClickTopbarLeftListener(OnClickTopbarLeftListener onClickTopbarLeftListener) {
        this.onClickTopbarLeftListener = onClickTopbarLeftListener;
    }

    public void setOnClickTopbarTitleListener(OnClickTopbarTitleListener onClickTopbarTitleListener) {
        this.onClickTopbarTitleListener = onClickTopbarTitleListener;
    }

    public void setOnClickTopbarRightListener(OnClickTopbarRightListener onClickTopbarRightListener) {
        this.onClickTopbarRightListener = onClickTopbarRightListener;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        tv_title.setText(titleText);
    }

    public void setTopbarBackgroundResourceId(int resid) {
        topbarBg = context.getResources().getColor(resid);
        controlStatusBar_titleBar();
    }

    public int getLeftType() {
        return leftType;
    }

    public void setLeftType(int leftType) {
        this.leftType = leftType;
    }

    public int getLeftImage() {
        return leftImage;
    }

    public void setLeftImage(int leftImage) {
        this.leftImage = leftImage;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public int getLeftMode() {
        return leftMode;
    }

    public void setLeftMode(@LeftMode int leftMode) {
        this.leftMode = leftMode;
    }

    public String getTitleText() {
        return titleText;
    }

    public int getRightType() {
        return rightType;
    }

    public void setRightType(@RightType int rightType) {
        this.rightType = rightType;
        controlRightWight();
    }

    public int getRightImage() {
        return rightImage;
    }

    public void setRightImage(int rightImage) {
        this.rightImage = rightImage;
        setRightType(TOPBAR_TYPE_IMG);
        controlRightWight();
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        setRightType(TOPBAR_TYPE_TEXT);
        controlRightWight();
    }

    public ImageView getIv_title() {
        return iv_arrow_down;
    }

    public ImageView getIv_right() {
        return iv_right;
    }

    public TextView getTv_right() {
        return tv_right;
    }

    public TextView getTitle() {
        return tv_title;
    }

    public ImageView getIv_left() {
        return iv_left;
    }

    public void setOnClickBackKeyListener(OnClickBackKeyListener onClickBackKeyListener) {
        this.onClickBackKeyListener = onClickBackKeyListener;
    }

    /*==========================set方法-end==========================*/
}
