package common.ui.datacontent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.common.R;

import common.ui.GifView;

/**
 * Created by Alick on 2015/10/10.
 */
public class LoadingFrameLayout extends FrameLayout implements ILoadingLayout{
    private GifView gifView;
    public LoadingFrameLayout(Context context) {
        this(context, null);
    }

    public LoadingFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_layout_loading_gif, this);

        gifView = rootView.findViewById(R.id.iv_loading);

        gifView.setGifResource(R.drawable.loadingjokes);


//        Glide.with(getContext()).load(R.drawable.loading_gif)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .centerCrop()
//                .into(iv_loading);

    }

    @Override
    public void setLoadingGif(int gifResId){
        gifView.setGifResource(gifResId);
    }

    public GifView getGifView() {
        return gifView;
    }
}
