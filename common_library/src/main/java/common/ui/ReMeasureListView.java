package common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ReMeasureListView extends ListView{

	public ReMeasureListView(Context context) {
        this(context, null);
    }

    public ReMeasureListView(Context context, AttributeSet attrs) {
    	this(context, attrs,0) ;
    }

    public ReMeasureListView(Context context, AttributeSet attrs, int defStyleAttr) {
    	super(context, attrs, defStyleAttr);
		setFocusable(false);
    }


	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
	            MeasureSpec.AT_MOST); 
	    super.onMeasure(widthMeasureSpec, expandSpec); 
	} 

}
