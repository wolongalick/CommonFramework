package common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by cxw on 2015/12/4.
 */
public class CustomRadioButton extends RadioButton {
    private Context context;
    public CustomRadioButton(Context context) {
        this(context,null);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }





}

