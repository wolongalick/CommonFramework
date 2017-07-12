package common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.common.R;

import java.util.ArrayList;
import java.util.List;

import common.utils.DensityUtils;

/**
 * Created by cxw on 2016/2/29.
 */
public class CustomRatingBar extends LinearLayout{
    private Context context;    //上下文
    private int star_img;       //星星的个数
    private int star_max_num;   //星星的图片资源ID
    private int star_progress;  //星星进度
    private List<ImageView> starList;   //星星集合


    public CustomRatingBar(Context context) {
        this(context, null);
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar);
        star_img =typedArray.getResourceId(R.styleable.CustomRatingBar_star_img, 0);
        star_max_num =typedArray.getInteger(R.styleable.CustomRatingBar_star_max_num, 0);
        star_progress =typedArray.getInteger(R.styleable.CustomRatingBar_star_progress,1);

        typedArray.recycle();

        initParams();
        initViews();

        updateStarStatus(star_progress);
    }

    private void initParams() {
        starList=new ArrayList<>();
    }

    private void initViews() {
        int padding = DensityUtils.dp2px(context, 15);
        for (int i = 0; i < star_max_num; i++) {
            ImageView star=new ImageView(context);
            star.setPadding(0,padding,0,padding);
            star.setImageResource(star_img);
            LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            star.setLayoutParams(layoutParams);
            star.setOnClickListener(new OnClickListenerImpl(i));

            starList.add(star);
            addView(star);
        }
    }

    private void updateStarStatus(int star_progress){
        if(star_progress<0) {
            new IllegalArgumentException("设置的进度不能<0").printStackTrace();
            return;
        }
        if(star_progress>star_max_num){
            new IllegalArgumentException("设置的进度超过了星星总数量,进度为:"+star_progress+",但总长度为:"+star_max_num).printStackTrace();
            return;
        }

        this.star_progress=star_progress;
        for (int i = 0; i < star_max_num; i++) {
            ImageView starImage=starList.get(i);
            starImage.setSelected(i<star_progress);
        }
    }

    private OnItemClickStarListener onItemClickStarListener;
    public interface OnItemClickStarListener{
        void onClickItem(int position);
    }

    public void setOnItemClickStarListener(OnItemClickStarListener onItemClickStarListener){
        this.onItemClickStarListener=onItemClickStarListener;
    }

    private class OnClickListenerImpl implements OnClickListener{
        private int position;

        public OnClickListenerImpl(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            updateStarStatus(position+1);

            if (onItemClickStarListener!=null) {
                onItemClickStarListener.onClickItem(position);
            }
        }
    }

    public int getStar_progress() {
        return star_progress;
    }

    public void setStar_progress(int star_progress) {
        this.star_progress = star_progress;
        updateStarStatus(star_progress);
    }

}
