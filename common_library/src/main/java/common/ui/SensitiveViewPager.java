package common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.common.R;

import java.lang.reflect.Field;

import common.utils.BLog;

/**
 * @ClassName: SensitiveViewPager
 * @Description: 可调节灵敏度的ViewPager
 * @author: moonlight
 * @date: 2016-8-13 下午3:20:59
 */
public class SensitiveViewPager extends ViewPager {
    private VelocityTracker tracker;
	private int snap_velocity = 600;
	private float snap_distance_percent = 0.3f;
	private int downX;
	private final String TAG = "SensitiveViewPager";
	private int snap_duration = 250;
	private FixedSpeedScroller scroller;


	private boolean scrollable = true;
 
	public SensitiveViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SensitiveViewPager);
		snap_velocity = array.getInt(R.styleable.SensitiveViewPager_snap_velocity, snap_velocity);
		snap_distance_percent = array.getFloat(R.styleable.SensitiveViewPager_snap_distance_percent, snap_distance_percent);
		snap_duration = array.getInt(R.styleable.SensitiveViewPager_snap_duration, snap_duration);
		array.recycle();
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			scroller = new FixedSpeedScroller(getContext());
			field.set(this, scroller);
			scroller.setDuration(snap_duration);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	public static class FixedSpeedScroller extends Scroller {
		private int mDuration;
		public FixedSpeedScroller(Context context) {
			super(context);
		}
		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}
		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
		public void setDuration(int time) {
			mDuration = time;
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if(this.scrollable) {
			if (getCurrentItem() == 0 && getChildCount() == 0) {
				return false;
			}
		} else {
			return false;
		}


		try {
			switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) event.getX();
                    if (!scroller.isFinished()) {
                        scroller.abortAnimation();
                    }
                    break;
                default:
                    break;
            }
			return super.onInterceptTouchEvent(event);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int currentItem = getCurrentItem();


		if(this.scrollable) {
			if (getCurrentItem() == 0 && getChildCount() == 0) {
				return false;
			}
		} else {
			return false;
		}



		if (tracker == null) {
			tracker = VelocityTracker.obtain();
		}
		tracker.addMovement(event);
		boolean isHandle = false;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) event.getX();
				break;
			case MotionEvent.ACTION_UP:
				tracker.computeCurrentVelocity(1000);
				int pageCount = getAdapter() == null ? 0 : getAdapter().getCount();
				float XVelocity = tracker.getXVelocity();
				int curX = (int) event.getX();
				int snapDistance = curX - downX;
				BLog.i(TAG,"距离:"+snapDistance);
				//1.如果速度够快,则切换页面
				if (Math.abs(XVelocity) > snap_velocity) {
					BLog.i(TAG, "速度:" + XVelocity + " currentItem:" + currentItem + " pageCount:" + pageCount);
					if (snapDistance > 0 && currentItem > 0) {
						setCurrentItem(currentItem - 1, true);
						isHandle = true;
					} else if (snapDistance < 0 && currentItem < pageCount - 1) {
						setCurrentItem(currentItem + 1, true);
						isHandle = true;
					}
				//2.如果切换距离够长(按屏幕宽度百分比算的),则切换界面
				} else {

					float minDistance = getWidth() * snap_distance_percent;
					if (Math.abs(snapDistance) > minDistance) {
						BLog.i(TAG, "距离:" + snapDistance + " currentItem:" + currentItem + " pageCount:" + pageCount + " downX:" + downX + " curX:" + curX);
						if (currentItem > 0 && snapDistance > 0) {
							setCurrentItem(currentItem - 1, true);
							isHandle = true;
						} else if (snapDistance < 0 && currentItem < pageCount - 1) {
							setCurrentItem(currentItem + 1, true);
							isHandle = true;
						}
					}else {
						BLog.e(TAG,"切换viewpager失败:速度:"+XVelocity+",距离:"+snapDistance+",要求最短距离:"+minDistance);
					}
				}
				if (tracker != null) {
					tracker.recycle();
					tracker = null;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_CANCEL:
				if (tracker != null) {
					tracker.recycle();
					tracker = null;
				}
				break;
			default:
				break;
		}
		return !isHandle ? super.onTouchEvent(event) : true;
	}

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}
}