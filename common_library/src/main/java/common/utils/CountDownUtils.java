package common.utils;

import android.os.Handler;

import java.text.NumberFormat;

/**
 * 时分秒倒计时工具类
 */
public class CountDownUtils {

	long totalMillis;
	CountDownListener countDownListener;

	Handler handler;
	NumberFormat numberFormat;

	public CountDownUtils(long totalMillis, CountDownListener countDownListener) {
		this.totalMillis = totalMillis;
		this.countDownListener = countDownListener;
		handler = new Handler();
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumIntegerDigits(2);
		numberFormat.setGroupingUsed(false);

		this.countDownListener.onInit(this,
				numberFormat.format((totalMillis / 1000) / 60 / 60),
				numberFormat.format((totalMillis / 1000) / 60 % 60),
				numberFormat.format((totalMillis / 1000) % 60));
	}

	public void start() {
		processCountDown();
	}

	public void stop() {
		if (handler != null) {
			handler.removeCallbacks(process);
			countDownListener.onFinished(this);
		}
	}

	private void processCountDown() {
		if (totalMillis <= 0) {
			handler.removeCallbacks(process);
			countDownListener.onFinished(this);
			return;
		}
		handler.postDelayed(process, 1000);
	}

	private Runnable process = new Runnable() {

		@Override
		public void run() {
			totalMillis -= 1000;

			countDownListener.onProgress(CountDownUtils.this,
					numberFormat.format((totalMillis / 1000) / 60 / 60),
					numberFormat.format((totalMillis / 1000) / 60 % 60),
					numberFormat.format((totalMillis / 1000) % 60));

			processCountDown();
		}
	};

	public interface CountDownListener {
		void onInit(CountDownUtils countDownUtils, String hour, String minutes,
					String second);

		void onProgress(CountDownUtils countDownUtils, String hour,
						String minutes, String second);

		void onFinished(CountDownUtils countDownUtils);
	}
}
