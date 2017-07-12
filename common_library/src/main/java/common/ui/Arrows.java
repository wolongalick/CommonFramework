package common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.common.R;

/**
 * Created by cxw on 2016/1/14.
 */
public class Arrows extends ImageView{
    private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;
    private int width;
    private int height;
    private int color;

    public Arrows(Context context) {
        this(context, null);
    }

    public Arrows(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Arrows(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Arrows);
        color = typedArray.getColor(R.styleable.Arrows_arrows_color, Color.parseColor("#13b9ff"));

        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(color);
        Path path=new Path();
        path.moveTo(0,0);
        path.lineTo(width, height / 2);
        path.lineTo(0,height);
        path.close();
        canvas.drawPath(path,paint);

        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
    }
}
