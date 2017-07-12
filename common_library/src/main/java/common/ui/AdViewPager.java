package common.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 广告图(轮播图)使用的类
 * Created by cxw on 2016/4/25.
 */
public class AdViewPager extends ViewPager {
    // 定时任务
    private ScheduledExecutorService executorService;

    private int currentItem;

    // Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setCurrentItem(currentItem);
        }
    };

    public AdViewPager(Context context) {
        super(context);
    }

    public AdViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 开始轮播图切换
     */
    public void startPlay() {
        if(executorService !=null && !executorService.isShutdown()){
            stopPlay();
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    public void stopPlay() {
        executorService.shutdownNow();
    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            currentItem = (currentItem + 1) % getChildCount();
            handler.obtainMessage().sendToTarget();
        }
    }
}
