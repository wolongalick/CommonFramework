package common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import common.utils.DensityUtils;

public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    private OnProgressListener onProgressListener;
    private OnGetTitleListener onGetTitleListener;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context,3), 0, 0));
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                //返回false防止跳转到系统浏览器
                return false;
            }
        });
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);         //支持js脚本
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码类型为utf-8
        webSettings.setDomStorageEnabled(true);         //使用缓存
        webSettings.setUseWideViewPort(true);           //设置webview推荐使用的窗口
        webSettings.setLoadWithOverviewMode(true);      //设置加载模式
        webSettings.setSupportZoom(true);               //支持缩放
        webSettings.setBuiltInZoomControls(true);       //支持缩放
        webSettings.setDisplayZoomControls(false);      //不显示缩放按钮
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            if(onProgressListener!=null){
            	onProgressListener.onProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if(onGetTitleListener!=null){
                onGetTitleListener.OnGetTitle(title);
            }

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(canGoBack()){
                //如果可以返回上一个WebView界面,则返回
                goBack();
                return true;
            }
        }
        return false;
    }
    
    public interface OnProgressListener{
    	void onProgress(int progress);
    }

    public interface OnGetTitleListener{
    	void OnGetTitle(String title);
    }


	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}

    public void setOnGetTitleListener(OnGetTitleListener onGetTitleListener) {
        this.onGetTitleListener = onGetTitleListener;
    }
}