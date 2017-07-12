package common.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {
	public MarqueeTextView(Context con) {
		super(con);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
		setMarqueeRepeatLimit(Integer.MAX_VALUE);
		setSingleLine();
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
		setMarqueeRepeatLimit(Integer.MAX_VALUE);
		setSingleLine();
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
		setMarqueeRepeatLimit(Integer.MAX_VALUE);
		setSingleLine();
	}

	@Override
	public boolean isFocused() {
		return true;
	}

}