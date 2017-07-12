package common.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import common.utils.DensityUtils;


public class CustomEditText extends EditText {
    private Drawable dRight;
    private Rect rBounds;
    private Context context;

    private int offset;

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomEditText(Context context) {
        super(context);
        this.context = context;
    }

    public void setLeftRightCompoundDrawables(Drawable right, int offset) {
        this.offset = offset;
        setCompoundDrawables(getCompoundDrawables()[0], null, right, null);
    }

    public void setLeftRightCompoundDrawables(Drawable right) {
        setCompoundDrawables(getCompoundDrawables()[0], null, right, null);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (right != null) {
            dRight = right;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && dRight != null) {
            rBounds = dRight.getBounds();
            final int x = (int) event.getX();
            final int y = (int) event.getY();
//            System.out.println("x:/y: "+x+"/"+y);
//            System.out.println("bounds: "+rBounds.left+"/"+rBounds.right+"/"+rBounds.top+"/"+rBounds.bottom);
            //check to make sure the touch event was within the bounds of the drawable
            if (x >= (this.getRight() - offset - rBounds.width() - DensityUtils.dp2px(context, 15) * 2)
                    && x <= (this.getRight() - this.getPaddingRight())
                    && y >= this.getPaddingTop()
                    && y <= (this.getHeight() - this.getPaddingBottom())
                    && getCompoundDrawables()[2] != null) {
                //System.out.println("touch");
                this.setText("");
                event.setAction(MotionEvent.ACTION_CANCEL);//use this to prevent the keyboard from coming up
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        dRight = null;
        rBounds = null;
        super.finalize();
    }
}