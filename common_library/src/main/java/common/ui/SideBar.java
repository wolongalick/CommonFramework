package common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.common.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SideBar extends View {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public static String[] allLetter = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z"};
	public  List<String> letterList=new ArrayList<String>();
	private int choose = -1;// 选中
	private Paint paint = new Paint();

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}
	
	private Context context;
	/**单个字母高度*/
	private int singleHeight;
	/**现有字母个数*/
	private int count;
	private int firstHeight;

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	public SideBar(Context context) {
		super(context);
		this.context=context;
	}

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		count=letterList.size();
		if(count==0){
			return;
		}
		// 获取焦点改变背景颜色.
		int height = getHeight();// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		singleHeight = height / 27;// 获取每一个字母的高度
		//26个字母中第一个字母的Y轴位置
		firstHeight=(height-singleHeight*count)/2;
		
		for (int i = 0; i < count; i++) {
			paint.setColor(Color.rgb(33, 65, 98));
			// paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(context.getResources().getDimension(R.dimen.size_20));
			// 选中的状态
			if (i == choose) {
//				paint.setColor(Color.parseColor("#3399ff"));
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(letterList.get(i)) / 2;
//			float yPos = singleHeight * i + singleHeight;
			float yPos = firstHeight+singleHeight*i;
			canvas.drawText(letterList.get(i), xPos, yPos, paint);
			paint.reset();// 重置画笔
		}
		/*
		 * 总长度100
		 * 字母个数1:a
		 * a位置:50
		 * 
		 * 字母个数2:a,b
		 * a位置:33,b位置66
		 * 
		 * 字母个数3:a,b,c
		 * a位置:25,b位置50,c位置75
		 * 
		 */

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		//索引
		final int index = (int) ((y-firstHeight) / (singleHeight*count) * letterList.size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_selector);
			if (oldChoose != index) {
				if (index >= 0 && index < letterList.size()) {
					if (listener != null) {
						listener.onTouchingLetterChanged(letterList.get(index));
					}
					if (mTextDialog != null) {
						mTextDialog.setText(letterList.get(index));
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = index;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		void onTouchingLetterChanged(String s);
	}

	public  void setLetterList(List<String> letterList) {
		this.letterList = letterList;
		invalidate();
	}

	public void setupAllLetter(){
		letterList.clear();
		letterList.addAll(Arrays.asList(allLetter));
	}
	
}