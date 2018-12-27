package common.ui.datacontent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.common.R;

import common.base.INoData;

/**
 * Created by Alick on 2015/10/10.
 */
public class GlobalFrameLayout extends FrameLayout implements ILoadingLayout,IEmptyLayout,IFailLayout,INoData{
    private Context context;
    private FrameLayout.LayoutParams match_parent_layoutParams =new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

    private View fl_realContent;
    private FailFrameLayout fl_failContent;
    private EmptyFrameLayout fl_emptyContent;
    private LoadingFrameLayout fl_loadingContent;
    private AlphaAnimation fadeAnimation;
    private int emptyDataImg;
    private String emptyDataHint;
    private String emptyDataBtnText;

    public GlobalFrameLayout(Context context) {
        this(context, null);
    }

    public GlobalFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlobalFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GlobalFrameLayout);
        //可能为空
        emptyDataImg=array.getResourceId(R.styleable.GlobalFrameLayout_globalEmptyDataImg,0);
        emptyDataHint=array.getString(R.styleable.GlobalFrameLayout_globalEmptyDataHint);
        emptyDataBtnText=array.getString(R.styleable.GlobalFrameLayout_globalEmptyDataBtnText);
        array.recycle();

        setId(R.id.globalFrameLayout);
        initField();
        initView();
    }

    private void initField() {
        fadeAnimation = new AlphaAnimation(0, 1);
        fadeAnimation.setDuration(300);
        fadeAnimation.setInterpolator(new DecelerateInterpolator());
    }

    /**
     * 初始化视图
     * @author cxw
     * create at 2015/10/10 15:10
     */
    private void initView() {

        fl_failContent =new FailFrameLayout(context);
        fl_failContent.setLayoutParams(match_parent_layoutParams);

        fl_emptyContent= new EmptyFrameLayout(context);
        fl_emptyContent.setLayoutParams(match_parent_layoutParams);

        setEmptyImg(emptyDataImg);
        if(!TextUtils.isEmpty(emptyDataHint)){
            setEmptyText(emptyDataHint);
        }
        setEmptyBtnText(emptyDataBtnText);


        fl_loadingContent=new LoadingFrameLayout(context);
        fl_loadingContent.setLayoutParams(match_parent_layoutParams);

        fl_failContent.setLoadingView(fl_loadingContent);
    }

    private void fillLayout(){
        fl_realContent=getRealContent();
        try {
            addView(fl_realContent);
        } catch (Exception e) {
//            e.printStackTrace();//当fl_realContent界面如果本身就在global中,则会报异常
        }

        addView(fl_failContent);
        addView(fl_emptyContent);
        addView(fl_loadingContent);
    }

    /**
     * (可能从子类)获取真实的内容
     * @return
     */
    protected View getRealContent(){
        //这是本类默认的方法:取第一个子view
        return getChildAt(0);
    }

    private void initValues(){
        fl_loadingContent.setVisibility(GONE);
        fl_emptyContent.setVisibility(GONE);
        fl_failContent.setVisibility(GONE);
    }

    //========================加载、空页面、加载失败和加载真实数据界面的方法===============================
    /**
     * 显示loading页
     */
    @Override
    public void showLoadingView(){
        fl_loadingContent.setVisibility(VISIBLE);
        fl_emptyContent.setVisibility(GONE);
        fl_failContent.setVisibility(GONE);
        fl_realContent.setVisibility(GONE);
    }

    /**
     * 显示空页面
     */
    @Override
    public void showEmptyView(){
        fl_loadingContent.setVisibility(GONE);
        fl_failContent.setVisibility(GONE);
        fl_realContent.setVisibility(GONE);
        fl_emptyContent.setVisibility(VISIBLE);
    }

    /**
     * 显示加载失败页面
     */
    @Override
    public void showFailView() {
        fl_loadingContent.setVisibility(GONE);
        fl_emptyContent.setVisibility(GONE);
        fl_failContent.setVisibility(VISIBLE);
        fl_realContent.setVisibility(GONE);
    }

    /**
     * 显示真实页面
     */
    @Override
    public void showRealView(){
        //因为有可能连续显示RealView,所以需要做下判断,避免执行不必要的代码
        if(fl_realContent.getVisibility()!=VISIBLE){
            fl_loadingContent.setVisibility(GONE);
            fl_emptyContent.setVisibility(GONE);
            fl_failContent.setVisibility(GONE);
            fl_realContent.setVisibility(VISIBLE);
        }
        //渐变动画
        fl_realContent.startAnimation(fadeAnimation);
    }

    /**
     * 显示真实内容页面(不使用动画)
     */
    @Override
    public void showRealViewWithoutAnimation() {
        //因为有可能连续显示RealView,所以需要做下判断,避免执行不一要的代码
        if(fl_realContent.getVisibility()!=VISIBLE){
            fl_loadingContent.setVisibility(GONE);
            fl_emptyContent.setVisibility(GONE);
            fl_failContent.setVisibility(GONE);
            fl_realContent.setVisibility(VISIBLE);
        }
    }

    //========================空页面的方法:===============================
    @Override
    public void setEmptyView(View emptyView) {
        fl_emptyContent.setEmptyView(emptyView);
    }

    @Override
    public void setEmptyText(String hintText) {
        fl_emptyContent.setEmptyText(hintText);
    }

    @Override
    public void setEmptyImg(@IdRes int emptyImgResId) {
        fl_emptyContent.setEmptyImg(emptyImgResId);
    }


    @Override
    public void setEmptyBtnText(String btnText) {
        fl_emptyContent.setEmptyBtnText(btnText);
    }

    @Override
    public void setOnClickEmptyLayoutListener(OnClickEmptyLayoutListener onClickEmptyLayoutListener) {
        fl_emptyContent.setOnClickEmptyLayoutListener(onClickEmptyLayoutListener);
    }

    @Override
    public void setLoadingGif(int gifResId) {
        fl_loadingContent.setLoadingGif(gifResId);
    }

    //========================加载失败页的方法:===============================
    @Override
    public void setOnClickReloadButton(OnClickReloadButtonListener onClickReloadButtonListener) {
        fl_failContent.setOnClickReloadButton(onClickReloadButtonListener);
    }

    @Override
    public void setFailView(int failImageResId) {
        fl_failContent.setFailView(failImageResId);
    }

    //========================当填充完整个view时调用的方法:===============================
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        fillLayout();
        initValues();
    }

    //===========================get方法-begin===========================
    public FailFrameLayout getFailContent() {
        return fl_failContent;
    }

    public EmptyFrameLayout getEmptyContent() {
        return fl_emptyContent;
    }

    public LoadingFrameLayout getLoadingContent() {
        return fl_loadingContent;
    }
    //===========================get方法-end===========================
}
