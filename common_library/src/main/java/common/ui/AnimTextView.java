package common.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cxw on 2016/4/13.
 */
public class AnimTextView extends TextView{
    public static final String TAG = "AnimTextView";
    private final int defaultDuration=500;
    public AnimTextView(Context context) {
        this(context,null);
    }

    public AnimTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextWithAnim(final String text){
        setTextWithAnim(text,defaultDuration);
    }

    public void setTextWithAnim(final String text, final int duration){
        if(TextUtils.isEmpty(getText())){
            setText(text);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0, 1).setDuration(duration);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setClickable(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }else if(TextUtils.isEmpty(text)){
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1, 0).setDuration(duration);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setText(text);
                    setClickable(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }else{
            setTextFromNotEmptyText(text, duration);
        }
    }

    private void setTextFromNotEmptyText(final String text, final int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1, 0).setDuration(duration / 2);

        objectAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setText(text);
                setClickable(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(AnimTextView.this, "alpha", 0, 1).setDuration(duration / 2);
        objectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playSequentially(objectAnimator,objectAnimator1);
        animatorSet.start();
    }

}
