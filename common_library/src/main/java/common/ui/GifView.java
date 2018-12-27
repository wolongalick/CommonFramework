//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.PorterDuff;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.common.R;

public class GifView extends android.support.v7.widget.AppCompatImageView {
    private static final int DEFAULT_MOVIE_VIEW_DURATION = 1000;
    private int mMovieResourceId;
    private Movie movie;
    private long mMovieStart;
    private int mCurrentAnimationTime;
    private float mLeft;
    private float mTop;
    private float mScale;
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;
    private volatile boolean mPaused;
    private boolean mVisible;
    private final int defaultMinWidth=0;
    private final int defaultMaxWidth=Integer.MAX_VALUE;

    private int minWidth=0;         //最小宽度(单位px)
    private int maxWidth=9999999;   //最大宽度(单位px)

    private String filePath;

    public GifView(Context context) {
        this(context, null);
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CustomTheme_gifViewStyle);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mVisible = true;
        this.setViewAttributes(context, attrs, defStyle);
    }

    @SuppressLint({"NewApi"})
    private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {
        if(VERSION.SDK_INT >= 11) {
            this.setLayerType(1,null);
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifView, defStyle, R.style.Widget_GifView);
        this.mMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1);
        this.mPaused = array.getBoolean(R.styleable.GifView_paused, false);

        minWidth=array.getDimensionPixelOffset(R.styleable.GifView_minWidth,defaultMinWidth);
        maxWidth=array.getDimensionPixelOffset(R.styleable.GifView_maxWidth,defaultMaxWidth);

        array.recycle();
        if(this.mMovieResourceId != -1) {
            this.movie = Movie.decodeStream(this.getResources().openRawResource(this.mMovieResourceId));
        }

    }

    public void setGifResource(int movieResourceId) {
        this.mMovieResourceId = movieResourceId;
        this.movie = Movie.decodeStream(this.getResources().openRawResource(this.mMovieResourceId));
        this.requestLayout();
    }

    public void setGifFilePath(String filePath) {
        this.filePath= filePath;
        this.movie = Movie.decodeFile(filePath);
        this.requestLayout();
    }

    public int getGifResource() {
        return this.mMovieResourceId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void play() {
        if(this.mPaused) {
            this.mPaused = false;
            this.mMovieStart = SystemClock.uptimeMillis() - (long)this.mCurrentAnimationTime;
            this.invalidate();
        }

    }

    public void pause() {
        if(!this.mPaused) {
            this.mPaused = true;
            this.invalidate();
        }

    }

    public boolean isPaused() {
        return this.mPaused;
    }

    public boolean isPlaying() {
        return !this.mPaused;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(this.movie != null) {
            int movieWidth = this.movie.width();
            int movieHeight = this.movie.height();
            float scaleH = 1.0F;
            int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);
            if(measureModeWidth != 0) {
                int scaleW = MeasureSpec.getSize(widthMeasureSpec);
                if(movieWidth > scaleW) {
                    scaleH = (float)movieWidth / (float)scaleW;
                }
            }

            float scaleW1 = 1.0F;
            int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);
            if(measureModeHeight != 0) {
                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
                if(movieHeight > maximumHeight) {
                    scaleW1 = (float)movieHeight / (float)maximumHeight;
                }
            }

            this.mScale = 1.0F / Math.max(scaleH, scaleW1);
            this.mMeasuredMovieWidth = (int)((float)movieWidth * this.mScale);
            this.mMeasuredMovieHeight = (int)((float)movieHeight * this.mScale);

            //=========add by cxw---2016.11.13-begin==========
            //新增调整gif图片宽高的代码,做了最小和最大宽度的限制

            if(mMeasuredMovieWidth<minWidth){
                float ratio = (float) minWidth / mMeasuredMovieWidth;
                if(ratio>2){
                    ratio=2;
                }
                mScale=mScale*ratio;
                mMeasuredMovieHeight = (int) (mMeasuredMovieHeight* ratio);
                mMeasuredMovieWidth=(int) (mMeasuredMovieWidth* ratio);
            }else if (mMeasuredMovieWidth > maxWidth) {
                mMeasuredMovieHeight = (int) (mMeasuredMovieHeight / ((double) mMeasuredMovieWidth / maxWidth));
                mScale=mScale*maxWidth/mMeasuredMovieWidth;
                mMeasuredMovieWidth = maxWidth;
            }
            //=========add by cxw---2016.11.13-end==========

            this.setMeasuredDimension(this.mMeasuredMovieWidth, this.mMeasuredMovieHeight);
        } else {
            this.setMeasuredDimension(this.getSuggestedMinimumWidth(), this.getSuggestedMinimumHeight());
        }

    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mLeft = (float)(this.getWidth() - this.mMeasuredMovieWidth) / 2.0F;
        this.mTop = (float)(this.getHeight() - this.mMeasuredMovieHeight) / 2.0F;
        this.mVisible = this.getVisibility() == VISIBLE;
    }

    protected void onDraw(Canvas canvas) {
        if(this.movie != null) {
            if(!this.mPaused) {
                this.updateAnimationTime();
                //设置背景色为透明色
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
                this.drawMovieFrame(canvas);
                this.invalidateView();
            } else {
                this.drawMovieFrame(canvas);
            }
        }

    }

    @SuppressLint({"NewApi"})
    private void invalidateView() {
        if(this.mVisible) {
            if(VERSION.SDK_INT >= 16) {
                this.postInvalidateOnAnimation();
            } else {
                this.invalidate();
            }
        }

    }

    private void updateAnimationTime() {
        long now = SystemClock.uptimeMillis();
        if(this.mMovieStart == 0L) {
            this.mMovieStart = now;
        }

        int dur = this.movie.duration();
        if(dur == 0) {
            dur = 1000;
        }

        this.mCurrentAnimationTime = (int)((now - this.mMovieStart) % (long)dur);
    }

    private void drawMovieFrame(Canvas canvas) {
        this.movie.setTime(this.mCurrentAnimationTime);
        canvas.save();
        canvas.scale(this.mScale, this.mScale);
        this.movie.draw(canvas, this.mLeft / this.mScale, this.mTop / this.mScale);
        canvas.restore();
    }

    @SuppressLint({"NewApi"})
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        this.mVisible = screenState == 1;
        this.invalidateView();
    }

    @SuppressLint({"NewApi"})
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.mVisible = visibility == VISIBLE;
        this.invalidateView();
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mVisible = visibility == VISIBLE;
        this.invalidateView();
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }
}
